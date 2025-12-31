package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;

import java.util.List;

public interface ReportService {

    /** Creates a new report (plate) within a cylinder. */
    ReportResponseDTO createReport(ReportRequestDTO request);

    /** Returns a single report by id. */
    ReportResponseDTO getReportById(Long id);

    /** Returns all reports belonging to a cylinder. */
    List<ReportResponseDTO> getReportsByCylinderId(Long cylinderId);

    /** Updates an existing report. */
    ReportResponseDTO updateReport(Long id, ReportRequestDTO request);

    /** Deletes a report by id. */
    void deleteReport(Long id);
}
