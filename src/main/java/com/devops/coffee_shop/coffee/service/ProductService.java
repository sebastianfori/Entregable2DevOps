package com.devops.coffee_shop.coffee.service;

import com.devops.coffee_shop.coffee.domain.Product;
import com.devops.coffee_shop.coffee.domain.ProductCategory;
import com.devops.coffee_shop.coffee.dto.ProductDto;
import com.devops.coffee_shop.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la lógica de negocio de productos
 */
@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Obtiene todos los productos
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un producto por ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Obtiene productos por categoría
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene productos disponibles
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getAvailableProducts() {
        return productRepository.findByAvailableTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca productos por nombre
     */
    @Transactional(readOnly = true)
    public List<ProductDto> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca productos por rango de precio
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Crea un nuevo producto
     */
    public ProductDto createProduct(ProductDto productDto) {
        // Validar que no exista un producto con el mismo nombre
        if (productRepository.existsByNameIgnoreCase(productDto.getName())) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + productDto.getName());
        }
        
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }
    
    /**
     * Actualiza un producto existente
     */
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        
        // Validar que el nuevo nombre no esté en uso por otro producto
        if (!existingProduct.getName().equalsIgnoreCase(productDto.getName()) &&
            productRepository.existsByNameIgnoreCase(productDto.getName())) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + productDto.getName());
        }
        
        // Actualizar campos
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setAvailable(productDto.isAvailable());
        
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }
    
    /**
     * Elimina un producto
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }
    
    /**
     * Cambia la disponibilidad de un producto
     */
    public ProductDto toggleProductAvailability(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        
        product.setAvailable(!product.isAvailable());
        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }
    
    /**
     * Convierte entidad a DTO
     */
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setAvailable(product.isAvailable());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
    
    /**
     * Convierte DTO a entidad
     */
    private Product convertToEntity(ProductDto dto) {
        return new Product(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getCategory());
    }
}
