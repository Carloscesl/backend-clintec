package com.terreneitors.backendclintec.usuarios.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.persistence.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {
    public Usuario toDomain(UsuarioEntity entity) {
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
}
