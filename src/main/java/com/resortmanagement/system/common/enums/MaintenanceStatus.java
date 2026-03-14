/*
<Enum>.java
Purpose:
 - Each enum is domain-specific. Use EnumType.STRING in entities.
Examples:
 - ReservationStatus: PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW
 - PaymentStatus: PENDING, SUCCESS, FAILED, REFUNDED
 - RoomStatus: AVAILABLE, OCCUPIED, DIRTY, OUT_OF_SERVICE, BLOCKED
 - OrderStatus: PLACED, PREPARING, SERVED, BILLED, CANCELLED
 - MaintenanceStatus: REPORTED, IN_PROGRESS, RESOLVED, CLOSED
Guidelines:
 - Keep enums small and precise; do NOT reuse a single generic Status enum across domains.
File: common/enums/<Enum>.java
*/

package com.resortmanagement.system.common.enums;

public enum MaintenanceStatus {

}
