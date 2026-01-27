package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Encuesta;
import com.entity.Usuario;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {
    
    List<Encuesta> findByActivaTrue();
    
    List<Encuesta> findByCreadaPor(Usuario usuario);
    
    List<Encuesta> findByEvento(String evento);
    
    List<Encuesta> findByActivaTrueAndEvento(String evento);
    
    @Query("SELECT e FROM Encuesta e WHERE e.activa = true AND e.creadaPor.id = :usuarioId")
    List<Encuesta> findEncuestasActivasByUsuario(@Param("usuarioId") Long usuarioId);
    
    Optional<Encuesta> findByIdAndActivaTrue(Long id);
    
    @Query("SELECT COUNT(e) FROM Encuesta e WHERE e.creadaPor.id = :usuarioId")
    Long countByUsuario(@Param("usuarioId") Long usuarioId);
}
