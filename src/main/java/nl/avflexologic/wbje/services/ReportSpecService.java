package nl.avflexologic.wbje.services;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;

import java.util.List;

public interface ReportSpecService {

    ReportSpecResponseDTO createReportSpec(ReportSpecRequestDTO request);

    ReportSpecResponseDTO getReportSpecById(Long id);

    List<ReportSpecResponseDTO> getAllReportSpecs();

    ReportSpecResponseDTO updateReportSpec(Long id, ReportSpecRequestDTO request);

    void deleteReportSpec(Long id);
}
