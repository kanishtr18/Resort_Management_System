Booking Service guidelines
Purpose:
 - Implement business flows in service layer; avoid logic in controllers or repos.

Examples of methods to implement:
 - ReservationService.createReservation(CreateReservationRequest)
    * Acquire lock for room_type/date-range
    * Validate availability
    * Persist reservation + ReservationDailyRate rows (atomic)
    * Create folio(s) and respond with DTO

 - ReservationService.checkIn(UUID reservationId, Optional<roomId>)
    * Assign room, set reservation status, set room status OCCUPIED
    * Should be transactional

 - ReservationDailyRateService.getRatesForReservation(UUID reservationId)

Guidelines:
 - Use @Transactional on mutation methods
 - Throw domain-specific exceptions (NoAvailabilityException, OverCapacityException)
 - Write unit tests for business rules and integration tests for concurrency (Testcontainers).

File: booking/service/<Service>.java