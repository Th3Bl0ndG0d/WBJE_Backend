package nl.avflexologic.wbje.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO representing a persisted report (plate).
 */
@Schema(description = "Response payload representing a persisted report (plate) within a cylinder.")
public record ReportResponseDTO(

        @Schema(
                description = "Database identifier of the report.",
                example = "15"
        )
        Long id,

        @Schema(
                description = "Sequential report number within the cylinder.",
                example = "1"
        )
        Integer reportNr,

        @Schema(
                description = "Width of the report in micrometers.",
                example = "500"
        )
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
                example = "10"
        )
        Long cylinderId,

        @Schema(
                description = "Identifier of the ReportSpec used by this report.",
                example = "3"
        )
        Long reportSpecId

) { }
