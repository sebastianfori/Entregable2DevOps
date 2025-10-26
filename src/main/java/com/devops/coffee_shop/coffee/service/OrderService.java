package com.devops.coffee_shop.coffee.service;

import com.devops.coffee_shop.coffee.domain.Order;
import com.devops.coffee_shop.coffee.domain.OrderStatus;
import com.devops.coffee_shop.coffee.dto.OrderDto;
import com.devops.coffee_shop.coffee.metrics.CoffeeMetrics;
import com.devops.coffee_shop.coffee.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CoffeeMetrics coffeeMetrics;

    public OrderDto createOrder(OrderDto dto) {
        Order order = new Order(dto.getCustomerName(), dto.getDrink(), dto.getQuantity());
        Order saved = orderRepository.save(order);
        coffeeMetrics.incrementOrdersCreated();
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    public OrderDto updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));

        order.setStatus(status);
        if (status == OrderStatus.DELIVERED) {
            coffeeMetrics.incrementOrdersDelivered();
        }

        Order updated = orderRepository.save(order);
        return convertToDto(updated);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setDrink(order.getDrink());
        dto.setQuantity(order.getQuantity());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
