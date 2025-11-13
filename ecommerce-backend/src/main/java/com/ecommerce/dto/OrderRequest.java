package com.ecommerce.dto;

import java.util.List;

public class OrderRequest {
    private Long customerId;
    private String address;
    private List<OrderItemDTO> items;

    // ✅ Getters & Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
