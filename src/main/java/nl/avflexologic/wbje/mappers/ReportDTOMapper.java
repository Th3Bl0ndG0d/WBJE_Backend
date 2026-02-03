package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.ReportEntity;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDTOMapper {

    /**
     * Maps a ReportEntity to a ReportResponseDTO.
     * Only ids are exposed for related entities to keep responses stable.
     */
    public ReportResponseDTO mapToDto(ReportEntity report) {
        if (report == null) return null;

        Long cylinderId = (report.getCylinder() != null) ? report.getCylinder().getId() : null;
        Long reportSpecId = (report.getReportSpec() != null) ? report.getReportSpec().getId() : null;

        return new ReportResponseDTO(
                report.getId(),
                report.getReportNr(),
                report.getReportWidth(),
                report.getXOffset(),
                report.getYOffset(),
                cylinderId,
                reportSpecId
        );
    }

    /**
     * Maps a list of entities to response DTOs.
     */
    public List<ReportResponseDTO> mapToDto(List<ReportEntity> reports) {
        List<ReportResponseDTO> result = new ArrayList<>();
        if (reports == null) return result;

        for (ReportEntity report : reports) {
            result.add(mapToDto(report));
        }
        return result;
    }

    public ReportEntity mapToEntity(ReportRequestDTO dto) {
        throw new UnsupportedOperationException(
                "Report mapping requires a CylinderEntity and ReportSpecEntity. Use mapToEntity(dto, cylinder, reportSpec)."
        );
    }

    /**
     * Creates a ReportEntity based on request input and required parent references.
     * Cylinder and ReportSpec are mandatory in the domain model.
     */
    public ReportEntity mapToEntity(ReportRequestDTO dto, CylinderEntity cylinder, ReportSpecEntity reportSpec) {
        if (dto == null) return null;

        ReportEntity report = new ReportEntity(cylinder, reportSpec, dto.reportNr());
        report.setReportWidth(dto.reportWidth());
        report.setXOffset(dto.xOffset());
        report.setYOffset(dto.yOffset());
        return report;
    }

    /**
     * Applies request input to an existing entity.
     * Cylinder ownership is not reassigned here; it is managed by the Cylinder aggregate.
     */
    public void updateEntity(ReportEntity report, ReportRequestDTO dto, ReportSpecEntity reportSpec) {
        if (report == null || dto == null) return;

        report.setReportNr(dto.reportNr());
        report.setReportWidth(dto.reportWidth());
        report.setXOffset(dto.xOffset());
        report.setYOffset(dto.yOffset());
        report.setReportSpec(reportSpec);
    }
}
