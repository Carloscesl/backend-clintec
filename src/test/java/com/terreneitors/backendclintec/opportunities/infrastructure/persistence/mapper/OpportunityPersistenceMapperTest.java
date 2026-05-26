package com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityResponseDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.OpportunityEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityPersistenceMapperTest {

    private final OpportunityPersistenceMapper mapper = new OpportunityPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        OpportunityEntity entity = new OpportunityEntity();
        entity.setId(100L);
        // IMPORTANTE: Asegura que los campos usados por la lógica interna no sean null
        entity.setEtapa(StageOpportunity.PROSPECCION); // Supongamos que este es tu Enum
        entity.setProbabilidad(10);

        Opportunity domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(100L, domain.getIdOportunidad());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        Opportunity domain = new Opportunity();

        // 1. Asignamos primero StageOpportunity para que la lógica interna de probabilidad tenga qué consultar
        domain.setStageOpportunity(StageOpportunity.PROPUESTA);

        // 2. Ahora sí podemos asignar lo demás
        domain.setIdOportunidad(200L);
        domain.setProbabilidad(50);

        OpportunityEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(200L, entity.getId());
        assertEquals(50, entity.getProbabilidad());
    }

    @Test
    @DisplayName("Debe mapear correctamente a Record DTO")
    void toDTO_ShouldMapCorrectly() {
        Opportunity domain = new Opportunity();

        // Al igual que arriba, inicializamos primero el stage
        domain.setStageOpportunity(StageOpportunity.PROSPECCION);
        domain.setIdOportunidad(300L);
        domain.setProbabilidad(10);

        OpportunityResponseDTO dto = mapper.toDTO(domain);

        assertNotNull(dto);
        assertEquals(300L, dto.idOportunidad());
    }
}