package com.devops.coffee_shop.coffee.repository;

import com.devops.coffee_shop.coffee.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos con clientes
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Busca clientes disponibles
     */
    List<Client> findByActiveTrue();
    
    /**
     * Busca clientes por nombre (búsqueda parcial, case insensitive)
     */
    @Query("SELECT p FROM Client p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Client> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    /**
     * Busca clientes por apellido (búsqueda parcial, case insensitive)
     */
    @Query("SELECT p FROM Client p WHERE LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Client> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);
    

    /**
     * Verifica si existe un cliente con el mismo nombre
     */
    boolean existsByFirstNameIgnoreCase(String firstName);

    /**
     * Busca un cliente por nombre exacto (case insensitive)
     */
    Optional<Client> findByFirstNameIgnoreCase(String firstName);
    
    /**
     * Cuenta clientes disponibles
     */
    long countByActiveTrue();
}
