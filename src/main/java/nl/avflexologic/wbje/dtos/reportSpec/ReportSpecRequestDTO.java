package nl.avflexologic.wbje.dtos.reportSpec;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public record ReportSpecRequestDTO(
        @NotBlank(message = "reportName is required.")
        @Size(max = 255, message = "reportName must be at most 255 characters.")
        String reportName,

        @Size(max = 255, message = "reportType must be at most 255 characters.")
        String reportType,

        Integer thickness,

        @Size(max = 255, message = "info must be at most 255 characters.")
        String info
) {
    public ReportSpecRequestDTO {
        Objects.requireNonNull(reportName, "reportName is required.");
    }
}
