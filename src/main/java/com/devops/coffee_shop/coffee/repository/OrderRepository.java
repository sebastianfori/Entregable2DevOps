package com.devops.coffee_shop.coffee.repository;

import com.devops.coffee_shop.coffee.domain.Order;
import com.devops.coffee_shop.coffee.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operaciones de base de datos con pedidos (orders)
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca pedidos por estado
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Busca pedidos por nombre del cliente (insensible a mayúsculas/minúsculas)
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.customerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Order> findByCustomerNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Cuenta pedidos entregados
     */
    long countByStatus(OrderStatus status);

    /**
     * Verifica si hay pedidos pendientes
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.status <> 'DELIVERED' AND o.status <> 'CANCELED'")
    boolean hasPendingOrders();
}
