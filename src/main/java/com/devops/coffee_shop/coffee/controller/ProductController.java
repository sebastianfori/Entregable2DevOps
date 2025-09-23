package com.devops.coffee_shop.coffee.controller;

import com.devops.coffee_shop.coffee.domain.ProductCategory;
import com.devops.coffee_shop.coffee.dto.ProductDto;
import com.devops.coffee_shop.coffee.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operaciones con productos
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestión de productos de la cafetería")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * Obtiene todos los productos
     */
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Obtiene un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene productos por categoría
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable ProductCategory category) {
        List<ProductDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Obtiene productos disponibles
     */
    @GetMapping("/available")
    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
        List<ProductDto> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Busca productos por nombre
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String name) {
        List<ProductDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Busca productos por rango de precio
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Crea un nuevo producto
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        try {
            ProductDto createdProduct = productService.createProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualiza un producto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Elimina un producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Cambia la disponibilidad de un producto
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<ProductDto> toggleProductAvailability(@PathVariable Long id) {
        try {
            ProductDto updatedProduct = productService.toggleProductAvailability(id);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Endpoint de salud para verificar que el controlador funciona
     */
    @Operation(summary = "Health check", description = "Verifica que el controlador de productos está funcionando")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Product Controller is running!");
    }
}
