package com.terreneitors.backendclintec.users.infrastructure.persistence;

import com.terreneitors.backendclintec.users.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Long countByRol(Rol rol);
    List<UserEntity> findByRol(Rol rol);
}
