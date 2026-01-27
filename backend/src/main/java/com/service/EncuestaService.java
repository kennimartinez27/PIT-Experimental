package com.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.ActualizarEncuestaRequest;
import com.dto.CrearEncuestaRequest;
import com.dto.EncuestaResponse;
import com.dto.PreguntaRequest;
import com.dto.PreguntaResponse;
import com.dto.ResponderEncuestaRequest;
import com.dto.RespuestaEncuestaResponse;
import com.dto.RespuestaPreguntaRequest;
import com.dto.RespuestaPreguntaResponse;
import com.entity.Encuesta;
import com.entity.Pregunta;
import com.entity.RespuestaEncuesta;
import com.entity.RespuestaPregunta;
import com.entity.Usuario;
import com.exception.EncuestaInactivaException;
import com.exception.EncuestaNoEncontradaException;
import com.exception.PreguntaNoEncontradaException;
import com.exception.RespuestaEncuestaNoEncontradaException;
import com.exception.UsuarioNoEncontradoException;
import com.repository.EncuestaRepository;
import com.repository.PreguntaRepository;
import com.repository.RespuestaEncuestaRepository;
import com.repository.RespuestaPreguntaRepository;
import com.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EncuestaService {

    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaEncuestaRepository respuestaEncuestaRepository;
    private final RespuestaPreguntaRepository respuestaPreguntaRepository;
    private final UsuarioRepository usuarioRepository;

    // ==================== CRUD DE ENCUESTAS ====================
    
    public EncuestaResponse crearEncuesta(CrearEncuestaRequest request, Long usuarioId) {
        Usuario creador = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
        
        // Crear la encuesta
        Encuesta encuesta = Encuesta.builder()
            .titulo(request.getTitulo())
            .descripcion(request.getDescripcion())
            .evento(request.getEvento())
            .activa(request.getActiva())
            .creadaPor(creador)
            .build();
        
        Encuesta encuestaGuardada = encuestaRepository.save(encuesta);
        
        // Crear las preguntas asociadas
        if (request.getPreguntas() != null && !request.getPreguntas().isEmpty()) {
            int orden = 1;
            for (PreguntaRequest preguntaReq : request.getPreguntas()) {
                Pregunta pregunta = Pregunta.builder()
                    .encuesta(encuestaGuardada)
                    .textoPregunta(preguntaReq.getTextoPregunta())
                    .tipoPregunta(preguntaReq.getTipoPregunta())
                    .obligatoria(preguntaReq.getObligatoria())
                    .orden(preguntaReq.getOrden() != null ? preguntaReq.getOrden() : orden++)
                    .opciones(preguntaReq.getOpciones())
                    .build();
                preguntaRepository.save(pregunta);
            }
        }
        
        return convertirAEncuestaResponse(encuestaGuardada);
    }
    
    @Transactional(readOnly = true)
    public List<EncuestaResponse> obtenerTodasLasEncuestas() {
        return encuestaRepository.findAll().stream()
            .map(this::convertirAEncuestaResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EncuestaResponse> obtenerEncuestasActivas() {
        return encuestaRepository.findByActivaTrue().stream()
            .map(this::convertirAEncuestaResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EncuestaResponse> obtenerEncuestasPorEvento(String evento) {
        return encuestaRepository.findByEvento(evento).stream()
            .map(this::convertirAEncuestaResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EncuestaResponse> obtenerEncuestasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
        
        return encuestaRepository.findByCreadaPor(usuario).stream()
            .map(this::convertirAEncuestaResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public EncuestaResponse obtenerEncuestaPorId(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new EncuestaNoEncontradaException(id));
        
        return convertirAEncuestaResponse(encuesta);
    }
    
    public EncuestaResponse actualizarEncuesta(Long id, ActualizarEncuestaRequest request) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new EncuestaNoEncontradaException(id));
        
        if (request.getTitulo() != null) {
            encuesta.setTitulo(request.getTitulo());
        }
        if (request.getDescripcion() != null) {
            encuesta.setDescripcion(request.getDescripcion());
        }
        if (request.getEvento() != null) {
            encuesta.setEvento(request.getEvento());
        }
        if (request.getActiva() != null) {
            encuesta.setActiva(request.getActiva());
        }
        
        Encuesta encuestaActualizada = encuestaRepository.save(encuesta);
        return convertirAEncuestaResponse(encuestaActualizada);
    }
    
    public void eliminarEncuesta(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new EncuestaNoEncontradaException(id));
        
        // Eliminar todas las respuestas asociadas
        List<RespuestaEncuesta> respuestas = respuestaEncuestaRepository.findByEncuestaId(id);
        for (RespuestaEncuesta respuesta : respuestas) {
            respuestaPreguntaRepository.deleteByRespuestaEncuestaId(respuesta.getId());
        }
        respuestaEncuestaRepository.deleteAll(respuestas);
        
        // Eliminar todas las preguntas
        preguntaRepository.deleteByEncuestaId(id);
        
        // Eliminar la encuesta
        encuestaRepository.delete(encuesta);
    }
    
    public EncuestaResponse activarEncuesta(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new EncuestaNoEncontradaException(id));
        
        encuesta.setActiva(true);
        Encuesta encuestaActualizada = encuestaRepository.save(encuesta);
        return convertirAEncuestaResponse(encuestaActualizada);
    }
    
    public EncuestaResponse desactivarEncuesta(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new EncuestaNoEncontradaException(id));
        
        encuesta.setActiva(false);
        Encuesta encuestaActualizada = encuestaRepository.save(encuesta);
        return convertirAEncuestaResponse(encuestaActualizada);
    }
    
    // ==================== GESTIÓN DE PREGUNTAS ====================
    
    public PreguntaResponse agregarPregunta(Long encuestaId, PreguntaRequest request) {
        Encuesta encuesta = encuestaRepository.findById(encuestaId)
            .orElseThrow(() -> new EncuestaNoEncontradaException(encuestaId));
        
        Integer maxOrden = preguntaRepository.findMaxOrdenByEncuesta(encuestaId);
        int nuevoOrden = maxOrden != null ? maxOrden + 1 : 1;
        
        Pregunta pregunta = Pregunta.builder()
            .encuesta(encuesta)
            .textoPregunta(request.getTextoPregunta())
            .tipoPregunta(request.getTipoPregunta())
            .obligatoria(request.getObligatoria())
            .orden(request.getOrden() != null ? request.getOrden() : nuevoOrden)
            .opciones(request.getOpciones())
            .build();
        
        Pregunta preguntaGuardada = preguntaRepository.save(pregunta);
        return convertirAPreguntaResponse(preguntaGuardada);
    }
    
    public PreguntaResponse actualizarPregunta(Long preguntaId, PreguntaRequest request) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
            .orElseThrow(() -> new PreguntaNoEncontradaException(preguntaId));
        
        if (request.getTextoPregunta() != null) {
            pregunta.setTextoPregunta(request.getTextoPregunta());
        }
        if (request.getTipoPregunta() != null) {
            pregunta.setTipoPregunta(request.getTipoPregunta());
        }
        if (request.getObligatoria() != null) {
            pregunta.setObligatoria(request.getObligatoria());
        }
        if (request.getOrden() != null) {
            pregunta.setOrden(request.getOrden());
        }
        if (request.getOpciones() != null) {
            pregunta.setOpciones(request.getOpciones());
        }
        
        Pregunta preguntaActualizada = preguntaRepository.save(pregunta);
        return convertirAPreguntaResponse(preguntaActualizada);
    }
    
    public void eliminarPregunta(Long preguntaId) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
            .orElseThrow(() -> new PreguntaNoEncontradaException(preguntaId));
        
        // Eliminar respuestas asociadas
        List<RespuestaPregunta> respuestas = respuestaPreguntaRepository.findByPreguntaId(preguntaId);
        respuestaPreguntaRepository.deleteAll(respuestas);
        
        preguntaRepository.delete(pregunta);
    }
    
    @Transactional(readOnly = true)
    public List<PreguntaResponse> obtenerPreguntasPorEncuesta(Long encuestaId) {
        return preguntaRepository.findByEncuestaIdOrderByOrdenAsc(encuestaId).stream()
            .map(this::convertirAPreguntaResponse)
            .collect(Collectors.toList());
    }
    
    // ==================== RESPONDER ENCUESTAS ====================
    
    public RespuestaEncuestaResponse responderEncuesta(ResponderEncuestaRequest request, Long encuestadorId) {
        // Validar encuesta
        Encuesta encuesta = encuestaRepository.findById(request.getEncuestaId())
            .orElseThrow(() -> new EncuestaNoEncontradaException(request.getEncuestaId()));
        
        if (!encuesta.getActiva()) {
            throw new EncuestaInactivaException(encuesta.getId());
        }
        
        // Validar encuestador
        Usuario encuestador = usuarioRepository.findById(encuestadorId)
            .orElseThrow(() -> new UsuarioNoEncontradoException(encuestadorId));
        
        // Crear respuesta de encuesta
        RespuestaEncuesta respuestaEncuesta = RespuestaEncuesta.builder()
            .encuesta(encuesta)
            .encuestador(encuestador)
            .fechaRespuesta(LocalDateTime.now())
            .completada(true)
            .build();
        
        RespuestaEncuesta respuestaGuardada = respuestaEncuestaRepository.save(respuestaEncuesta);
        
        // Guardar respuestas individuales
        for (RespuestaPreguntaRequest respuestaReq : request.getRespuestas()) {
            Pregunta pregunta = preguntaRepository.findById(respuestaReq.getPreguntaId())
                .orElseThrow(() -> new PreguntaNoEncontradaException(respuestaReq.getPreguntaId()));
            
            RespuestaPregunta respuestaPregunta = RespuestaPregunta.builder()
                .respuestaEncuesta(respuestaGuardada)
                .pregunta(pregunta)
                .valorRespuesta(respuestaReq.getValorRespuesta())
                .build();
            
            respuestaPreguntaRepository.save(respuestaPregunta);
        }
        
        return convertirARespuestaEncuestaResponse(respuestaGuardada);
    }
    
    @Transactional(readOnly = true)
    public List<RespuestaEncuestaResponse> obtenerRespuestasPorEncuesta(Long encuestaId) {
        return respuestaEncuestaRepository.findByEncuestaId(encuestaId).stream()
            .map(this::convertirARespuestaEncuestaResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RespuestaEncuestaResponse obtenerRespuestaPorId(Long respuestaId) {
        RespuestaEncuesta respuesta = respuestaEncuestaRepository.findById(respuestaId)
            .orElseThrow(() -> new RespuestaEncuestaNoEncontradaException(respuestaId));
        
        return convertirARespuestaEncuestaResponse(respuesta);
    }
    
    @Transactional(readOnly = true)
    public Long contarRespuestasCompletadas(Long encuestaId) {
        return respuestaEncuestaRepository.countRespuestasCompletasByEncuesta(encuestaId);
    }
    
    // ==================== CONVERSORES ====================
    
    private EncuestaResponse convertirAEncuestaResponse(Encuesta encuesta) {
        List<PreguntaResponse> preguntas = preguntaRepository
            .findByEncuestaOrderByOrdenAsc(encuesta)
            .stream()
            .map(this::convertirAPreguntaResponse)
            .collect(Collectors.toList());
        
        Long totalRespuestas = respuestaEncuestaRepository
            .countRespuestasCompletasByEncuesta(encuesta.getId());
        
        return EncuestaResponse.builder()
            .id(encuesta.getId())
            .titulo(encuesta.getTitulo())
            .descripcion(encuesta.getDescripcion())
            .evento(encuesta.getEvento())
            .activa(encuesta.getActiva())
            .nombreCreador(encuesta.getCreadaPor().getNombreCompleto())
            .creadaPorId(encuesta.getCreadaPor().getId())
            .fechaCreacion(encuesta.getFechaCreacion())
            .fechaActualizacion(encuesta.getFechaActualizacion())
            .preguntas(preguntas)
            .totalRespuestas(totalRespuestas)
            .build();
    }
    
    private PreguntaResponse convertirAPreguntaResponse(Pregunta pregunta) {
        return PreguntaResponse.builder()
            .id(pregunta.getId())
            .textoPregunta(pregunta.getTextoPregunta())
            .tipoPregunta(pregunta.getTipoPregunta())
            .obligatoria(pregunta.getObligatoria())
            .orden(pregunta.getOrden())
            .opciones(pregunta.getOpciones())
            .build();
    }
    
    private RespuestaEncuestaResponse convertirARespuestaEncuestaResponse(RespuestaEncuesta respuestaEncuesta) {
        List<RespuestaPreguntaResponse> respuestas = respuestaPreguntaRepository
            .findByRespuestaEncuesta(respuestaEncuesta)
            .stream()
            .map(this::convertirARespuestaPreguntaResponse)
            .collect(Collectors.toList());
        
        return RespuestaEncuestaResponse.builder()
            .id(respuestaEncuesta.getId())
            .encuestaId(respuestaEncuesta.getEncuesta().getId())
            .tituloEncuesta(respuestaEncuesta.getEncuesta().getTitulo())
            .encuestadorId(respuestaEncuesta.getEncuestador().getId())
            .nombreEncuestador(respuestaEncuesta.getEncuestador().getNombreCompleto())
            .fechaRespuesta(respuestaEncuesta.getFechaRespuesta())
            .completada(respuestaEncuesta.getCompletada())
            .respuestas(respuestas)
            .build();
    }
    
    private RespuestaPreguntaResponse convertirARespuestaPreguntaResponse(RespuestaPregunta respuestaPregunta) {
        return RespuestaPreguntaResponse.builder()
            .id(respuestaPregunta.getId())
            .preguntaId(respuestaPregunta.getPregunta().getId())
            .textoPregunta(respuestaPregunta.getPregunta().getTextoPregunta())
            .valorRespuesta(respuestaPregunta.getValorRespuesta())
            .build();
    }
}
