package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.RespuestaEncuesta;
import com.entity.RespuestaPregunta;

@Repository
public interface RespuestaPreguntaRepository extends JpaRepository<RespuestaPregunta, Long> {
    
    List<RespuestaPregunta> findByRespuestaEncuesta(RespuestaEncuesta respuestaEncuesta);
    
    List<RespuestaPregunta> findByRespuestaEncuestaId(Long respuestaEncuestaId);
    
    Optional<RespuestaPregunta> findByRespuestaEncuestaIdAndPreguntaId(Long respuestaEncuestaId, Long preguntaId);
    
    @Query("SELECT rp FROM RespuestaPregunta rp WHERE rp.pregunta.id = :preguntaId")
    List<RespuestaPregunta> findByPreguntaId(@Param("preguntaId") Long preguntaId);
    
    @Query("SELECT COUNT(rp) FROM RespuestaPregunta rp WHERE rp.respuestaEncuesta.id = :respuestaEncuestaId")
    Long countByRespuestaEncuesta(@Param("respuestaEncuestaId") Long respuestaEncuestaId);
    
    void deleteByRespuestaEncuestaId(Long respuestaEncuestaId);
}
