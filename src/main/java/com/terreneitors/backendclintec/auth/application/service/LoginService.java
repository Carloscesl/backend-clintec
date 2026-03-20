package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.application.port.in.LoginUseCase;
import com.terreneitors.backendclintec.auth.domain.AuthUsuario;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsuarioRepositoryPort usuarioRepository,
                        JwtService jwtService,
                        PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String email, String password) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtService.generateToken(usuario);
    }
}
