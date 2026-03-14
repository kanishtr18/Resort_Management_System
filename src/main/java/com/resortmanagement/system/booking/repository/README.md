Booking repository guidelines
Purpose:
 - All booking repositories extend JpaRepository<Entity, UUID>.
 - Add domain queries:
   - ReservationRepository.countOverlappingReservations(roomTypeId, start, end, blockingStatuses)
   - ReservationDailyRateRepository.findByReservationIdOrderByDate
   - ReservationServiceBookingRepository.findUpcomingByReservation

Notes:
 - Use @Query or method naming; keep logic in services.
 - Use pagination for listing operations.

File: booking/repository/<Entity>Repository.java
