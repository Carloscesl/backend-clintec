package com.terreneitors.backendclintec.users.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserResponseDTO;
import com.terreneitors.backendclintec.users.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        User user = new User();
        user.setId(entity.getId());
        user.setNombreUser(entity.getNombreUser());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setRol(entity.getRol());
        user.setActivo(entity.getActivo());
        user.setFechaCreacion(entity.getFechaCreacion());
        return user;
    }

    public UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setNombreUser(user.getNombreUser());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRol(user.getRol());
        entity.setActivo(user.getActivo());
        entity.setFechaCreacion(user.getFechaCreacion());
        return entity;
    }

    public UserResponseDTO toDTO(User u) {
        if (u == null) return null;

        return new UserResponseDTO(
                u.getId(),
                u.getNombreUser(),
                u.getEmail(),
                u.getRol(),
                u.getActivo()
        );
    }
}
