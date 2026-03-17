# Project Conventions & Common Templates

> This file is the **canonical** project conventions and module-by-module TODO templates. Paste the relevant module TODO block at the top of each domain file and enforce these conventions across the team.

---

## Project Conventions (must follow) ✅

1) Entities:
   - Use `@Entity`, `@Table(name = "...")`.
   - Primary keys: **UUID** with `@GeneratedValue` (Hibernate UUIDGenerator) unless otherwise noted.
   - Extend `com.resortmanagement.system.common.audit.Auditable` if the entity is business-critical (reservation, invoice, payment, order, inventoryTx, maintenanceRequest, employee).
   - Use `@Enumerated(EnumType.STRING)` for enums.
   - Use `fetch = FetchType.LAZY` on `@ManyToOne` or big collections.
   - Use `@Version` for optimistic concurrency on inventory_item and other hot-updated tables where needed.

2) Repositories:
   - Extend `JpaRepository<Entity, UUID>`.
   - Add domain-specific query methods and `@Query` only when necessary.
   - No business logic in repositories.

3) Services:
   - Put business logic here; annotate implementations with `@Service`.
   - Use `@Transactional` on methods that change data (propagation REQUIRED).
   - Services call repositories and other services only.

4) Controllers:
   - Thin controllers; validate DTOs with `@Valid` and map to service calls.
   - Do not accept entity objects from clients. Use DTOs.

5) DTOs and Mappers:
   - Create Request/Response DTOs for each API.
   - Use MapStruct or manual mappers in `common.mapper`.

6) Logging, exceptions:
   - Use structured logging (include correlation id).
   - Use custom exceptions under `common.exception` and a global `@ControllerAdvice`.

7) Tests:
   - Unit tests for services with Mockito.
   - Integration tests with Testcontainers Postgres.

8) Auditing & Soft delete:
   - Use Auditable for WHO & WHEN fields.
   - Use soft-delete pattern for business entities (Reservation, Invoice, Payment): add `deleted` + `deletedAt` and handle logically in service layer.

9) Design patterns:
   - Builder for complex object creation.
   - Factory for creation policies (ReservationFactory, InvoiceFactory, PaymentFactory).
   - AbstractFactory for payment gateways and notification factories.

---

## Module-by-module TODO blocks (paste the appropriate block into the top of each file)

> Use these exact blocks when creating or updating files in each domain. They provide WHAT/WHY/HOW guidance for implementers and reviewers.

### Billing

#### billing/controller/AccountLedgerController.java
/*
AccountLedgerController.java
Purpose:
 - Expose REST endpoints for ledger queries and adjustments.
 - Controller should be thin: validate DTOs, call AccountLedgerService.

Endpoints (suggested):
 - GET /api/billing/ledgers -> list ledgers (pageable)
 - GET /api/billing/ledgers/{id} -> get ledger detail
 - POST /api/billing/ledgers/adjust -> admin-only adjustment (body: ledgerId, amount, reason)

Responsibilities:
 - Use DTOs for input/output (AccountLedgerRequest, AccountLedgerResponse).
 - Validate user roles with @PreAuthorize (e.g., ROLE_FINANCE, ROLE_ADMIN).
 - Map exceptions to proper HTTP codes via global @ControllerAdvice.

Implementation notes:
 - Do NOT implement business logic here. Call AccountLedgerService.
 - Include simple request logging and correlation id (from header).
 - Return appropriate 201/200/404 statuses.

File: billing/controller/AccountLedgerController.java
*/

#### billing/entity/AccountLedger.java
/*
AccountLedger.java
Purpose:
 - Entity for accounting ledger/accounts used by financial flows (room revenue, F&B, cash).
Annotations & fields:
 - @Entity, @Table(name = "account_ledger")
 - id: UUID PK
 - accountCode String (unique)
 - name String
 - accountType enum (ASSET/LIABILITY/REVENUE/EXPENSE)
 - balance BigDecimal (nullable, use BigDecimal)
 - currency String (ISO)
 - @Version Long version (optimistic locking)
 - extends Auditable (since ledger updates are critical for audit)
 - soft-delete not required but allowed for archival

Behavior:
 - Keep minimal logic in entity (no methods that mutate business state).
 - Use Repository for CRUD and Service for transactional updates.

Usage:
 - Ledger adjustments performed through AccountLedgerService (transactional).

File: billing/entity/AccountLedger.java
*/

