Billing Repository template
Purpose:
 - All billing repositories must extend JpaRepository<Entity, UUID>.
 - Add domain-specific methods where necessary.

Examples:
 - InvoiceRepository:
    Optional<Invoice> findByIdAndDeletedFalse(UUID id);
    @Query to compute unpaid invoices for a reservation.

Implementation notes:
 - Avoid complex joins in repositories; push complex business logic to service or create read-only projections for reporting.
 - Use pagination on list endpoints to protect the DB.

File: billing/repository/<Entity>Repository.java