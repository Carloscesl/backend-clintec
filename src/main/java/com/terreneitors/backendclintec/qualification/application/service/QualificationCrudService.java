package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QualificationCrudService implements QualificationCrudUseCase {
    private final QualificationRepositoryPort qualificationRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;

    @Override
    public QualificationClient createQualificationInitial(Long clienteId) {
        log.info("[CALIFICACION_CREAR] clienteId={}", clienteId);
        if (clientRepositoryPort.findById(clienteId).isPresent()){
            throw new InvalidStateException(
                    "El cliente con id " + clienteId + " ya tiene una calificación asignada.");
        }

        QualificationClient nuevaQualification = new QualificationClient();
        nuevaQualification.setId(clienteId);

        QualificationClient guardada = qualificationRepositoryPort.save(nuevaQualification);

        if (guardada == null || guardada.getId() == null) {
            log.error("[CALIFICACION_CREAR_FALLIDO] clienteId={}", clienteId);
            throw new ValidationException(
                    "No se pudo crear la calificación para el cliente: " + clienteId);
        }

        log.info("[CALIFICACION_CREADA] id={} | clienteId={} | clasificacion={}",
                guardada.getId(), clienteId, guardada.getClasificacion());

        return guardada;
    }

    @Override
    public Optional<QualificationClient> findByClientId(Long clienteId) {
        return qualificationRepositoryPort.findByClientId(clienteId);
    }

    @Override
    public List<QualificationClient> findAll() {
        return qualificationRepositoryPort.findAll();
    }

    @Override
    public QualificationClient updateScore(Long clienteId, int newEscore) {
        log.info("[CALIFICACION_ACTUALIZAR] clienteId={} | nuevoPuntaje={}",
                clienteId, newEscore);
        QualificationClient calificacion = qualificationRepositoryPort
                .findByClientId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calificacion", "clienteId", clienteId));
        calificacion.setPuntaje(newEscore);

        QualificationClient actualizada = qualificationRepositoryPort.save(calificacion);
        log.info("[CALIFICACION_ACTUALIZADA] clienteId={} | puntaje={} | clasificacion={}",
                clienteId, actualizada.getPuntaje(), actualizada.getClasificacion());

        return actualizada;
    }
}
