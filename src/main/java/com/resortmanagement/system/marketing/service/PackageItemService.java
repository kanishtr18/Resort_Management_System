package com.resortmanagement.system.marketing.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.fnb.repository.ServiceItemRepository;
import com.resortmanagement.system.fnb.service.MenuItemService;
import com.resortmanagement.system.inventory.entity.InventoryItem;
import com.resortmanagement.system.inventory.repository.InventoryItemRepository;
import com.resortmanagement.system.marketing.dto.PackageItemDTO;
import com.resortmanagement.system.marketing.entity.Package;
import com.resortmanagement.system.marketing.entity.PackageInventoryItem;
import com.resortmanagement.system.marketing.entity.PackageItem;
import com.resortmanagement.system.marketing.repository.PackageItemRepository;
import com.resortmanagement.system.marketing.repository.PackageRepository;
import com.resortmanagement.system.room.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

// @Service
// @Transactional
// public class PackageItemService {

//     private final PackageItemRepository repository;
//     private final PackageRepository packageRepository;
//     private final RoomTypeService roomTypeService;
//     private final ServiceItemRepository serviceItemRepository;
//     private final MenuItemService menuItemService;
//     private final InventoryItemService inventoryItemService;
    
//     public PackageItemService(
//             PackageItemRepository repository,
//             PackageRepository packageRepository,
//             RoomTypeService roomTypeService,
//             ServiceItemRepository serviceItemRepository,
//             MenuItemService menuItemService,
//             InventoryItemService inventoryItemService) {
//         this.repository = repository;
//         this.packageRepository = packageRepository;
//         this.roomTypeService = roomTypeService;
//         this.serviceItemRepository = serviceItemRepository;
//         this.menuItemService = menuItemService;
//         this.inventoryItemService = inventoryItemService;
//     }

//     @Transactional(readOnly = true)
//     public org.springframework.data.domain.Page<PackageItemDTO> findAll(
//             org.springframework.data.domain.Pageable pageable) {
//         return repository.findByDeletedFalse(pageable).map(this::toDTO);
//     }

//     @Transactional(readOnly = true)
//     public Optional<PackageItemDTO> findById(UUID id) {
//         return repository.findByIdAndDeletedFalse(id).map(this::toDTO);
//     }

//     public PackageItemDTO save(PackageItemDTO dto) {
//         if (dto.getPkgId() == null) {
//             throw new IllegalArgumentException("Package ID is required");
//         }

//         PackageItem entity = new PackageItem();

//         // Resolve package
//         entity.setPkg(packageRepository.findById(dto.getPkgId())
//                 .orElseThrow(() -> new IllegalArgumentException("Package not found")));

//         // Set optional item IDs directly
//         entity.setRoomType(roomTypeService.getRoomType(dto.getRoomTypeId()));
//         entity.setServiceItem(serviceItemRepository.findByIdAndDeletedFalse(dto.getServiceItemId()).orElse(null));
//         entity.setMenuItem(menuItemService.findMenuItem(dto.getMenuItemId()));
//         entity.setInventoryItem(inventoryItemService.findInventoryItem(dto.getInventoryItemId()));

//         boolean hasItem = entity.getRoomType() != null ||
//                 entity.getServiceItem() != null ||
//                 entity.getMenuItem() != null ||
//                 entity.getInventoryItem() != null;

//         if (!hasItem) {
//             throw new IllegalArgumentException(
//                     "At least one item reference (RoomType, ServiceItem, MenuItem, InventoryItem) is required");
//         }

//         if (dto.getQty() == null || dto.getQty() <= 0) {
//             throw new IllegalArgumentException("Quantity must be greater than 0");
//         }
//         entity.setQty(dto.getQty());

//         if (dto.getPrice() == null || dto.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
//             throw new IllegalArgumentException("Price cannot be negative");
//         }
//         entity.setPrice(dto.getPrice());

//         return toDTO(repository.save(entity));
//     }

