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