package com.resortmanagement.system.billing.mapper;

import java.util.stream.Collectors;

import com.resortmanagement.system.billing.dto.AccountLedgerRequest;
import com.resortmanagement.system.billing.dto.AccountLedgerResponse;
import com.resortmanagement.system.billing.dto.FolioRequest;
import com.resortmanagement.system.billing.dto.FolioResponse;
import com.resortmanagement.system.billing.dto.InvoiceRequest;
import com.resortmanagement.system.billing.dto.InvoiceResponse;
import com.resortmanagement.system.billing.dto.PaymentRequest;
import com.resortmanagement.system.billing.dto.PaymentResponse;
import com.resortmanagement.system.billing.dto.RefundRequest;
import com.resortmanagement.system.billing.dto.RefundResponse;
import com.resortmanagement.system.billing.entity.AccountLedger;
import com.resortmanagement.system.billing.entity.Folio;
import com.resortmanagement.system.billing.entity.Invoice;
import com.resortmanagement.system.billing.entity.Payment;
import com.resortmanagement.system.billing.entity.Refund;

public class BillingMapper {

    public static Invoice toEntity(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setDueDate(request.getDueDate());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setTaxAmount(request.getTaxAmount());
        invoice.setCurrency(request.getCurrency());
        if(request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        return invoice;
    }

    public static InvoiceResponse toResponse(Invoice entity) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(entity.getId());
        response.setFolioId(entity.getFolio() != null ? entity.getFolio().getId() : null);
        response.setReservationId(entity.getReservation() != null ? entity.getReservation().getId() : null);
        response.setIssueDate(entity.getIssueDate());
        response.setDueDate(entity.getDueDate());
        response.setTotalAmount(entity.getTotalAmount());
        response.setTaxAmount(entity.getTaxAmount());
        response.setStatus(entity.getStatus());
        response.setCurrency(entity.getCurrency());
        if (entity.getPayments() != null) {
            response.setPayments(entity.getPayments().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
        }
        return response;
    }

    public static Payment toEntity(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(request.getTransactionRef());
        payment.setProviderResponse(request.getProviderResponse());
        return payment;
    }

    public static PaymentResponse toResponse(Payment entity) {
        PaymentResponse response = new PaymentResponse();
        response.setId(entity.getId());
        response.setInvoiceId(entity.getInvoice() != null ? entity.getInvoice().getId() : null);
        response.setAmount(entity.getAmount());
        response.setPaymentMethod(entity.getPaymentMethod());
        response.setTransactionRef(entity.getTransactionRef());
        response.setStatus(entity.getStatus());
        response.setProviderResponse(entity.getProviderResponse());
        response.setProcessedAt(entity.getProcessedAt());
        return response;
    }

    public static Refund toEntity(RefundRequest request) {
        Refund refund = new Refund();
        refund.setAmount(request.getAmount());
        refund.setReason(request.getReason());
        return refund;
    }

    public static RefundResponse toResponse(Refund entity) {
        RefundResponse response = new RefundResponse();
        response.setId(entity.getId());
        response.setPaymentId(entity.getPayment() != null ? entity.getPayment().getId() : null);
        response.setAmount(entity.getAmount());
        response.setReason(entity.getReason());
        response.setProcessedBy(entity.getProcessedBy());
        response.setProcessedAt(entity.getProcessedAt());
        response.setStatus(entity.getStatus());
        response.setProviderRefundRef(entity.getProviderRefundRef());
        return response;
    }
    
    public static Folio toEntity(FolioRequest request) {
        Folio folio = new Folio();
        folio.setName(request.getName());
        if (request.getStatus() != null) {
            folio.setStatus(request.getStatus());
        }
        return folio;
    }

    public static FolioResponse toResponse(Folio entity) {
        FolioResponse response = new FolioResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setStatus(entity.getStatus());
        response.setTotalAmount(entity.getTotalAmount());
        if (entity.getInvoices() != null) {
             response.setInvoices(entity.getInvoices().stream()
                .map(BillingMapper::toResponse)
                .collect(Collectors.toList()));
        }
        return response;
    }

    public static AccountLedger toEntity(AccountLedgerRequest request) {
        AccountLedger ledger = new AccountLedger();
        ledger.setAccountCode(request.getAccountCode());
        ledger.setName(request.getName());
        ledger.setAccountType(request.getAccountType());
        if (request.getBalance() != null) ledger.setBalance(request.getBalance());
        ledger.setCurrency(request.getCurrency());
        return ledger;
    }

    public static AccountLedgerResponse toResponse(AccountLedger entity) {
        AccountLedgerResponse response = new AccountLedgerResponse();
        response.setId(entity.getId());
        response.setAccountCode(entity.getAccountCode());
        response.setName(entity.getName());
        response.setAccountType(entity.getAccountType());
        response.setBalance(entity.getBalance());
        response.setCurrency(entity.getCurrency());
        return response;
    }
}
