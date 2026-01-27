package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarEncuestaRequest {
    
    private String titulo;
    private String descripcion;
    private String evento;
    private Boolean activa;
}
