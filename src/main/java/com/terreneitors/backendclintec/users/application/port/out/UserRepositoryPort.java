package com.terreneitors.backendclintec.users.application.port.out;

import com.terreneitors.backendclintec.users.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User save(User user);
    List<User> findAll();
}
