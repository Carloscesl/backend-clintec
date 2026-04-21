package com.terreneitors.backendclintec.users.infrastructure.persistence;

import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringUserRepository springUserRepository;
    private final UserPersistenceMapper mapper;

    public UserRepositoryAdapter(SpringUserRepository springUserRepository, UserPersistenceMapper mapper) {
        this.springUserRepository = springUserRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    public Optional<User> findById(Long id) {
        return springUserRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = springUserRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<User> findAll() {
        return springUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}
