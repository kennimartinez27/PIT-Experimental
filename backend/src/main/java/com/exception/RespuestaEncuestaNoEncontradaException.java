package com.exception;

public class RespuestaEncuestaNoEncontradaException extends RuntimeException {
    public RespuestaEncuestaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
    
    public RespuestaEncuestaNoEncontradaException(Long id) {
        super("Respuesta de encuesta no encontrada con ID: " + id);
    }
}
