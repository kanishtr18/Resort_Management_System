# API Endpoints Documentation

## Resort Management System - REST API Endpoints

**Generated:** March 15, 2026  
**Spring Boot Version:** 4.0.3  
**Total Endpoints:** 160+  
**Total Controllers:** 47

---

## Table of Contents

1. [Billing Module](#billing-module)
2. [Booking Module](#booking-module)
3. [Room Module](#room-module)
4. [Inventory Module](#inventory-module)
5. [HR Module](#hr-module)
6. [Marketing Module](#marketing-module)
7. [Pricing Module](#pricing-module)
8. [F&B Module](#fnb-module)
9. [Support Module](#support-module)
10. [Reporting Module](#reporting-module)
11. [Security Module](#security-module)

---

## Billing Module

### RefundController
**Base Path:** `/api/billing/refunds`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/billing/refunds` | Get all refunds |
| GET | `/api/billing/refunds/{id}` | Get refund by ID |
| POST | `/api/billing/refunds` | Create new refund request |
| POST | `/api/billing/refunds/{id}/process` | Process refund (update status to SUCCESS/FAILED) |

### PaymentController
**Base Path:** `/api/billing/payments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/billing/payments` | Get all payments |
| GET | `/api/billing/payments/{id}` | Get payment by ID |
| POST | `/api/billing/payments` | Create new payment |
| POST | `/api/billing/payments/{id}/process` | Process payment (PENDING → SUCCESS/FAILED) |

### InvoiceController
**Base Path:** `/api/billing/invoices`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/billing/invoices` | Get all invoices |
| GET | `/api/billing/invoices/{id}` | Get invoice by ID |
| POST | `/api/billing/invoices` | Create new invoice |
| PUT | `/api/billing/invoices/{id}` | Update invoice |
| POST | `/api/billing/invoices/{id}/issue` | Issue invoice (DRAFT → ISSUED) |

### FolioController
**Base Path:** `/api/billing/folios`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/billing/folios` | Get all folios |
| GET | `/api/billing/folios/{id}` | Get folio by ID |
| POST | `/api/billing/folios` | Create new folio for reservation |
| PUT | `/api/billing/folios/{id}` | Update folio |
| POST | `/api/billing/folios/{id}/close` | Close folio |
| POST | `/api/billing/folios/{id}/void` | Void folio (for cancelled/incorrect folios) |

### AccountLedgerController
**Base Path:** `/api/billing/ledger`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/billing/ledger` | Get all ledger accounts |
| GET | `/api/billing/ledger/{id}` | Get ledger account by ID |
| POST | `/api/billing/ledger` | Create new ledger account |
| PUT | `/api/billing/ledger/{id}` | Update ledger account |
| DELETE | `/api/billing/ledger/{id}` | Delete ledger account (zero balance only) |

---

## Booking Module

### ReservationController
**Base Path:** `/api/booking/reservations`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/booking/reservations` | Create new reservation |
| GET | `/api/booking/reservations` | List all reservations |
| GET | `/api/booking/reservations/{id}` | Get reservation details by ID |
| PUT | `/api/booking/reservations/{id}` | Update reservation |
| DELETE | `/api/booking/reservations/{id}` | Cancel reservation |

### ReservationServiceBookingController
**Base Path:** `/api/booking/reservationservicebookings`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/booking/reservationservicebookings` | Book service for reservation (spa, shuttle, etc.) |
| DELETE | `/api/booking/reservationservicebookings/{serviceBookingId}` | Cancel service booking |

### ReservationRoomAssignmentController
**Base Path:** `/api/bookings/room-assignments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings/room-assignments` | Assign room to reservation |
| DELETE | `/api/bookings/room-assignments/{assignmentId}` | Unassign room from reservation |

### ReservationDailyRateController
**Base Path:** `/api/booking/reservationdailyrates`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/booking/reservationdailyrates/reservation/{reservationId}` | Get daily rates breakdown for reservation |

### BookingGuestController
**Base Path:** `/api/booking/bookingguests`

| Method | Endpoint | Description |
|--------|----------|-------------|
| DELETE | `/api/booking/bookingguests/{id}` | Remove guest from reservation |

### ReservationAddOnController
**Base Path:** `/api/booking/reservationaddons`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/booking/reservationaddons` | Add add-on to reservation (breakfast, airport pickup) |
| DELETE | `/api/booking/reservationaddons/{addOnId}` | Remove add-on from reservation |

---

## Room Module

### RoomTypeController
**Base Path:** `/api/room-types`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/room-types` | Create new room type |
| GET | `/api/room-types` | Get all room types |
| GET | `/api/room-types/{id}` | Get room type by ID |
| PUT | `/api/room-types/{id}` | Update room type |
| DELETE | `/api/room-types/{id}` | Delete room type |

### RoomController
**Base Path:** `/api/rooms`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/rooms` | Create new room |
| GET | `/api/rooms` | Get all rooms |
| GET | `/api/rooms/{id}` | Get room by ID |
| PUT | `/api/rooms/{id}` | Update room |
| DELETE | `/api/rooms/{id}` | Soft delete room |

### RoomBlockController
**Base Path:** `/api/room-blocks`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/room-blocks` | Create new room block |
| GET | `/api/room-blocks` | Get all room blocks |
| GET | `/api/room-blocks/{id}` | Get room block by ID |
| PUT | `/api/room-blocks/{id}` | Update room block |
| DELETE | `/api/room-blocks/{id}` | Delete room block |

### RoomAmenityController
**Base Path:** `/api/room-amenities`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/room-amenities` | Assign amenity to room |
| DELETE | `/api/room-amenities/{id}` | Remove amenity from room |

### AmenityController
**Base Path:** `/api/amenities`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/amenities` | Create new amenity |
| GET | `/api/amenities` | Get all amenities |
| GET | `/api/amenities/{id}` | Get amenity by ID |
| PUT | `/api/amenities/{id}` | Update amenity |
| DELETE | `/api/amenities/{id}` | Delete amenity |

### MaintenanceRequestController
**Base Path:** `/api/maintenance`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/maintenance` | Create maintenance request |
| GET | `/api/maintenance` | Get all open maintenance requests |
| DELETE | `/api/maintenance/{id}` | Close maintenance request |

### HousekeepingTaskController
**Base Path:** `/api/housekeeping`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/housekeeping` | Create housekeeping task |
| GET | `/api/housekeeping` | Get all housekeeping tasks |
| PUT | `/api/housekeeping/{id}` | Update housekeeping task |
| DELETE | `/api/housekeeping/{id}` | Delete housekeeping task |

---

## Inventory Module

### InventoryItemController
**Base Path:** `/api/inventory/items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/items?lowStock=true` | Get all inventory items (optionally filter by low stock) |
| GET | `/api/inventory/items/{id}` | Get inventory item by ID |
| POST | `/api/inventory/items` | Create inventory item |
| PUT | `/api/inventory/items/{id}` | Update inventory item |
| PATCH | `/api/inventory/items/{id}/adjust` | Adjust inventory quantity (atomic) |
| DELETE | `/api/inventory/items/{id}` | Delete inventory item |

### InventoryTransactionController
**Base Path:** `/api/inventory/transactions`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/transactions` | View inventory transactions |
| POST | `/api/inventory/transactions/manual` | Manual inventory adjustment (ADMIN only) |

### SupplierController
**Base Path:** `/api/inventory/suppliers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/suppliers?activeOnly=true` | Get all suppliers (optionally filter active only) |
| GET | `/api/inventory/suppliers/{id}` | Get supplier by ID |
| POST | `/api/inventory/suppliers` | Create supplier |
| PUT | `/api/inventory/suppliers/{id}` | Update supplier |
| DELETE | `/api/inventory/suppliers/{id}` | Delete supplier |

### PurchaseOrderController
**Base Path:** `/api/inventory/purchase-orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/purchase-orders` | View all purchase orders |
| GET | `/api/inventory/purchase-orders/{id}` | View purchase order by ID |
| POST | `/api/inventory/purchase-orders` | Create new purchase order |
| POST | `/api/inventory/purchase-orders/{id}/receive` | Receive purchase order (updates inventory) |

### PurchaseOrderLineController
**Base Path:** `/api/inventory/purchase-orders/{purchaseOrderId}/lines`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/purchase-orders/{purchaseOrderId}/lines` | Get all lines for purchase order |

---

## HR Module

### EmployeeController
**Base Path:** `/api/hr/employees`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/hr/employees` | Get all employees (paginated) |
| GET | `/api/hr/employees/{id}` | Get employee by ID |
| POST | `/api/hr/employees` | Create new employee (ADMIN only) |
| PUT | `/api/hr/employees/{id}` | Update employee (ADMIN only) |
| DELETE | `/api/hr/employees/{id}` | Delete employee (ADMIN only) |
| GET | `/api/hr/employees/available` | Get available employees (not on shift) |

### RoleController
**Base Path:** `/api/hr/roles`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/hr/roles` | Get all roles (paginated) |
| GET | `/api/hr/roles/{id}` | Get role by ID |
| POST | `/api/hr/roles` | Create new role |
| PUT | `/api/hr/roles/{id}` | Update role |
| DELETE | `/api/hr/roles/{id}` | Delete role |

### EmployeeRoleController
**Base Path:** `/api/hr/employee_roles`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/hr/employee_roles` | Get all employee roles (paginated) |
| GET | `/api/hr/employee_roles/{id}` | Get employee role by ID |
| POST | `/api/hr/employee_roles` | Assign role to employee |
| PUT | `/api/hr/employee_roles/{id}` | Update employee role |
| DELETE | `/api/hr/employee_roles/{id}` | Remove role from employee |

### ShiftScheduleController
**Base Path:** `/api/hr/shift-schedules`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/hr/shift-schedules` | Get all shift schedules (paginated) |
| GET | `/api/hr/shift-schedules/{id}` | Get shift schedule by ID |
| POST | `/api/hr/shift-schedules` | Create new shift schedule |
| PUT | `/api/hr/shift-schedules/{id}` | Update shift schedule |
| DELETE | `/api/hr/shift-schedules/{id}` | Delete shift schedule |
| GET | `/api/hr/shift-schedules/employee/{employeeId}` | Get shift schedules for employee |
| GET | `/api/hr/shift-schedules/range?startTime=&endTime=` | Get shift schedules by time range |

### PayrollController
**Base Path:** `/api/hr/payrolls`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/hr/payrolls` | Get all payrolls (paginated) |
| GET | `/api/hr/payrolls/{id}` | Get payroll by ID |
| POST | `/api/hr/payrolls` | Create new payroll |
| PUT | `/api/hr/payrolls/{id}` | Update payroll |
| DELETE | `/api/hr/payrolls/{id}` | Delete payroll |
| GET | `/api/hr/payrolls/employee/{employeeId}` | Get payrolls for employee |

---

## Marketing Module

### PackageController
**Base Path:** `/api/marketing/packages`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/marketing/packages` | Get all packages |
| GET | `/api/marketing/packages/{id}` | Get package by ID |
| POST | `/api/marketing/packages` | Create new package |
| PUT | `/api/marketing/packages/{id}` | Update package |
| DELETE | `/api/marketing/packages/{id}` | Delete package |

### PackageItemController
**Base Path:** `/api/marketing/package_items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/marketing/package_items` | Get all package items (paginated) |
| GET | `/api/marketing/package_items/{id}` | Get package item by ID |
| POST | `/api/marketing/package_items` | Add item to package |
| PUT | `/api/marketing/package_items/{id}` | Update package item |
| DELETE | `/api/marketing/package_items/{id}` | Remove item from package |

### LoyaltyMemberController
**Base Path:** `/api/marketing/loyalty-members`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/marketing/loyalty-members` | Get all loyalty members (paginated) |
| GET | `/api/marketing/loyalty-members/{id}` | Get loyalty member by ID |
| POST | `/api/marketing/loyalty-members` | Create new loyalty member |
| PUT | `/api/marketing/loyalty-members/{id}` | Update loyalty member |
| DELETE | `/api/marketing/loyalty-members/{id}` | Delete loyalty member |
| GET | `/api/marketing/loyalty-members/guest/{guestId}` | Get loyalty member by guest ID |

### PromotionController
**Base Path:** `/api/marketing/promotions`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/marketing/promotions` | Get all promotions (paginated) |
| GET | `/api/marketing/promotions/{id}` | Get promotion by ID |
| POST | `/api/marketing/promotions` | Create new promotion |
| PUT | `/api/marketing/promotions/{id}` | Update promotion |
| DELETE | `/api/marketing/promotions/{id}` | Delete promotion |

---

## Pricing Module

### PricingQuoteController
**Base Path:** `/api/pricing`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pricing/quote` | Calculate pricing quote for reservation |

### RatePlanController
**Base Path:** `/api/rate-plans`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/rate-plans` | List all rate plans |
| POST | `/api/rate-plans` | Create new rate plan |
| PUT | `/api/rate-plans/{id}` | Update rate plan |

### RateHistoryController
**Base Path:** `/api/rate-history`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/rate-history/{ratePlanId}` | Create seasonal rate override for rate plan |

---

## F&B Module

### MenuController
**Base Path:** `/api/fnb/menus`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/menus?activeOnly=true` | Get all menus (optionally filter active only) |
| GET | `/api/fnb/menus/{id}` | Get menu by ID |
| POST | `/api/fnb/menus` | Create new menu |
| PUT | `/api/fnb/menus/{id}` | Update menu |
| DELETE | `/api/fnb/menus/{id}` | Delete menu |

### MenuItemController
**Base Path:** `/api/fnb/menu-items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/menu-items?activeOnly=true` | Get all menu items (optionally filter active only) |
| GET | `/api/fnb/menu-items/{id}` | Get menu item by ID |
| POST | `/api/fnb/menu-items` | Create new menu item |
| PUT | `/api/fnb/menu-items/{id}` | Update menu item |
| DELETE | `/api/fnb/menu-items/{id}` | Soft delete menu item |

### MenuItemIngredientController
**Base Path:** `/api/fnb/menu-item-ingredients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/menu-item-ingredients` | Get all menu item ingredients (recipes) |
| GET | `/api/fnb/menu-item-ingredients/{id}` | Get menu item ingredient by ID |
| GET | `/api/fnb/menu-item-ingredients/menu-item/{menuItemId}` | Get all ingredients for menu item |
| POST | `/api/fnb/menu-item-ingredients` | Create menu item ingredient (recipe mapping) |
| PUT | `/api/fnb/menu-item-ingredients/{id}` | Update menu item ingredient |
| DELETE | `/api/fnb/menu-item-ingredients/{id}` | Delete menu item ingredient |

### OrderController
**Base Path:** `/api/fnb/orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/orders` | Get all orders |
| GET | `/api/fnb/orders/{id}` | Get order by ID |
| POST | `/api/fnb/orders` | Create new order (deducts inventory) |

### OrderItemController
**Base Path:** `/api/fnb/order-items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/order-items` | Get all active order items |
| GET | `/api/fnb/order-items/{id}` | Get order item by ID |
| POST | `/api/fnb/order-items` | Create new order item |
| PUT | `/api/fnb/order-items/{id}` | Update order item |
| DELETE | `/api/fnb/order-items/{id}` | Soft delete order item |

### ActivityEventController
**Base Path:** `/api/fnb/activity-events`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fnb/activity-events?activeOnly=true` | Get all activity events (optionally filter active only) |
| GET | `/api/fnb/activity-events/{id}` | Get activity event by ID |
| POST | `/api/fnb/activity-events` | Create new activity event |
| PUT | `/api/fnb/activity-events/{id}` | Update activity event |
| DELETE | `/api/fnb/activity-events/{id}` | Soft delete activity event |

---

## Support Module

### HelpTicketController
**Base Path:** `/api/support/tickets`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/support/tickets` | Create new help ticket |
| GET | `/api/support/tickets` | Get all help tickets |
| DELETE | `/api/support/tickets/{id}` | Close help ticket |

### FeedbackReviewController
**Base Path:** `/api/feedback`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/feedback` | Submit feedback/review |
| GET | `/api/feedback` | Get all feedback/reviews |
| PUT | `/api/feedback/{id}/respond/{staffId}` | Respond to feedback as staff member |

### CommunicationController
**Base Path:** `/api/communications`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/communications` | Create and send communication (email/SMS) |
| GET | `/api/communications` | Get all communications |
| DELETE | `/api/communications/{id}` | Delete communication |

---

## Reporting Module

### AuditLogController
**Base Path:** `/api/reporting/audit-logs`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/reporting/audit-logs` | Get all audit logs |
| GET | `/api/reporting/audit-logs/{id}` | Get audit log by ID |
| GET | `/api/reporting/audit-logs/entity/{entityName}` | Get audit logs for specific entity type |
| GET | `/api/reporting/audit-logs/target/{targetId}` | Get audit logs for specific entity instance |
| GET | `/api/reporting/audit-logs/user/{performedBy}` | Get audit logs by user who performed action |

### ReportMetaController
**Base Path:** `/api/reporting/reports`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/reporting/reports` | Get all report metadata |
| GET | `/api/reporting/reports/{id}` | Get report metadata by ID |
| POST | `/api/reporting/reports` | Create new report metadata |
| PUT | `/api/reporting/reports/{id}` | Update report metadata |

---

## Security Module

### AuthController
**Base Path:** `/api/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | User registration/signup |
| POST | `/api/auth/login` | User login/authentication |