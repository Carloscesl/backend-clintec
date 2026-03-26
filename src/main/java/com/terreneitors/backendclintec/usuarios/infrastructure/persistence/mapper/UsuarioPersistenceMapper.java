package com.terreneitors.backendclintec.usuarios.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioResponseDTO;
import com.terreneitors.backendclintec.usuarios.infrastructure.persistence.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {
    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setNombreUser(entity.getNombreUser());
        usuario.setEmail(entity.getEmail());
        usuario.setPassword(entity.getPassword());
        usuario.setRol(entity.getRol());
        usuario.setActivo(entity.isActivo());
        usuario.setFechaCreacion(entity.getFechaCreacion());
        return usuario;
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombreUser(usuario.getNombreUser());
        entity.setEmail(usuario.getEmail());
        entity.setPassword(usuario.getPassword());
        entity.setRol(usuario.getRol());
        entity.setActivo(usuario.getActivo());
        entity.setFechaCreacion(usuario.getFechaCreacion());
        return entity;
    }

    public UsuarioResponseDTO toDTO(Usuario u) {
        if (u == null) return null;

        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombreUser(),
                u.getEmail(),
                u.getRol(),
                u.getActivo()
        );
    }
}
