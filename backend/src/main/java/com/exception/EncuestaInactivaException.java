package com.exception;

public class EncuestaInactivaException extends RuntimeException {
    public EncuestaInactivaException(String mensaje) {
        super(mensaje);
    }
    
    public EncuestaInactivaException(Long id) {
        super("La encuesta con ID " + id + " está inactiva y no puede recibir respuestas");
    }
}
