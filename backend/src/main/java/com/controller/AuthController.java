package com.controller;

import com.config.AppConfig.PasswordEncoder;
import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.entity.Usuario;
import com.exception.UsuarioNoEncontradoException;
import com.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario o contraseña incorrectos"));
        
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponse.builder()
                            .mensaje("Usuario o contraseña incorrectos")
                            .build());
        }
        
        if (!usuario.getActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(LoginResponse.builder()
                            .mensaje("Usuario desactivado")
                            .build());
        }
        
        LoginResponse response = LoginResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .rol(usuario.getRol())
                .mensaje("Login exitoso")
                .build();
        
        return ResponseEntity.ok(response);
    }
}
