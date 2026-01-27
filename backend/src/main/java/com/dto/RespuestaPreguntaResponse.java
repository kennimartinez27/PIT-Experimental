package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaPreguntaResponse {
    
    private Long id;
    private Long preguntaId;
    private String textoPregunta;
    private String valorRespuesta;
}
