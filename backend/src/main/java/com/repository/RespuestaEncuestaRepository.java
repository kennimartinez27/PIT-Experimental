package com.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Encuesta;
import com.entity.RespuestaEncuesta;
import com.entity.Usuario;

@Repository
public interface RespuestaEncuestaRepository extends JpaRepository<RespuestaEncuesta, Long> {
    
    List<RespuestaEncuesta> findByEncuesta(Encuesta encuesta);
    
    List<RespuestaEncuesta> findByEncuestaId(Long encuestaId);
    
    List<RespuestaEncuesta> findByEncuestador(Usuario encuestador);
    
    List<RespuestaEncuesta> findByCompletadaTrue();
    
    List<RespuestaEncuesta> findByEncuestaIdAndCompletadaTrue(Long encuestaId);
    
    @Query("SELECT COUNT(r) FROM RespuestaEncuesta r WHERE r.encuesta.id = :encuestaId AND r.completada = true")
    Long countRespuestasCompletasByEncuesta(@Param("encuestaId") Long encuestaId);
    
    @Query("SELECT r FROM RespuestaEncuesta r WHERE r.encuesta.id = :encuestaId AND r.fechaRespuesta BETWEEN :fechaInicio AND :fechaFin")
    List<RespuestaEncuesta> findByEncuestaAndFechaBetween(
        @Param("encuestaId") Long encuestaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT COUNT(r) FROM RespuestaEncuesta r WHERE r.encuestador.id = :usuarioId")
    Long countByEncuestador(@Param("usuarioId") Long usuarioId);
}
