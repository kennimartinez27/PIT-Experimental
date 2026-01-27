package com.exception;

public class PreguntaNoEncontradaException extends RuntimeException {
    public PreguntaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
    
    public PreguntaNoEncontradaException(Long id) {
        super("Pregunta no encontrada con ID: " + id);
    }
}
