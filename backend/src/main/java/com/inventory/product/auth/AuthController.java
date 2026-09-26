package com.inventory.product.auth;

import com.inventory.product.auth.dto.LoginRequest;
import com.inventory.product.auth.dto.LoginResponse;
import com.inventory.product.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login con credenciales fijas para obtener un JWT")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final String configuredUsername;
    private final String configuredPassword;

    public AuthController(JwtUtil jwtUtil,
                           @Value("${auth.username}") String configuredUsername,
                           @Value("${auth.password}") String configuredPassword) {
        this.jwtUtil = jwtUtil;
        this.configuredUsername = configuredUsername;
        this.configuredPassword = configuredPassword;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica con usuario/contraseña fijos y devuelve un JWT")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        if (!configuredUsername.equals(request.getUsername()) || !configuredPassword.equals(request.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        String token = jwtUtil.generateToken(request.getUsername());
        return new LoginResponse(token);
    }
}
