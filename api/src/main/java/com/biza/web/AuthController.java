package com.biza.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biza.dto.AuthUserResponse;
import com.biza.dto.LoginRequest;
import com.biza.dto.LoginResponse;
import com.biza.repo.UtilizadorRepo;
import com.biza.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UtilizadorRepo utilizadorRepo;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          UtilizadorRepo utilizadorRepo) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.utilizadorRepo = utilizadorRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {

        // 1) valida credenciais (usa UserDetailsService + PasswordEncoder configurados)
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        // 2) buscar o utilizador real (para devolver id/name/role)
        var u = utilizadorRepo.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        if (!u.isActivo())
            throw new RuntimeException("Utilizador inactivo");

        // 3) gerar token
        String token = jwtService.generateToken(u.getUsername(), u.getRole().name());

        // 4) devolver user completo
        AuthUserResponse user = new AuthUserResponse(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getRole()
        );

        return ResponseEntity.ok(new LoginResponse(token, user));
    }
}
