package nl.avflexologic.wbje.dtos.reportSpec;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing a persisted report specification.")
public record ReportSpecResponseDTO(

        @Schema(
                description = "Database identifier of the report specification.",
                example = "7"
        )
        Long id,

        @Schema(
                description = "Name of the report specification.",
                example = "Flexo Plate Type A"
        )
        String reportName,

        @Schema(
                description = "Type or category of the report specification.",
                example = "Photopolymer"
        )
        String reportType,

        @Schema(
                description = "Thickness of the report in micrometers.",
                example = "170"
        )
        Integer thickness,

        @Schema(
                description = "Optional additional information about the report specification.",
                example = "Optimized for high-speed printing."
        )
        String info

) { }
