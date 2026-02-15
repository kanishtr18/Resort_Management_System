package com.resortmanagement.system.booking.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.booking.dto.request.AddOnSelectionRequest;
import com.resortmanagement.system.booking.dto.request.ReservationCreateRequest;
import com.resortmanagement.system.booking.dto.request.ReservationUpdateRequest;
import com.resortmanagement.system.booking.dto.response.ReservationDetailResponse;
import com.resortmanagement.system.booking.dto.response.ReservationResponse;
import com.resortmanagement.system.booking.entity.BookingGuest;
import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.booking.entity.ReservationAddOn;
import com.resortmanagement.system.booking.mapper.ReservationMapper;
import com.resortmanagement.system.booking.repository.ReservationRepository;
import com.resortmanagement.system.common.enums.AddOnStatus;
import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.common.guest.GuestRepository;
import com.resortmanagement.system.pricing.dto.request.GuestPricingInput;
import com.resortmanagement.system.pricing.dto.request.PricingQuoteRequest;
import com.resortmanagement.system.pricing.entity.RatePlan;
import com.resortmanagement.system.pricing.repository.RatePlanRepository;
import com.resortmanagement.system.pricing.service.PricingQuoteService;
import com.resortmanagement.system.room.entity.RoomType;
import com.resortmanagement.system.room.repository.RoomTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PricingQuoteService pricingQuoteService;
    private final RoomTypeRepository roomTypeRepository;
    private final RatePlanRepository ratePlanRepository;
    private final GuestRepository guestRepository;

    @Transactional
    public ReservationDetailResponse createReservation(ReservationCreateRequest request) {
        // Build Reservation
        Reservation reservation = buildReservationEntity(request);
        // Save Reservation
        reservationRepository.save(reservation);
        // Map to Response and return
        return ReservationMapper.toDetailResponse(reservation);
    }

    private Reservation buildReservationEntity(ReservationCreateRequest request) {
        // 1. Validation
        validateReservationDates(request);

        // 2. Resolve RatePlan
        RatePlan ratePlan = resolveRatePlan(request);

        // 3. Availability Check
        RoomType roomType = resolveRoomType(ratePlan, request);
        
        long overlapping = reservationRepository.countOverlappingReservations(roomType, request.getStartDate(),
                request.getEndDate());

        if (roomType.getTotalKeys() != null && overlapping >= roomType.getTotalKeys()) {
            throw new ApplicationException("No availability for the selected dates");
        }

        // 4. Create Entity
        Reservation reservation = ReservationMapper.toEntity(request);
        reservation.setRatePlan(ratePlan);

        // Set Booker (Primary Guest) linked to Reservation
        if (request.getGuestId() != null) {
            Guest booker = guestRepository.findByIdAndDeletedFalse(request.getGuestId())
                    .orElseThrow(() -> new ApplicationException("Guest (Booker) not found"));
            reservation.setGuest(booker);
        } else {
            throw new ApplicationException("Guest ID (Booker) is required");
        }

        // 5. Pricing Calculation
        PricingQuoteRequest quoteReq = new PricingQuoteRequest();
        GuestPricingInput input = new GuestPricingInput();
        input.setRatePlanId(ratePlan.getId());
        input.setCheckIn(request.getStartDate());
        input.setCheckOut(request.getEndDate());
        input.setNumberOfGuests(request.getNumGuests());
        quoteReq.setGuestInputs(List.of(input));

        com.resortmanagement.system.pricing.dto.response.PricingQuoteResponse quote = pricingQuoteService
                .calculate(quoteReq);

        // 6. Save Daily Rates
        if (quote.getGuestBreakdowns() != null && !quote.getGuestBreakdowns().isEmpty()) {
            var breakdown = quote.getGuestBreakdowns().get(0);
            for (var day : breakdown.getDailyBreakdown()) {
                com.resortmanagement.system.booking.entity.ReservationDailyRate dailyRate = new com.resortmanagement.system.booking.entity.ReservationDailyRate();
                dailyRate.setReservation(reservation);
                dailyRate.setDate(day.getDate());
                dailyRate.setAmount(day.getFinalPrice());
                dailyRate.setRatePlan(ratePlan);
                reservation.getDailyRates().add(dailyRate);
            }
        }

        // 7. Save Guests (Occupants)
        attachGuests(reservation, request);

        // Handle optional add-ons
        attachAddOns(reservation, request);

        return reservation;
    }

    private void validateReservationDates(ReservationCreateRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new ApplicationException("Start date and end date are required");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ApplicationException("Start date must be before end date");
        }
    }

    private RatePlan resolveRatePlan(ReservationCreateRequest request) {
        if (request.getRatePlanId() == null) {
            throw new ApplicationException("Rate Plan ID is required");
        }
        RatePlan ratePlan = ratePlanRepository
                .findByIdAndDeletedFalse(request.getRatePlanId())
                .orElseThrow(() -> new ApplicationException("Rate Plan not found"));
        if (request.getStartDate().isBefore(ratePlan.getValidFrom()) || request.getEndDate().isAfter(ratePlan.getValidTo())) {
            throw new ApplicationException("Start date is before rate plan valid from date");
        }
        return ratePlan;
    }

    private RoomType resolveRoomType(RatePlan ratePlan, ReservationCreateRequest request) {
        UUID roomTypeId = ratePlan.getRoomTypeId() != null
            ? ratePlan.getRoomTypeId().getId()
            : request.getRoomTypeId();

        if (roomTypeId == null) {
            throw new ApplicationException("Room Type not resolved");
        }

        return roomTypeRepository.findByIdAndDeletedFalse(roomTypeId)
            .orElseThrow(() -> new ApplicationException("Room Type not found"));
    }

    private void attachGuests(Reservation reservation, ReservationCreateRequest request) {
        if (request.getBookingGuests() == null) return;

        for (var guestReq : request.getBookingGuests()) {
            if (guestReq.getGuestId() == null) continue;

            Guest guest = guestRepository.findByIdAndDeletedFalse(guestReq.getGuestId())
                .orElseThrow(() -> new ApplicationException("Guest not found"));

            BookingGuest bg = new BookingGuest();
            bg.setReservation(reservation);
            bg.setGuest(guest);
            bg.setPrimary(Boolean.TRUE.equals(guestReq.getIsPrimary()));
            bg.setGuestType(guestReq.getGuestType());
            bg.setAge(guestReq.getAge());
            bg.setSpecialNeeds(guestReq.getSpecialNeeds());

            reservation.getBookingGuests().add(bg); // helper method
        }
    }

    private void attachAddOns(Reservation reservation, ReservationCreateRequest request) {
        if (request.getAddOns() == null) return;

        for (AddOnSelectionRequest sel : request.getAddOns()) {
            ReservationAddOn addOn = new ReservationAddOn();
            addOn.setReservation(reservation);
            addOn.setAddOnCode("ADDON_" + sel.getAddOnId());
            addOn.setQuantity(sel.getQuantity());
            addOn.setStatus(AddOnStatus.REQUESTED);
            reservation.getAddOns().add(addOn);
        }
        
    }

    @Transactional(readOnly = true)
    public ReservationDetailResponse getReservation(UUID id) {
        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException("Reservation not found"));
        return ReservationMapper.toDetailResponse(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> listReservations() {
        return reservationRepository.findByDeletedFalse()
                .stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }

    @Transactional
    public void updateReservation(UUID id, ReservationUpdateRequest request) {
        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException("Reservation not found"));
        ReservationMapper.updateEntity(reservation, request);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(UUID id) {
        Reservation reservation = reservationRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException("Reservation not found"));
        reservation.setDeleted(true);
        reservationRepository.save(reservation);
    }
}
