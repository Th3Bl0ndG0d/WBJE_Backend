package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps ReportSpec entities to DTOs and vice versa.
 */
@Component
public class ReportSpecDTOMapper {

    public ReportSpecResponseDTO mapToDto(ReportSpecEntity reportSpec) {
        if (reportSpec == null) return null;

        return new ReportSpecResponseDTO(
                reportSpec.getId(),
                reportSpec.getReportName(),
                reportSpec.getReportType(),
                reportSpec.getThickness(),
                reportSpec.getInfo()
        );
    }

    public List<ReportSpecResponseDTO> mapToDto(List<ReportSpecEntity> specs) {
        List<ReportSpecResponseDTO> result = new ArrayList<>();
        if (specs == null) return result;

        for (ReportSpecEntity spec : specs) {
            result.add(mapToDto(spec));
        }
        return result;
    }

    /**
     * Creates a new entity from request input.
     */
    public ReportSpecEntity mapToEntity(ReportSpecRequestDTO dto) {
        if (dto == null) return null;

        ReportSpecEntity entity = new ReportSpecEntity();
        entity.setReportName(dto.reportName());
        entity.setReportType(dto.reportType());
        entity.setThickness(dto.thickness());
        entity.setInfo(dto.info());
        return entity;
    }

    /**
     * Applies request input to an existing entity.
     */
    public void updateEntity(ReportSpecEntity entity, ReportSpecRequestDTO dto) {
        if (entity == null || dto == null) return;

        entity.setReportName(dto.reportName());
        entity.setReportType(dto.reportType());
        entity.setThickness(dto.thickness());
        entity.setInfo(dto.info());
    }
}
