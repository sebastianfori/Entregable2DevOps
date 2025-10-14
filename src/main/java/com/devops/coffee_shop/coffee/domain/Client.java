package com.devops.coffee_shop.coffee.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un cliente de la cafetería
 */
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String firstName;

    @Column(nullable = false)
    @Getter
    @Setter
    private String lastName;

    @Column(nullable = false, unique = true, length = 64)
    @Getter
    @Setter
    private String documentNumber;

    @Column(nullable = false)
    @Getter
    @Setter
    private Date birthDate;

    @Column(name = "isActive")
    @Getter
    @Setter
    private boolean active = true;

    @Column(name = "created_at")
    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Getter
    @Setter
    private LocalDateTime updatedAt;

    // Constructores
    public Client() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Client(String firstName, String lastName, Date birthDate) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Client(String firstName, String lastName, Date birthDate, String documentNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.documentNumber = documentNumber;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
