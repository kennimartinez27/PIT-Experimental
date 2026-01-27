package com.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponderEncuestaRequest {
    
    @NotNull(message = "El ID de la encuesta es obligatorio")
    private Long encuestaId;
    
    @NotNull(message = "Las respuestas son obligatorias")
    private List<RespuestaPreguntaRequest> respuestas;
}
