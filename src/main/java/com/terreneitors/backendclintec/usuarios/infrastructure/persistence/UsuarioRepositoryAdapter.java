package com.terreneitors.backendclintec.usuarios.infrastructure.persistence;

import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final SpringUsuarioRepository springUsuarioRepository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(SpringUsuarioRepository springUsuarioRepository, UsuarioPersistenceMapper mapper) {
        this.springUsuarioRepository = springUsuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return springUsuarioRepository.findByEmail(email).map(mapper::toDomain);
    }

    public Optional<Usuario> findById(Long id) {
        return springUsuarioRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = springUsuarioRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Usuario> findAll() {
        return springUsuarioRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}
