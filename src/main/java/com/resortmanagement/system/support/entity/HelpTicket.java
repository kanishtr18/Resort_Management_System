/*
HelpTicket.java
Purpose:
 - Guest request / helpdesk ticket entity.
Fields:
 - id UUID
 - guestId UUID
 - reservationId UUID
 - category String
 - description String
 - priority String
 - status String (OPEN, ASSIGNED, RESOLVED, CLOSED)
 - assignedTo UUID
 - createdAt via Auditable
Notes:
 - Ticket lifecycle managed via HelpTicketService; write AuditLog entries on important state changes.
File: support/entity/HelpTicket.java
*/
package com.resortmanagement.system.support.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.support.enums.TicketPriority;
import com.resortmanagement.system.support.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "help_tickets")
@Getter
@Setter
public class HelpTicket extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(name = "ticket_number", unique = true)
    private String ticketNumber;

    private String category;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private Guest assignedTo;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
