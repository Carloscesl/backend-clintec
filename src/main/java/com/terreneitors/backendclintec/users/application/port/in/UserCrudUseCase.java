package com.terreneitors.backendclintec.users.application.port.in;

import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserRequestDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UserCrudUseCase {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    User createUser(UserRequestDTO usuario);

    User updateUser(Long id, UserUpdateDTO usuario);

    void desactivateUser(String email);
    void activateUser(String email);

}
