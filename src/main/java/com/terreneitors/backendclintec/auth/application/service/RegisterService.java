package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.application.port.in.RegisterUseCase;
import com.terreneitors.backendclintec.auth.domain.AuthUsuario;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Rol;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {


    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public TokenResponse register(String nombreUser, String email, String password) {

        usuarioRepository.findByEmail(email).ifPresent(u -> {
            throw new ValidationException("El correo ya está registrado: " + email);
        });

        Usuario usuario = new Usuario();
        usuario.setNombreUser(nombreUser);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(Rol.ADMINISTRADOR);
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());

        usuarioRepository.save(usuario);


        String jwtToken = jwtService.generateToken(usuario);
        log.info("[REGISTER_EXITOSO] email={} | rol={}", email, usuario.getRol());

        return new TokenResponse(
                usuario.getId(),
                usuario.getNombreUser(),
                usuario.getEmail(),
                jwtToken,
                List.of(usuario.getRol().name())
        );
    }
}