//     public PackageItemDTO update(UUID id, PackageItemDTO dto) {
//         return repository.findByIdAndDeletedFalse(id)
//                 .map(existing -> {
//                     // Update quantity and price
//                     if (dto.getQty() != null)
//                         existing.setQty(dto.getQty());
//                     if (dto.getPrice() != null)
//                         existing.setPrice(dto.getPrice());

//                     // Update relationships if provided
//                     if (dto.getPkgId() != null) {
//                         existing.setPkg(packageRepository.findById(dto.getPkgId())
//                                 .orElseThrow(() -> new IllegalArgumentException("Package not found")));
//                     }

//                     if (dto.getRoomTypeId() != null)
//                         existing.setRoomType(roomTypeService.getRoomType(dto.getRoomTypeId()));
//                     if (dto.getServiceItemId() != null)
//                         existing.setServiceItem(serviceItemRepository.findByIdAndDeletedFalse(dto.getServiceItemId()).orElse(null));
//                     if (dto.getMenuItemId() != null)
//                         existing.setMenuItem(menuItemService.findMenuItem(dto.getMenuItemId()));
//                     if (dto.getInventoryItemId() != null)
//                         existing.setInventoryItem(inventoryItemService.findInventoryItem(dto.getInventoryItemId()));

//                     return toDTO(repository.save(existing));
//                 })
//                 .orElseThrow(() -> new RuntimeException("PackageItem not found with id " + id));
//     }

//     public void deleteById(UUID id) {
//         repository.softDeleteById(id, java.time.Instant.now());
//     }

//     // Simple DTO mapping
//     private PackageItemDTO toDTO(PackageItem entity) {
//         PackageItemDTO dto = new PackageItemDTO();
//         dto.setId(entity.getId());
//         dto.setPkgId(entity.getPkg() != null ? entity.getPkg().getId() : null);
//         dto.setRoomTypeId(entity.getRoomType() != null ? entity.getRoomType().getId() : null);
//         dto.setServiceItemId(entity.getServiceItem() != null ? entity.getServiceItem().getId() : null);
//         dto.setMenuItemId(entity.getMenuItem() != null ? entity.getMenuItem().getId() : null);
//         dto.setInventoryItemId(entity.getInventoryItem() != null ? entity.getInventoryItem().getId() : null);
//         dto.setQty(entity.getQty());
//         dto.setPrice(entity.getPrice());
//         return dto;
//     }
// }

@Service
@RequiredArgsConstructor
@Transactional
public class PackageItemService {

    private final PackageRepository packageRepository;
    private final PackageItemRepository packageItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final RoomTypeService roomTypeService;
    private final MenuItemService menuItemService;
    private final ServiceItemRepository serviceItemRepository;

    public PackageItemDTO addPackageItem(PackageItemDTO dto) {

        Package pkg = packageRepository.findById(dto.getPkgId())
                .orElseThrow(() -> new ApplicationException("Package not found"));

        validateSingleItemType(dto);

        PackageItem item = new PackageItem();
        item.setPkg(pkg);
        item.setPrice(dto.getPrice());

        // Only ONE of these should be set
        item.setRoomTypeId(dto.getRoomTypeId());
        item.setServiceItemId(dto.getServiceItemId());
        item.setMenuItemId(dto.getMenuItemId());

        PackageItem savedItem = packageItemRepository.save(item);

        // Inventory linkage (optional)
        if (dto.getInventoryItemId() != null) {
            InventoryItem inventory = inventoryItemRepository.findById(dto.getInventoryItemId())
                    .orElseThrow(() -> new ApplicationException("Inventory item not found"));

            PackageInventoryItem pii = new PackageInventoryItem();
            pii.setPackageItem(savedItem);
            pii.setInventoryItem(inventory);
            pii.setQuantityRequired(dto.getQty());

            savedItem.getInventoryItems().add(pii);
        }

        return toDTO(savedItem);
    }

