package com.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearEncuestaRequest {
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    private String descripcion;
    
    @NotBlank(message = "El evento es obligatorio")
    private String evento;
    
    @Builder.Default
    private Boolean activa = true;
    
    @NotNull(message = "Debe incluir al menos una pregunta")
    private List<PreguntaRequest> preguntas;
}
