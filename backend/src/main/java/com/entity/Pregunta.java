package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preguntas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pregunta {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "encuesta_id", nullable = false)
    private Encuesta encuesta;

    @Column(name = "texto_pregunta", nullable = false)
    private String textoPregunta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pregunta", nullable = false)
    private TipoPregunta tipoPregunta;

    @Builder.Default
    private Boolean obligatoria = false;

    private Integer orden;

    @Column(columnDefinition = "TEXT")
    private String opciones; // JSON en text
}
