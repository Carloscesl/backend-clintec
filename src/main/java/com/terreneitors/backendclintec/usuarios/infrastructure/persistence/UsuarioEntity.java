package com.terreneitors.backendclintec.usuarios.infrastructure.persistence;

import com.terreneitors.backendclintec.usuarios.domain.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long id;

    @Column(name = "nombreUser", nullable = false, length = 100)
    private String nombreUser;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

}
