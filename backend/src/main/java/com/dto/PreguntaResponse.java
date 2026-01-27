package com.dto;

import com.entity.TipoPregunta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreguntaResponse {
    
    private Long id;
    private String textoPregunta;
    private TipoPregunta tipoPregunta;
    private Boolean obligatoria;
    private Integer orden;
    private String opciones;
}
