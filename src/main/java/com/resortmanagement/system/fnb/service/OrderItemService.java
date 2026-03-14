package com.resortmanagement.system.fnb.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.resortmanagement.system.fnb.entity.OrderItem;
import com.resortmanagement.system.fnb.repository.OrderItemRepository;

@Service
public class OrderItemService {

    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetch all order items
     * (Order lifecycle is controlled by Order, not OrderItem)
     */
    public List<OrderItem> findAll() {
        return repository.findAll();
    }

    public Optional<OrderItem> findById(UUID id) {
        return repository.findById(id);
    }

    public OrderItem save(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    /**
     * Hard delete is acceptable here
     * (OrderItem is not a root aggregate)
     */
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public @Nullable Object findAllActive() {
        // Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllActive'");
    }

    public void delete(UUID id) {
        // Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}

