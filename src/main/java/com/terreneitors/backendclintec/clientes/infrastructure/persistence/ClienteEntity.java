package com.terreneitors.backendclintec.clientes.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClientes")
    private Long id;

    @Column(name= "nombreCliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name= "empresaCliente", nullable = false, length = 100)
    private String empresa;

    @Column(name= "emailCliente", nullable = false, length = 100)
    private String email;

    @Column(name= "telefonoCliente", nullable = false, length = 100)
    private String telefono;

    @Column(name= "direccionCliente", nullable = false, length = 100)
    private String direccion;

    @Column(name= "fechaResgitro")
    private LocalDateTime fechaRegistro;

}
