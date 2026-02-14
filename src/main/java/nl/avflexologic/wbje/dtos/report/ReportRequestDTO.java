package nl.avflexologic.wbje.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Request DTO for creating or updating a report (plate).
 */
@Schema(description = "Request payload for creating or updating a report (plate) within a cylinder.")
public record ReportRequestDTO(

        @Schema(
                description = "Sequential report number within the cylinder.",
                example = "1",
                minimum = "1"
        )
        @Min(1)
        Integer reportNr,

        @Schema(
                description = "Width of the report in micrometers.",
                example = "500",
                minimum = "0"
        )
        @Min(0)
        Integer reportWidth,

        @Schema(
                description = "Horizontal offset of the report in micrometers.",
                example = "10"
        )
        Integer xOffset,

        @Schema(
                description = "Vertical offset of the report in micrometers.",
                example = "5"
        )
        Integer yOffset,

        @Schema(
                description = "Identifier of the parent cylinder.",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Long cylinderId,

        @Schema(
                description = "Identifier of the ReportSpec used by this report.",
                example = "3",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Long reportSpecId

) {
    /**
     * Enforces the minimal required fields for a valid request.
     */
    public ReportRequestDTO {
//        Objects.requireNonNull(reportNr, "reportNr is required.");
//        Objects.requireNonNull(cylinderId, "cylinderId is required.");
//        Objects.requireNonNull(reportSpecId, "reportSpecId is required.");
    }
}
