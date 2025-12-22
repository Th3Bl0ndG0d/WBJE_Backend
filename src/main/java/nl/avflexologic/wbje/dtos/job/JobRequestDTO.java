package nl.avflexologic.wbje.dtos.job;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

/**
 * Inkomend contract voor het aanmaken en bijwerken van een Job.
 */
public record JobRequestDTO(
        String jobNumber,
        @NotNull LocalDateTime jobDate,
        String jobName,
        String info,
        Integer cylinderWidth,
        Integer cylinderCircumference,
        String noteInfo
) { }
