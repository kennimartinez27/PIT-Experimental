package com.dto;

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
public class RespuestaPreguntaRequest {
    
    @NotNull(message = "El ID de la pregunta es obligatorio")
    private Long preguntaId;
    
    @NotBlank(message = "La respuesta no puede estar vacía")
    private String valorRespuesta;
}
