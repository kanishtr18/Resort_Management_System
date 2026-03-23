

package com.resortmanagement.system.inventory.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.inventory.dto.request.PurchaseOrderRequest;
import com.resortmanagement.system.inventory.dto.response.PurchaseOrderResponse;
import com.resortmanagement.system.inventory.entity.InventoryItem;
import com.resortmanagement.system.inventory.entity.PurchaseOrder;
import com.resortmanagement.system.inventory.entity.PurchaseOrderLine;
import com.resortmanagement.system.inventory.entity.PurchaseOrderStatus;
import com.resortmanagement.system.inventory.entity.Supplier;
import com.resortmanagement.system.inventory.mapper.PurchaseOrderMapper;
import com.resortmanagement.system.inventory.repository.InventoryItemRepository;
import com.resortmanagement.system.inventory.repository.PurchaseOrderRepository;
import com.resortmanagement.system.inventory.repository.SupplierRepository;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository repository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper mapper;
    private final InventoryItemRepository itemRepository;

    public PurchaseOrderService(
        PurchaseOrderRepository repository,
        SupplierRepository supplierRepository,
        PurchaseOrderMapper mapper,
        InventoryItemRepository itemRepository) {  // ← add
    this.repository = repository;
    this.supplierRepository = supplierRepository;
    this.mapper = mapper;
    this.itemRepository = itemRepository;  // ← add
}

    public List<PurchaseOrderResponse> findAll() {
        return repository.findAll().stream()
                .map(po -> {
                    Supplier supplier = supplierRepository.findById(po.getSupplier().getId()).orElse(null);
                    return mapper.toResponse(po, supplier, null);
                })
                .collect(Collectors.toList());
    }

    // @Transactional
    // public PurchaseOrderResponse create(PurchaseOrderRequest request) {
    //     Supplier supplier = supplierRepository.findById(request.getSupplierId())
    //             .orElseThrow(() -> new RuntimeException("Supplier not found"));

    //     PurchaseOrder po = mapper.toEntity(request, supplier);
    //     po.setStatus(PurchaseOrderStatus.CREATED);
    //     po.setPoNumber(UUID.randomUUID().toString().substring(0, 8));

    //     // FIX: totalAmount was declared and calculated but never assigned back to the PO — unused variable warning
    //     // Now: calculate total from request lines, then set it on the entity before saving
    //     BigDecimal totalAmount = BigDecimal.ZERO;
    //     List<PurchaseOrderLine> lines = new ArrayList<>();

    //     if (request.getLines() != null) {
    //         for (var lineRequest : request.getLines()) {
    //             // FIX: getQuantity() does not exist — field is named qty (BigDecimal), so use getQty() directly
    //             BigDecimal lineTotal = lineRequest.getUnitPrice()
    //                     .multiply(lineRequest.getQty());
    //             totalAmount = totalAmount.add(lineTotal);
    //         }
    //     }

    //     // FIX: This is what was missing — totalAmount was calculated but never set on the PO
    //     po.setTotalAmount(totalAmount);

    //     PurchaseOrder savedPo = repository.save(po);
    //     return mapper.toResponse(savedPo, supplier, lines);
    // }

  // Add this to constructor + field



@Transactional
public PurchaseOrderResponse create(PurchaseOrderRequest request) {
    Supplier supplier = supplierRepository.findById(request.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

    PurchaseOrder po = mapper.toEntity(request, supplier);
    po.setStatus(PurchaseOrderStatus.CREATED);
    po.setPoNumber(UUID.randomUUID().toString().substring(0, 8));

    BigDecimal totalAmount = BigDecimal.ZERO;
    List<PurchaseOrderLine> lineEntities = new ArrayList<>();

    if (request.getLines() != null) {
        for (var lineRequest : request.getLines()) {

            // ✅ Bug 1 Fix: fetch and set the InventoryItem
            InventoryItem item = itemRepository.findById(lineRequest.getInventoryItemId())
                    .orElseThrow(() -> new RuntimeException(
                        "Inventory item not found: " + lineRequest.getInventoryItemId()));

            BigDecimal lineTotal = lineRequest.getUnitPrice().multiply(lineRequest.getQty());
            totalAmount = totalAmount.add(lineTotal);

            PurchaseOrderLine line = new PurchaseOrderLine();
            line.setInventoryItem(item);         // ✅ Bug 1 Fix
            line.setQty(lineRequest.getQty());
            line.setUnitPrice(lineRequest.getUnitPrice());
            line.setTotalPrice(lineTotal);       // ✅ Bug 2 Fix
            line.setPurchaseOrder(po);

            lineEntities.add(line);
        }
    }

    po.setLines(lineEntities);
    po.setTotalAmount(totalAmount);

    PurchaseOrder savedPo = repository.save(po);
    return mapper.toResponse(savedPo, supplier, savedPo.getLines());
}

    public Optional<PurchaseOrderResponse> findById(UUID id) {
        return repository.findById(id).map(po -> {
            Supplier supplier = supplierRepository.findById(po.getSupplier().getId()).orElse(null);
            return mapper.toResponse(po, supplier, null);
        });
    }

    @Transactional
    public void receive(UUID id) {
        PurchaseOrder po = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        // FIX: Empty if-block was left as a placeholder — added a proper status guard
        // Only allow receiving a PO that has been SENT (or CREATED, depending on your workflow)
        if (po.getStatus() != PurchaseOrderStatus.SENT && po.getStatus() != PurchaseOrderStatus.CREATED) {
            throw new IllegalStateException(
                "Cannot receive PO with status: " + po.getStatus() + ". PO must be SENT or CREATED.");
        }

        po.setStatus(PurchaseOrderStatus.DELIVERED);
        repository.save(po);
    }
}