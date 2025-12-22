package nl.avflexologic.wbje.dtos.job;

import java.time.LocalDateTime;

/**
 * Uitgaand contract voor Job-gegevens.
 */
public record JobResponseDTO(
        Long id,
        String jobNumber,
        LocalDateTime jobDate,
        String jobName,
        String info,
        Integer cylinderWidth,
        Integer cylinderCircumference,
        String noteInfo
) { }

