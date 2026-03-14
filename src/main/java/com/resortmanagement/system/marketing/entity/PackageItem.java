package com.resortmanagement.system.marketing.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.resortmanagement.system.common.audit.AuditableSoftDeletable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "package_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageItem extends AuditableSoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @ToString.Exclude
    private Package pkg;


    @Column(name = "room_type_id")
    private UUID roomTypeId;

    @Column(name = "service_item_id")
    private UUID serviceItemId;

    @Column(name = "menu_item_id")
    private UUID menuItemId;

    @OneToMany(mappedBy = "packageItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageInventoryItem> inventoryItems = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal qty;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}