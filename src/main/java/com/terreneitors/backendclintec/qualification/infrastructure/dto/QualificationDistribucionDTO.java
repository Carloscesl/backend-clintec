// qualification/infrastructure/dto/QualificationDistribucionDTO.java
package com.terreneitors.backendclintec.qualification.infrastructure.dto;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import java.util.Map;

public record QualificationDistribucionDTO(
        Map<Qualification, Long> distribucion
) {}