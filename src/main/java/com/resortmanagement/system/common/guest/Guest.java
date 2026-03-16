// package com.resortmanagement.system.common.guest;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.UUID;

// import org.hibernate.annotations.UuidGenerator;

// import com.resortmanagement.system.booking.entity.Reservation;
// import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
// import com.resortmanagement.system.common.enums.GuestType;
// import com.resortmanagement.system.marketing.entity.LoyaltyMember;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Entity
// @Table(name = "guest")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Guest extends AuditableSoftDeletable {

//     @Id
//     @UuidGenerator
//     @Column(name = "guest_id", updatable = false, nullable = false)
//     private UUID id;

//     @NotBlank
//     @Column(name = "first_name", nullable = false, length = 100)
//     private String firstName;

//     @NotBlank
//     @Column(name = "last_name", nullable = false, length = 100)
//     private String lastName;

//     @Email
//     @NotBlank
//     @Column(name = "email", nullable = false, unique = true, length = 150)
//     private String email;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "guest_type", length = 50)
//     private GuestType guestType;

//     @Column(name = "phone", length = 20)
//     private String phone;

//     @Column(name = "address", length = 255)
//     private String address;

//     @Column(name = "dob")
//     private LocalDate dob;

//     @OneToOne   
//     @JoinColumn(name = "loyalty_id", nullable = true)
//     private LoyaltyMember loyaltyMember;

//     @Column(name = "age")
//     private Integer age;

//     @OneToMany(mappedBy = "guest",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//     private List<Reservation> reservations = new ArrayList<>();
// }

package com.resortmanagement.system.common.guest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.resortmanagement.system.booking.entity.Reservation;
import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import com.resortmanagement.system.common.enums.GuestType;
import com.resortmanagement.system.marketing.entity.LoyaltyMember;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest extends AuditableSoftDeletable {

    @Id
    @UuidGenerator
    @Column(name = "guest_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "guest_type", length = 50)
    private GuestType guestType;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "dob")
    private LocalDate dob;

    @OneToOne
    @JoinColumn(name = "loyalty_id", nullable = true)
    private LoyaltyMember loyaltyMember;

    @Column(name = "age")
    private Integer age;

    // FIX: Added @Builder.Default — Lombok's @Builder silently ignores field initializers (= new ArrayList<>())
    // without this annotation. Without it, calling Guest.builder().build() gives a NULL list instead of
    // an empty list, causing NullPointerExceptions when anything calls guest.getReservations().add(...)
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}