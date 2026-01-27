package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Encuesta;
import com.entity.Pregunta;
import com.entity.TipoPregunta;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    
    List<Pregunta> findByEncuestaOrderByOrdenAsc(Encuesta encuesta);
    
    List<Pregunta> findByEncuestaIdOrderByOrdenAsc(Long encuestaId);
    
    List<Pregunta> findByEncuestaAndObligatoriaTrue(Encuesta encuesta);
    
    List<Pregunta> findByTipoPregunta(TipoPregunta tipoPregunta);
    
    @Query("SELECT COUNT(p) FROM Pregunta p WHERE p.encuesta.id = :encuestaId")
    Long countByEncuesta(@Param("encuestaId") Long encuestaId);
    
    @Query("SELECT MAX(p.orden) FROM Pregunta p WHERE p.encuesta.id = :encuestaId")
    Integer findMaxOrdenByEncuesta(@Param("encuestaId") Long encuestaId);
    
    void deleteByEncuestaId(Long encuestaId);
}
