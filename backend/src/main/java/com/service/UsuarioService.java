package com.service;

import com.config.AppConfig.PasswordEncoder;
import com.dto.CrearUsuarioRequest;
import com.dto.UsuarioResponse;
import com.entity.Usuario;
import com.exception.UsuarioNoEncontradoException;
import com.exception.UsuarioYaExisteException;
import com.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new UsuarioYaExisteException("El username '" + request.getUsername() + "' ya existe");
        }
        
        // Crear el usuario
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nombreCompleto(request.getNombreCompleto())
                .rol(request.getRol())
                .activo(true)
                .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        return mapearAResponse(usuarioGuardado);
    }
    
    // Método para obtener todos los usuarios
    public List<UsuarioResponse> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    // Método para obtener un usuario por ID
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado"));
        return mapearAResponse(usuario);
    }
    
    // Método para buscar usuarios por nombre y/o rol
    public List<UsuarioResponse> buscarUsuarios(String nombre, String rol) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> {
                    boolean coincideNombre = nombre == null || nombre.trim().isEmpty() || 
                            usuario.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase());
                    
                    boolean coincideRol = true;
                    if (rol != null && !rol.trim().isEmpty()) {
                        try {
                            coincideRol = usuario.getRol().name().equalsIgnoreCase(rol.trim());
                        } catch (Exception e) {
                            coincideRol = false;
                        }
                    }
                    
                    return coincideNombre && coincideRol;
                })
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    // Método privado para mapear Usuario a UsuarioResponse
    private UsuarioResponse mapearAResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
