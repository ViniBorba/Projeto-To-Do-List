package com.example.demo.auth;

//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.*;
import com.example.demo.user.*;

// com.example.demo.auth.AuthService
@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtService jwt){
        this.repo = repo; this.encoder = encoder; this.jwt = jwt;
    }

    public void register(RegisterRequest req){
        final String username = safe(req.username());
        final String raw = safe(req.password());

        if (username.isEmpty() || raw.isEmpty())
            throw new IllegalArgumentException("Usuário e senha são obrigatórios");

        if (repo.existsByUsername(username))
            throw new IllegalArgumentException("Usuário já existe");

        var user = User.builder()
            .username(username)                // username normalizado
            .password(encoder.encode(raw))
            .role(Role.USER)
            .build();

        repo.save(user);
    }

    public AuthResponse login(LoginRequest req){
        final String username = safe(req.username());
        final String raw = safe(req.password());

        if (username.isEmpty() || raw.isEmpty())
            throw new IllegalArgumentException("Credenciais inválidas");

        var user = repo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if(!encoder.matches(raw, user.getPassword()))
            throw new IllegalArgumentException("Credenciais inválidas");

        return new AuthResponse(jwt.generateToken(user.getUsername()));
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();   // remove espaços acidentais
    }
}
