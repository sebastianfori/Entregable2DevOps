package com.devops.coffee_shop.coffee.controller;

import com.devops.coffee_shop.coffee.dto.ClientDto;
import com.devops.coffee_shop.coffee.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operaciones con clientes
 */
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gestión de clientes de la cafetería")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Obtiene todos los clientes
     */
    @Operation(summary = "Obtener todos los clientes", description = "Retorna una lista de todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    /**
     * Obtiene un cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        Optional<ClientDto> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene clientes activos
     */
    @GetMapping("/active")
    public ResponseEntity<List<ClientDto>> getActiveClients() {
        List<ClientDto> clients = clientService.getActiveClients();
        return ResponseEntity.ok(clients);
    }

    /**
     * Busca clientes por nombre
     */
    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<List<ClientDto>> searchClientsByFirstName(@RequestParam String firstName) {
        List<ClientDto> clients = clientService.searchClientsByFirstName(firstName);
        return ResponseEntity.ok(clients);
    }

    /**
     * Busca clientes por apellido
     */
    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<ClientDto>> searchClientsByLastName(@RequestParam String lastName) {
        List<ClientDto> clients = clientService.searchClientsByLastName(lastName);
        return ResponseEntity.ok(clients);
    }

    /**
     * Crea un nuevo cliente
     */
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        try {
            ClientDto createdClient = clientService.createClient(clientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un cliente existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        try {
            ClientDto updatedClient = clientService.updateClient(id, clientDto);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cambia la disponibilidad de un cliente
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<ClientDto> toggleClientAvailability(@PathVariable Long id) {
        try {
            ClientDto updatedClient = clientService.toggleClientAvailability(id);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador funciona
     */
    @Operation(summary = "Health check", description = "Verifica que el controlador de clientes está funcionando")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Client Controller is running!");
    }
}
