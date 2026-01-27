package com.exception;

public class EncuestaNoEncontradaException extends RuntimeException {
    public EncuestaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
    
    public EncuestaNoEncontradaException(Long id) {
        super("Encuesta no encontrada con ID: " + id);
    }
}