#### billing/controller/FolioController.java
/*
FolioController.java
Purpose:
 - Expose folio operations for a reservation: list folios, create folio, attach items, close folio.
Suggested endpoints:
 - GET /api/reservations/{reservationId}/folios
 - POST /api/reservations/{reservationId}/folios  (create main folio or additional folio)
 - POST /api/folios/{folioId}/assign-item  (assign Order/Service/Addon to folio)
 - POST /api/folios/{folioId}/invoice  (generate invoice for folio)

Responsibilities:
 - Validate guest/reservation existence before invoking service.
 - Use DTOs (FolioRequest, FolioResponse, AssignItemRequest).
 - Ensure that folio creation and assignments are transactional in service layer.
 - Use @PreAuthorize for access control (frontdesk, finance).

File: billing/controller/FolioController.java
*/

#### billing/entity/Folio.java
/*
Folio.java
Purpose:
 - Billing bucket entity; multiple folios per reservation allow split billing.
Fields & annotations:
 - @Entity @Table("folio")
 - id: UUID PK
 - reservationId: UUID @ManyToOne -> Reservation (optional lazy)
 - name: String (e.g., "Main Folio", "Incidentals - Guest A")
 - bookingGuestId: UUID nullable (if folio is assigned to a specific BookingGuest)
 - status enum (OPEN/CLOSED)
 - createdAt/updatedAt handled by Auditable -> extend Auditable
 - totalAmount computed or stored (BigDecimal) - prefer compute in query or maintain by service
 - soft-delete not required; closing is a state change
Behavior:
 - No heavy logic inside entity.
 - Provide equals/hashCode only on id.

File: billing/entity/Folio.java
*/

#### billing/controller/InvoiceController.java
/*
InvoiceController.java
Purpose:
 - REST endpoints for invoice life cycle: create invoice from folio, get invoice, list invoices, mark paid/refund.
Suggested endpoints:
 - POST /api/folios/{folioId}/invoices -> create invoice
 - GET /api/invoices/{invoiceId}
 - POST /api/invoices/{invoiceId}/pay -> invoke PSP through PaymentService
 - POST /api/invoices/{invoiceId}/refund -> create refund

Responsibilities:
 - Use DTOs: CreateInvoiceRequest, InvoiceResponse, PaymentRequest.
 - Input validation (currency consistency, amounts).
 - Delegate to InvoiceService for transactional work.
 - Ensure invoice persistence uses ReservationDailyRate and folio line items.

File: billing/controller/InvoiceController.java
*/

#### billing/entity/Invoice.java
/*
Invoice.java
Purpose:
 - Persisted financial document generated from folio charges.
Fields:
 - id: UUID
 - folioId: UUID (ManyToOne -> Folio)
 - reservationId: UUID (optional)
 - guestId: UUID
 - issueDate: Instant
 - dueDate: Instant
 - totalAmount: BigDecimal
 - taxAmount: BigDecimal
 - status enum (DRAFT, ISSUED, PAID, PARTIALLY_PAID, REFUNDED, CANCELLED)
 - currency String
 - extends Auditable (must track who issued)
 - store request/response snapshot JSON if needed (for dispute)
Notes:
 - Immutable once ISSUED except for credit/refund operations stored separately.
 - Use InvoiceService to compute totals; never compute in controllers.

File: billing/entity/Invoice.java
*/

#### billing/controller/PaymentController.java
/*
PaymentController.java
Purpose:
 - Accept payment requests and forward to PaymentService; handle idempotency and webhooks.
Endpoints:
 - POST /api/invoices/{invoiceId}/pay -> client triggers payment
 - POST /api/payments/webhook -> PSP webhook endpoint (idempotent)
 - GET /api/payments/{paymentId}
Responsibilities:
 - Use PaymentRequest DTO containing paymentMethod, token, amount, idempotencyKey.
 - Validate amounts match invoice outstanding.
 - Use PaymentService (transactional) for recording Payment and updating Invoice status.
 - Secure webhook with signature check and idempotency key handling.

File: billing/controller/PaymentController.java
*/

#### billing/entity/Payment.java
/*
Payment.java
Purpose:
 - Records payment attempts and results for invoices.
Fields:
 - id: UUID
 - invoiceId: UUID (ManyToOne -> Invoice)
 - reservationId: UUID (nullable)
 - guestId: UUID
 - amount: BigDecimal
 - method: enum (CARD, UPI, CASH, WALLET)
 - transactionRef: String (PSP reference)
 - status: PaymentStatus (PENDING, SUCCESS, FAILED, REFUNDED)
 - providerResponse: JSON/string for PSP raw response
 - processedAt: Instant
 - extends Auditable (important for PCI/compliance trace)
Notes:
 - Do NOT store card PAN/CVV. Use PSP tokenization.
 - Payment creation and invoice update should be handled in a single transactional service method with idempotency.

File: billing/entity/Payment.java
*/

