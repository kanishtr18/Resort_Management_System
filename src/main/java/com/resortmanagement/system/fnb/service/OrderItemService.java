// package com.resortmanagement.system.fnb.service;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import org.jspecify.annotations.Nullable;
// import org.springframework.stereotype.Service;

// import com.resortmanagement.system.fnb.entity.OrderItem;
// import com.resortmanagement.system.fnb.repository.OrderItemRepository;

// @Service
// public class OrderItemService {

//     private final OrderItemRepository repository;

//     public OrderItemService(OrderItemRepository repository) {
//         this.repository = repository;
//     }

//     /**
//      * Fetch all order items
//      * (Order lifecycle is controlled by Order, not OrderItem)
//      */
//     public List<OrderItem> findAll() {
//         return repository.findAll();
//     }

//     public Optional<OrderItem> findById(UUID id) {
//         return repository.findById(id);
//     }

//     public OrderItem save(OrderItem orderItem) {
//         return repository.save(orderItem);
//     }

//     /**
//      * Hard delete is acceptable here
//      * (OrderItem is not a root aggregate)
//      */
//     public void deleteById(UUID id) {
//         repository.deleteById(id);
//     }

//     public @Nullable Object findAllActive() {
//         // Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'findAllActive'");
//     }

//     public void delete(UUID id) {
//         // Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'delete'");
//     }
// }

package com.resortmanagement.system.fnb.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.fnb.dto.response.OrderItemResponse;
import com.resortmanagement.system.fnb.entity.OrderItem;
import com.resortmanagement.system.fnb.mapper.OrderItemMapper;
import com.resortmanagement.system.fnb.repository.OrderItemRepository;

@Service
public class OrderItemService {

    private final OrderItemRepository repository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemService(OrderItemRepository repository, OrderItemMapper orderItemMapper) {
        this.repository = repository;
        this.orderItemMapper = orderItemMapper;
    }

    public List<OrderItemResponse> findAllActive() {
        // Fix: was throwing UnsupportedOperationException
        return repository.findAll().stream()
                .map(orderItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrderItemResponse> findById(UUID id) {
        return repository.findById(id).map(orderItemMapper::toResponse);
    }

    public OrderItem save(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    public void delete(UUID id) {
        // Fix: was throwing UnsupportedOperationException
        // Also fixes name mismatch (controller calls delete(), not deleteById())
        repository.deleteById(id);
    }
}