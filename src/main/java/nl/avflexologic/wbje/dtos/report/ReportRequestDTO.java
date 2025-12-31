package nl.avflexologic.wbje.dtos.report;

import java.util.Objects;

/**
 * Request DTO for creating or updating a report (plate).
 */
public record ReportRequestDTO(
        Integer reportNr,
        Integer reportWidth,
        Integer xOffset,
        Integer yOffset,
        Long cylinderId,
        Long reportSpecId
) {
    /**
     * Enforces the minimal required fields for a valid request.
     */
    public ReportRequestDTO {
        Objects.requireNonNull(reportNr, "reportNr is required.");
        Objects.requireNonNull(cylinderId, "cylinderId is required.");
        Objects.requireNonNull(reportSpecId, "reportSpecId is required.");
    }
}
