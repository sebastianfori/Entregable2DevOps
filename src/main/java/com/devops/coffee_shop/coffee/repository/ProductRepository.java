package com.devops.coffee_shop.coffee.repository;

import com.devops.coffee_shop.coffee.domain.Product;
import com.devops.coffee_shop.coffee.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos con productos
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Busca productos por categoría
     */
    List<Product> findByCategory(ProductCategory category);
    
    /**
     * Busca productos disponibles
     */
    List<Product> findByAvailableTrue();
    
    /**
     * Busca productos por categoría y disponibilidad
     */
    List<Product> findByCategoryAndAvailableTrue(ProductCategory category);
    
    /**
     * Busca productos por nombre (búsqueda parcial, case insensitive)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca productos por rango de precio
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetween(@Param("minPrice") java.math.BigDecimal minPrice, 
                                   @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    /**
     * Verifica si existe un producto con el mismo nombre
     */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Busca un producto por nombre exacto (case insensitive)
     */
    Optional<Product> findByNameIgnoreCase(String name);
    
    /**
     * Cuenta productos por categoría
     */
    long countByCategory(ProductCategory category);
    
    /**
     * Cuenta productos disponibles
     */
    long countByAvailableTrue();
}
