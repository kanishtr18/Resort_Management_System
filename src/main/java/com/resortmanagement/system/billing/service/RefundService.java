package com.resortmanagement.system.billing.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resortmanagement.system.billing.entity.Payment;
import com.resortmanagement.system.billing.entity.Refund;
import com.resortmanagement.system.billing.entity.RefundStatus;
import com.resortmanagement.system.billing.repository.PaymentRepository;
import com.resortmanagement.system.billing.repository.RefundRepository;
import com.resortmanagement.system.common.exception.ApplicationException;

/**
 * RefundService
 * Purpose:
 *  - Service layer for Refund entity operations
 *  - Handles refund processing with validation
 * Business Logic:
 *  - processRefund: Transitions refund through processing states
 *  - Validates refund before processing
 *  - Ensures idempotency and audit trail
 */
@Service
@Transactional
public class RefundService {

    private final RefundRepository repository;
    private final PaymentRepository paymentRepository;
    
    public RefundService(RefundRepository repository, PaymentRepository paymentRepository) {
        this.repository = repository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<Refund> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Refund> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Refund> findByPaymentId(UUID paymentId) {
        return repository.findByPaymentId(paymentId);
    }

    @Transactional(readOnly = true)
    public List<Refund> findByStatus(RefundStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    // Fix — rename to getPayment and fix error message
    public Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ApplicationException("Payment not found with id: " + paymentId));
    }

    public Refund save(Refund refund) {
        // Validation: ensure required fields are present
        if (refund.getPayment() == null) {
            throw new ApplicationException("Payment ID is required for refund");
        }
        if (refund.getAmount() == null || refund.getAmount().signum() <= 0) {
            throw new ApplicationException("Refund amount must be greater than zero");
        }
        if (refund.getReason() == null || refund.getReason().trim().isEmpty()) {
            throw new ApplicationException("Refund reason is required");
        }
        
        return repository.save(refund);
    }

    public Refund processRefund(UUID refundId, boolean success, String providerRefundRef) {
        Refund refund = repository.findById(refundId)
                .orElseThrow(() -> new ApplicationException("Refund not found with id: " + refundId));
        
        if (refund.getStatus() == RefundStatus.SUCCESS || refund.getStatus() == RefundStatus.FAILED) {
            throw new ApplicationException("Refund has already been processed");
        }
        
        // Transition to PROCESSING if currently REQUESTED
        if (refund.getStatus() == RefundStatus.REQUESTED) {
            refund.setStatus(RefundStatus.PROCESSING);
            repository.save(refund);
        }
        
        // Update final status
        refund.setStatus(success ? RefundStatus.SUCCESS : RefundStatus.FAILED);
        refund.setProcessedAt(Instant.now());
        refund.setProviderRefundRef(providerRefundRef);
        
        return repository.save(refund);
    }
}
