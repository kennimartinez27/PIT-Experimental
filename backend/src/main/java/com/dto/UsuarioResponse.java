package com.dto;

import com.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String username;
    private String nombreCompleto;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
