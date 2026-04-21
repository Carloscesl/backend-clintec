package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.application.port.in.RegisterUseCase;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
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


    private final UserRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public TokenResponse register(String nombreUser, String email, String password) {

        usuarioRepository.findByEmail(email).ifPresent(u -> {
            throw new ValidationException("El correo ya está registrado: " + email);
        });

        User user = new User();
        user.setNombreUser(nombreUser);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRol(Rol.ADMINISTRADOR);
        user.setActivo(true);
        user.setFechaCreacion(LocalDateTime.now());

        usuarioRepository.save(user);


        String jwtToken = jwtService.generateToken(user);
        log.info("[REGISTER_EXITOSO] email={} | rol={}", email, user.getRol());

        return new TokenResponse(
                user.getId(),
                user.getNombreUser(),
                user.getEmail(),
                jwtToken,
                List.of(user.getRol().name())
        );
    }
}
