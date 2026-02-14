package nl.avflexologic.wbje.dtos.reportSpec;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Schema(description = "Request payload for creating or updating a report specification.")
public record ReportSpecRequestDTO(

        @Schema(
                description = "Name of the report specification.",
                example = "Flexo Plate Type A",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "reportName is required.")
        @Size(max = 255, message = "reportName must be at most 255 characters.")
        String reportName,

        @Schema(
                description = "Type or category of the report specification.",
                example = "Photopolymer"
        )
        @Size(max = 255, message = "reportType must be at most 255 characters.")
        String reportType,

        @Schema(
                description = "Thickness of the report in micrometers.",
                example = "170",
                minimum = "0"
        )
        @Min(0)
        Integer thickness,

        @Schema(
                description = "Optional additional information about the report specification.",
                example = "Optimized for high-speed printing."
        )
        @Size(max = 255, message = "info must be at most 255 characters.")
        String info

) {
    public ReportSpecRequestDTO {
        //Objects.requireNonNull(reportName, "reportName is required.");
    }
}
