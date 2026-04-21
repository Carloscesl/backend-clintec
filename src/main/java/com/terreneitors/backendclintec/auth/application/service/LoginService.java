package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.application.port.in.LoginUseCase;
import com.terreneitors.backendclintec.auth.domain.AuthUsuario;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(String email, String password) {

        log.info("[LOGIN_INTENTO] email={}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        if (!usuario.getActivo()) {
            log.warn("[LOGIN_USUARIO_INACTIVO] email={}", email);
            throw new InvalidStateException(
                    "La cuenta está desactivada. Contacta al administrador.");
        }


        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            log.warn("[LOGIN_CREDENCIALES_INVALIDAS] email={}", email);
            throw new ValidationException("Email o contraseña incorrectos.");
        }


        String jwtToken = jwtService.generateToken(usuario);
        log.info("[LOGIN_EXITOSO] email={} | rol={}", email, usuario.getRol());

        return new TokenResponse(
                usuario.getId(),
                usuario.getNombreUser(),
                usuario.getEmail(),
                jwtToken,
                List.of(usuario.getRol().name())
        );
    }
}