#### billing/controller/RefundController.java
/*
RefundController.java
Purpose:
 - Expose endpoints to create refunds against payments/invoices.
Endpoints:
 - POST /api/payments/{paymentId}/refund -> create refund request (amount, reason)
 - GET /api/refunds/{refundId}
Responsibilities:
 - Validate refund business rules (amount <= refundable).
 - Use RefundService (transaction) to create refund record, call PSP refund API, update Payment/Invoice statuses.
 - Use proper RBAC (finance/admin only).
File: billing/controller/RefundController.java
*/

#### billing/entity/Refund.java
/*
Refund.java
Purpose:
 - Persist refund operations and statuses.
Fields:
 - id: UUID
 - paymentId: UUID (FK to Payment)
 - invoiceId: UUID (optional)
 - amount: BigDecimal
 - reason: String
 - processedBy: String (auditable but keep as field for quick reporting)
 - processedAt: Instant
 - status: enum (REQUESTED, PROCESSING, SUCCESS, FAILED)
 - providerRefundRef: String
 - extends Auditable
Notes:
 - All refund flows must be idempotent and logged in AuditLog with reason.
 - Do not perform external PSP ops in the controller; do it in RefundService with retries and idempotency.
File: billing/entity/Refund.java
*/

#### billing/repository/*Repository.java (general)
/*
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
*/

#### billing/service/*Service.java (general)
/*
Billing Service template
Purpose:
 - All services implement an interface in service package and provide a @Service implementation.
 - Services contain transactional business logic.

Guidelines:
 - Annotate implementation methods with @Transactional where mutation occurs.
 - Use ReservationService/HoldService for cross-domain checks (inject required services).
 - Handle domain exceptions using custom exceptions in common.exception and map via global exception handler.

Examples of required methods:
 - InvoiceService.createInvoiceFromFolio(UUID folioId)
 - PaymentService.chargeInvoice(UUID invoiceId, PaymentRequest)
 - RefundService.createRefund(UUID paymentId, BigDecimal amount, String reason)

Logging & Monitoring:
 - Add structured log lines with invoice/reservation ids.
 - Emit metrics for payment success/fail rates.

File: billing/service/<Service>.java
*/

### Booking

... (truncated for brevity in README) ...

---

## Cross-cutting notes & advanced guidance (paste to a team doc)

### Advanced Implementation Guidance (important):

A) Concurrency & Availability:
 - Use DB row-level locking or Redis distributed locks keyed on "roomType:{id}:{date}" during booking.
 - BookingService.createReservation should:
   1) Begin @Transactional
   2) Acquire lock (DB FOR UPDATE on a room_type_availability or Redis lock)
   3) Check availability using COUNT of overlapping confirmed reservations vs room count
   4) Persist Reservation and ReservationDailyRate rows
   5) Commit

B) Inventory consumption:
 - Use single SQL atomic update to decrement inventory quantities:
   UPDATE inventory_item SET quantity_on_hand = quantity_on_hand - :qty WHERE id = :id AND quantity_on_hand >= :qty
 - If rowsUpdated == 0 -> insufficient stock -> rollback

C) Pricing:
 - PricingService.computeNightlyPrice(roomTypeId, date, ratePlanId, promoCode) returns BigDecimal
 - BookingService stores price snapshots into ReservationDailyRate.

D) Factories & Builders:
 - ReservationFactory for different booking sources (online, OTA, frontdesk).
 - InvoiceFactory to create invoices from Folio with tax computation.
 - Use Lombok @Builder on entities with many fields (use caution with JPA; create a DTO builder then map if necessary).

E) Audit & AuditLog:
 - Auditable: automatic who/when
 - AuditLog: write in services for any meaningful change (status transitions, financial adjustments)
 - Keep AuditLog write as part of the same transaction for important flows.

F) Testing:
 - Unit test services with mocked repositories.
 - Integration tests using Testcontainers for booking concurrency and inventory consumption.

G) Error handling:
 - Use global ControllerAdvice to map exceptions into standard API error format with error codes (NO_AVAILABILITY, INSUFFICIENT_STOCK, PAYMENT_FAILED).

H) Security:
 - Use Spring Security JWT or OAuth2
 - Protect endpoints: method-level @PreAuthorize for roles
 - Integrate SecurityContext user into AuditorAware implementation

I) Schema & Migrations:
 - Use Flyway; create migration scripts for each domain migration step.
 - Add constraints, indexes (reservation(room_type_id, start_date,end_date) etc.)

---

Notes
- Use this document as the authoritative team policy; update via PRs and keep it in sync with infra and migrations.
