package com.devops.coffee_shop.coffee.service;

import com.devops.coffee_shop.coffee.domain.Client;
import com.devops.coffee_shop.coffee.dto.ClientDto;
import com.devops.coffee_shop.coffee.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la lógica de negocio de clientes
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Obtiene todos los clientes
     */
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un client por ID
     */
    @Transactional(readOnly = true)
    public Optional<ClientDto> getClientById(Long id) {
        return clientRepository.findById(id)
                .map(this::convertToDto);
    }

    /**
     * Obtiene clientes activos
     */
    @Transactional(readOnly = true)
    public List<ClientDto> getActiveClients() {
        return clientRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes por nombre
     */
    @Transactional(readOnly = true)
    public List<ClientDto> searchClientsByFirstName(String firstName) {
        return clientRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes por apellido
     */
    @Transactional(readOnly = true)
    public List<ClientDto> searchClientsByLastName(String lastName) {
        return clientRepository.findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo cliente
     */
    public ClientDto createClient(ClientDto clientDto) {
        // Validar que no exista un cliente con el mismo nombre
        if (clientRepository.existsByFirstNameIgnoreCase(clientDto.getFirstName())) {
            throw new IllegalArgumentException("Ya existe un cliente con el nombre: " + clientDto.getFirstName());
        }

        Client client = convertToEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return convertToDto(savedClient);
    }

    /**
     * Actualiza un cliente existente
     */
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Validar que el nuevo nombre no esté en uso por otro cliente
        if (!existingClient.getFirstName().equalsIgnoreCase(clientDto.getFirstName()) &&
                clientRepository.existsByFirstNameIgnoreCase(clientDto.getFirstName())) {
            throw new IllegalArgumentException("Ya existe un cliente con el nombre: " + clientDto.getFirstName());
        }

        // Actualizar campos
        existingClient.setFirstName(clientDto.getFirstName());
        existingClient.setLastName(clientDto.getLastName());
        existingClient.setBirthDate(clientDto.getBirthDate());
        existingClient.setActive(clientDto.isActive());

        Client updatedClient = clientRepository.save(existingClient);
        return convertToDto(updatedClient);
    }

    /**
     * Elimina un cliente
     */
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
        clientRepository.deleteById(id);
    }

    /**
     * Cambia la disponibilidad de un cliente
     */
    public ClientDto toggleClientAvailability(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        client.setActive(!client.isActive());
        Client updatedClient = clientRepository.save(client);
        return convertToDto(updatedClient);
    }

    /**
     * Convierte entidad a DTO
     */
    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setBirthDate(client.getBirthDate());
        dto.setActive(client.isActive());
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());
        return dto;
    }

    /**
     * Convierte DTO a entidad
     */
    private Client convertToEntity(ClientDto dto) {
        return new Client(dto.getFirstName(), dto.getLastName(), dto.getBirthDate());
    }
}
