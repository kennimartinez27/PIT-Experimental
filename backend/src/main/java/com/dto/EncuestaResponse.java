package com.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncuestaResponse {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private String evento;
    private Boolean activa;
    private String nombreCreador;
    private Long creadaPorId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<PreguntaResponse> preguntas;
    private Long totalRespuestas;
}
