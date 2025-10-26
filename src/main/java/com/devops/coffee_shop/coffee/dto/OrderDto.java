package com.devops.coffee_shop.coffee.dto;

import com.devops.coffee_shop.coffee.domain.OrderStatus;
import java.time.LocalDateTime;

public class OrderDto {

    private Long id;
    private String customerName;
    private String drink;
    private int quantity;
    private OrderStatus status;
    private LocalDateTime createdAt;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDrink() { return drink; }
    public void setDrink(String drink) { this.drink = drink; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
