package com.devops.coffee_shop.coffee.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * DTO para transferir datos de clientes
 */
public class ClientDto {

    @Getter
    @Setter
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Getter
    @Setter
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Getter
    @Setter
    private String lastName;

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 8, max = 10, message = "El documento debe tener entre 8 y 10 caracteres")
    @Getter
    @Setter
    private String documentNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Getter
    @Setter
    private Date birthDate;

    @Getter
    @Setter
    private boolean active = true;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private LocalDateTime updatedAt;

    // Constructores
    public ClientDto() {}

    public ClientDto(String firstName, String lastName, Date birthDate, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.active = active;
    }

    public ClientDto(String firstName, String lastName, Date birthDate, String documentNumber, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.documentNumber = documentNumber;
        this.active = active;
    }
}
