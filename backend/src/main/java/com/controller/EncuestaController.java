package com.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.ActualizarEncuestaRequest;
import com.dto.CrearEncuestaRequest;
import com.dto.EncuestaResponse;
import com.dto.PreguntaRequest;
import com.dto.PreguntaResponse;
import com.dto.ResponderEncuestaRequest;
import com.dto.RespuestaEncuestaResponse;
import com.service.EncuestaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/encuestas")
@RequiredArgsConstructor
public class EncuestaController {

    private final EncuestaService encuestaService;

    // ==================== CRUD DE ENCUESTAS ====================
    
    @PostMapping
    public ResponseEntity<EncuestaResponse> crearEncuesta(
        @Valid @RequestBody CrearEncuestaRequest request,
        @RequestParam Long usuarioId
    ) {
        EncuestaResponse response = encuestaService.crearEncuesta(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<EncuestaResponse>> obtenerTodasLasEncuestas() {
        List<EncuestaResponse> encuestas = encuestaService.obtenerTodasLasEncuestas();
        return ResponseEntity.ok(encuestas);
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<EncuestaResponse>> obtenerEncuestasActivas() {
        List<EncuestaResponse> encuestas = encuestaService.obtenerEncuestasActivas();
        return ResponseEntity.ok(encuestas);
    }
    
    @GetMapping("/evento/{evento}")
    public ResponseEntity<List<EncuestaResponse>> obtenerEncuestasPorEvento(@PathVariable String evento) {
        List<EncuestaResponse> encuestas = encuestaService.obtenerEncuestasPorEvento(evento);
        return ResponseEntity.ok(encuestas);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EncuestaResponse>> obtenerEncuestasPorUsuario(@PathVariable Long usuarioId) {
        List<EncuestaResponse> encuestas = encuestaService.obtenerEncuestasPorUsuario(usuarioId);
        return ResponseEntity.ok(encuestas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EncuestaResponse> obtenerEncuestaPorId(@PathVariable Long id) {
        EncuestaResponse encuesta = encuestaService.obtenerEncuestaPorId(id);
        return ResponseEntity.ok(encuesta);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EncuestaResponse> actualizarEncuesta(
        @PathVariable Long id,
        @Valid @RequestBody ActualizarEncuestaRequest request
    ) {
        EncuestaResponse encuesta = encuestaService.actualizarEncuesta(id, request);
        return ResponseEntity.ok(encuesta);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEncuesta(@PathVariable Long id) {
        encuestaService.eliminarEncuesta(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activar")
    public ResponseEntity<EncuestaResponse> activarEncuesta(@PathVariable Long id) {
        EncuestaResponse encuesta = encuestaService.activarEncuesta(id);
        return ResponseEntity.ok(encuesta);
    }
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<EncuestaResponse> desactivarEncuesta(@PathVariable Long id) {
        EncuestaResponse encuesta = encuestaService.desactivarEncuesta(id);
        return ResponseEntity.ok(encuesta);
    }
    
    // ==================== GESTIÓN DE PREGUNTAS ====================
    
    @PostMapping("/{encuestaId}/preguntas")
    public ResponseEntity<PreguntaResponse> agregarPregunta(
        @PathVariable Long encuestaId,
        @Valid @RequestBody PreguntaRequest request
    ) {
        PreguntaResponse pregunta = encuestaService.agregarPregunta(encuestaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pregunta);
    }
    
    @PutMapping("/preguntas/{preguntaId}")
    public ResponseEntity<PreguntaResponse> actualizarPregunta(
        @PathVariable Long preguntaId,
        @Valid @RequestBody PreguntaRequest request
    ) {
        PreguntaResponse pregunta = encuestaService.actualizarPregunta(preguntaId, request);
        return ResponseEntity.ok(pregunta);
    }
    
    @DeleteMapping("/preguntas/{preguntaId}")
    public ResponseEntity<Void> eliminarPregunta(@PathVariable Long preguntaId) {
        encuestaService.eliminarPregunta(preguntaId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{encuestaId}/preguntas")
    public ResponseEntity<List<PreguntaResponse>> obtenerPreguntasPorEncuesta(@PathVariable Long encuestaId) {
        List<PreguntaResponse> preguntas = encuestaService.obtenerPreguntasPorEncuesta(encuestaId);
        return ResponseEntity.ok(preguntas);
    }
    
    // ==================== RESPONDER ENCUESTAS ====================
    
    @PostMapping("/responder")
    public ResponseEntity<RespuestaEncuestaResponse> responderEncuesta(
        @Valid @RequestBody ResponderEncuestaRequest request,
        @RequestParam Long encuestadorId
    ) {
        RespuestaEncuestaResponse respuesta = encuestaService.responderEncuesta(request, encuestadorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
    
    @GetMapping("/{encuestaId}/respuestas")
    public ResponseEntity<List<RespuestaEncuestaResponse>> obtenerRespuestasPorEncuesta(@PathVariable Long encuestaId) {
        List<RespuestaEncuestaResponse> respuestas = encuestaService.obtenerRespuestasPorEncuesta(encuestaId);
        return ResponseEntity.ok(respuestas);
    }
    
    @GetMapping("/respuestas/{respuestaId}")
    public ResponseEntity<RespuestaEncuestaResponse> obtenerRespuestaPorId(@PathVariable Long respuestaId) {
        RespuestaEncuestaResponse respuesta = encuestaService.obtenerRespuestaPorId(respuestaId);
        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping("/{encuestaId}/estadisticas")
    public ResponseEntity<Long> contarRespuestasCompletadas(@PathVariable Long encuestaId) {
        Long total = encuestaService.contarRespuestasCompletadas(encuestaId);
        return ResponseEntity.ok(total);
    }
}
