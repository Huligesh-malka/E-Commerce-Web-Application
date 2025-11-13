package com.ecommerce.service;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderItemDTO;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;

    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository itemRepo,
                        ProductRepository productRepo,
                        CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }

    @Transactional
    public Order createOrder(OrderRequest req) {

        // 1️⃣ Get customer
        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2️⃣ Calculate total & build items
        double total = 0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemDTO dto : req.getItems()) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

            if (product.getStock() < dto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPrice(product.getPrice());
            items.add(orderItem);

            total += product.getPrice() * dto.getQuantity();

            // Reduce stock
            product.setStock(product.getStock() - dto.getQuantity());
            productRepo.save(product);
        }

        // 3️⃣ Create Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(req.getAddress());
        order.setTotalPrice(total);
        order.setStatus("Pending");
        order.setOrderDate(LocalDateTime.now());
        order.setItems(items);

        // 4️⃣ Link items back to order & save each item
        for (OrderItem item : items) {
            item.setOrder(order);       // set order reference
        }

        // 5️⃣ Save order (cascades items if cascade = ALL)
        Order savedOrder = orderRepo.save(order);

        // 6️⃣ Optional: explicitly save items (if cascade is not set)
        for (OrderItem item : items) {
            itemRepo.save(item);
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
