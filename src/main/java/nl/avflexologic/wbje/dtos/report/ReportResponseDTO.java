package nl.avflexologic.wbje.dtos.report;

/**
 * Response DTO representing a persisted report (plate).
 */
public record ReportResponseDTO(
        Long id,
        Integer reportNr,
        Integer reportWidth,
        Integer xOffset,
        Integer yOffset,
        Long cylinderId,
        Long reportSpecId
) { }
