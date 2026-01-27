package com.dto;

import com.entity.TipoPregunta;

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
public class PreguntaRequest {
    
    @NotBlank(message = "El texto de la pregunta es obligatorio")
    private String textoPregunta;
    
    @NotNull(message = "El tipo de pregunta es obligatorio")
    private TipoPregunta tipoPregunta;
    
    @Builder.Default
    private Boolean obligatoria = false;
    
    private Integer orden;
    
    private String opciones; // JSON string para opciones múltiples
}
