package nl.avflexologic.wbje.dtos.reportSpec;

public record ReportSpecResponseDTO(
        Long id,
        String reportName,
        String reportType,
        Integer thickness,
        String info
) { }
