/*
Communication.java
Purpose:
 - Store outbound messages (type, to, subject, snippet, status).
Fields:
 - id UUID
 - guestId UUID
 - type String (EMAIL,SMS)
 - to String (email/phone)
 - subject String
 - bodySnippet String
 - channel String (provider)
 - status String (SENT, FAILED)
 - sentAt Instant
 - extends Auditable
Notes:
 - For large email bodies, store in file store and only snippet in DB.
File: support/entity/Communication.java
*/
package com.resortmanagement.system.support.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.guest.Guest;
import com.resortmanagement.system.support.enums.CommunicationStatus;
import com.resortmanagement.system.support.enums.CommunicationType;

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
@Table(name = "communications")
@Getter
@Setter
public class Communication extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Enumerated(EnumType.STRING)
    private CommunicationType type;

    @Column(name = "recipient")
    private String to;

    private String subject;

    @Column(name = "body_snippet")
    private String bodySnippet;

    @Enumerated(EnumType.STRING)
    private CommunicationStatus status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private String channel;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
