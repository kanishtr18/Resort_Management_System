package com.resortmanagement.system.marketing.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.common.exception.ApplicationException;
import com.resortmanagement.system.marketing.dto.packagedto.PackageRequest;
import com.resortmanagement.system.marketing.dto.packagedto.PackageResponse;
import com.resortmanagement.system.marketing.entity.Package;
import com.resortmanagement.system.marketing.repository.PackageRepository;

import lombok.RequiredArgsConstructor;

// @Service
// @Transactional
// public class PackageService {

//     private final PackageRepository repository;
//     private final PackageMapper mapper;

//     public PackageService(PackageRepository packageRepository, PackageMapper packageMapper) {
//         this.repository = packageRepository;
//         this.mapper = packageMapper;
//     }

//     @Transactional(readOnly = true)
//     public Page<PackageResponse> findAll(Pageable pageable) {
//         return repository.findByDeletedFalse(pageable).map(mapper::toResponse);
//     }

//     @Transactional(readOnly = true)
//     public Optional<PackageResponse> findById(UUID id) {
//         return repository.findByIdAndDeletedFalse(id).map(mapper::toResponse);
//     }

//     public PackageResponse save(PackageRequest dto) {
//         if (dto.getName() == null || dto.getName().isEmpty()) {
//             throw new IllegalArgumentException("Package name is required");
//         }
//         if (dto.getPrice() == null) {
//             throw new IllegalArgumentException("Package price is required");
//         }

//         Package pkg = mapper.toEntity(dto);
//         Package saved = repository.save(pkg);
//         return mapper.toResponse(saved);
//     }

//     public PackageResponse update(UUID id, PackageRequest dto) {
//         return repository.findByIdAndDeletedFalse(id)
//                 .map(existing -> {
//                     mapper.updateEntity(existing, dto);
//                     return mapper.toResponse(repository.save(existing));
//                 })
//                 .orElseThrow(() -> new RuntimeException("Package not found with id " + id));
//     }

//     public void deleteById(UUID id) {
//         if (!repository.existsById(id)) {
//             throw new RuntimeException("Package not found with id " + id);
//         }
//         repository.softDeleteById(id, Instant.now());
//     }
// }

@Service
@RequiredArgsConstructor
@Transactional
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageResponse createPackage(PackageRequest request) {

        if (request.getValidFrom().isAfter(request.getValidTo())) {
            throw new ApplicationException("validFrom cannot be after validTo");
        }

        Package pkg = Package.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .usageLimit(request.getUsageLimit())
                .build();

        Package saved = packageRepository.save(pkg);
        return toResponse(saved);
    }

    public PackageResponse updatePackage(UUID packageId, PackageRequest request) {

        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ApplicationException("Package not found"));

        pkg.setName(request.getName());
        pkg.setDescription(request.getDescription());
        pkg.setPrice(request.getPrice());
        pkg.setValidFrom(request.getValidFrom());
        pkg.setValidTo(request.getValidTo());
        pkg.setUsageLimit(request.getUsageLimit());

        return toResponse(pkg);
    }

    @Transactional(readOnly = true)
    public PackageResponse getPackage(UUID id) {
        return toResponse(
                packageRepository.findById(id)
                        .orElseThrow(() -> new ApplicationException("Package not found"))
        );
    }

    private PackageResponse toResponse(Package pkg) {
        return PackageResponse.builder()
                .id(pkg.getId())
                .name(pkg.getName())
                .description(pkg.getDescription())
                .price(pkg.getPrice())
                .validFrom(pkg.getValidFrom())
                .validTo(pkg.getValidTo())
                .usageLimit(pkg.getUsageLimit())
                .createdAt(pkg.getCreatedAt())
                .updatedAt(pkg.getUpdatedAt())
                .build();
    }

    public List<PackageResponse> getAllPackages() {
        return packageRepository.findByDeletedFalse().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PackageResponse findPackageById(UUID id) {
        return packageRepository.findByIdAndDeletedFalse(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApplicationException("Package not found"));
    }

    public void deleteById(UUID id) {
        if (!packageRepository.existsById(id)) {
            throw new ApplicationException("Package not found");
        }
        packageRepository.softDeleteById(id, Instant.now());
    }
}