    @Transactional(readOnly = true)
    public List<PackageItemDTO> getItemsByPackage(UUID packageId) {

        return packageItemRepository.findByIdAndDeletedFalse(packageId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void validateSingleItemType(PackageItemDTO dto) {
        int count =
                (dto.getRoomTypeId() != null ? 1 : 0) +
                (dto.getServiceItemId() != null ? 1 : 0) +
                (dto.getMenuItemId() != null ? 1 : 0);

        if (count != 1) {
            throw new ApplicationException(
                    "Exactly one of roomTypeId, serviceItemId, or menuItemId must be provided"
            );
        }
    }

    private PackageItemDTO toDTO(PackageItem item) {

        PackageItemDTO.PackageItemDTOBuilder b = PackageItemDTO.builder()
                .id(item.getId())
                .pkgId(item.getPkg().getId())
                .pkgName(item.getPkg().getName())
                .price(item.getPrice())
                .qty(item.getQty());

        // Item type mapping
        if (item.getRoomTypeId() != null) {
            b.roomTypeId(item.getRoomTypeId())
             .roomTypeName(roomTypeService.getRoomType(item.getRoomTypeId()).getName());
        }
        if (item.getServiceItemId() != null) {
            b.serviceItemId(item.getServiceItemId())
             .serviceItemName(serviceItemRepository.findByIdAndDeletedFalse(item.getServiceItemId()).orElseThrow(() -> new ApplicationException("Service item not found")).getName());
        }
        if (item.getMenuItemId() != null) {
            b.menuItemId(item.getMenuItemId())
             .menuItemName(menuItemService.findMenuItem(item.getMenuItemId()).getName());
        }

        // Inventory mapping
        if (!item.getInventoryItems().isEmpty()) {
            PackageInventoryItem pii = item.getInventoryItems().get(0);
            b.inventoryItemId(pii.getInventoryItem().getId())
             .inventoryItemName(pii.getInventoryItem().getName())
             .qty(pii.getQuantityRequired());
        }

        return b.build();
    }

    public List<PackageItemDTO> getAllPackageItemDTOs(org.springframework.data.domain.Pageable pageable) {
        return packageItemRepository.findByDeletedFalse(pageable)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public PackageItemDTO getPackageItemDTOById(UUID id) {
        return packageItemRepository.findByIdAndDeletedFalse(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ApplicationException("Package item not found"));
    }

    public PackageItemDTO updatePackageItem(UUID id, PackageItemDTO dto) {

        return packageItemRepository.findByIdAndDeletedFalse(id)
                .map(existing -> {
                    if(dto.getQty() != null)
                        existing.setQty(dto.getQty());  
                    if (dto.getPrice() != null)
                        existing.setPrice(dto.getPrice());

                    // Update relationships if provided
                    if (dto.getPkgId() != null) {
                        existing.setPkg(packageRepository.findById(dto.getPkgId())
                                .orElseThrow(() -> new ApplicationException("Package not found")));
                    }

                    if (dto.getRoomTypeId() != null)
                        existing.setRoomTypeId(dto.getRoomTypeId());
                    if (dto.getServiceItemId() != null)
                        existing.setServiceItemId(dto.getServiceItemId());
                    if (dto.getMenuItemId() != null)
                        existing.setMenuItemId(dto.getMenuItemId());
                    // Inventory update logic can be added here if needed
                    if (dto.getInventoryItemId() != null) {
                        InventoryItem inventory = inventoryItemRepository.findById(dto.getInventoryItemId())
                                .orElseThrow(() -> new ApplicationException("Inventory item not found"));

                        if (existing.getInventoryItems().isEmpty()) {
                            PackageInventoryItem pii = new PackageInventoryItem();
                            pii.setPackageItem(existing);
                            pii.setInventoryItem(inventory);
                            pii.setQuantityRequired(dto.getQty());
                            existing.getInventoryItems().add(pii);
                        } else {
                            PackageInventoryItem pii = existing.getInventoryItems().get(0);
                            pii.setInventoryItem(inventory);
                            if (dto.getQty() != null)
                                pii.setQuantityRequired(dto.getQty());
                        }
                    }

                    return toDTO(packageItemRepository.save(existing));
                })
                .orElseThrow(() -> new ApplicationException("Package item not found with id " + id));
    }


    public void deleteById(UUID id) {
        packageItemRepository.softDeleteById(id, Instant.now());
    }
}