package com.terreneitors.backendclintec.clients.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name= "nombre_Cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name= "empresa_Cliente", nullable = false, length = 100)
    private String empresa;

    @Column(name= "email_Cliente", nullable = false, length = 100)
    private String email;

    @Column(name= "telefono_Cliente", nullable = false, length = 100)
    private String telefono;

    @Column(name= "direccion_Cliente", nullable = false, length = 100)
    private String direccion;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }

}
