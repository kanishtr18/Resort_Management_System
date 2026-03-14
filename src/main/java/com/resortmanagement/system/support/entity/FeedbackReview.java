/*
FeedbackReview.java
Purpose:
 - Guest feedback and rating for a stay or service.
Fields:
 - id UUID
 - guestId UUID
 - reservationId UUID
 - rating int (1..5)
 - comments String
 - responseBy UUID (staff responding)
 - respondedAt Instant
 - extends Auditable
Notes:
 - Consider moderation + public/private flags.
File: support/entity/FeedbackReview.java
*/
package com.resortmanagement.system.support.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.guest.Guest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "feedback_reviews")
@Getter
@Setter
public class FeedbackReview extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(nullable = false)
    private Integer rating;

    private String comments;

    @Column(name = "response_by")
    private UUID responseBy;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
