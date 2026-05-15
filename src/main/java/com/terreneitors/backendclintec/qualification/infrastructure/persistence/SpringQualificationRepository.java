package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringQualificationRepository extends JpaRepository<QualificationEntity, Long> {
    Optional<QualificationEntity> findByClienteId(Long clienteId);
    List<QualificationEntity> findByClasificacion(Qualification clasificacion);

    List<QualificationEntity> findByUltimaActualizacionBefore(LocalDateTime threshold);

    @Query("SELECT q FROM QualificationEntity q ORDER BY q.puntaje DESC LIMIT :n")
    List<QualificationEntity> findTopN(int n);

    @Query("""
    SELECT DISTINCT q FROM QualificationEntity q
    WHERE q.clienteId IN (
        SELECT h.clienteId FROM QualificationHistoryEntity h
        WHERE h.fecha >= :desde AND h.puntajeNuevo < h.puntajeAnterior
    )
""")
    List<QualificationEntity> findClientesQueBAjaronPuntaje(LocalDateTime desde);

}
