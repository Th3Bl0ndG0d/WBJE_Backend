package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.ReportEntity;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.ReportDTOMapper;
import nl.avflexologic.wbje.repositories.CylinderRepository;
import nl.avflexologic.wbje.repositories.ReportRepository;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements Report operations using repository access and DTO mapping.
 */
@Service
public class ReportServiceImplementation implements ReportService {

    private final ReportRepository reportRepository;
    private final CylinderRepository cylinderRepository;
    private final ReportSpecRepository reportSpecRepository;
    private final ReportDTOMapper reportDTOMapper;

    /**
     * Creates a new service instance with required dependencies.
     */
    public ReportServiceImplementation(
            ReportRepository reportRepository,
            CylinderRepository cylinderRepository,
            ReportSpecRepository reportSpecRepository,
            ReportDTOMapper reportDTOMapper
    ) {
        this.reportRepository = reportRepository;
        this.cylinderRepository = cylinderRepository;
        this.reportSpecRepository = reportSpecRepository;
        this.reportDTOMapper = reportDTOMapper;
    }

    /**
     * Creates and persists a new Report within a given cylinder.
     */
    @Override
    public ReportResponseDTO createReport(ReportRequestDTO request) {
        CylinderEntity cylinder = cylinderRepository.findById(request.cylinderId())
                .orElseThrow(() -> new ResourceNotFoundException("Cylinder not found for id: " + request.cylinderId()));

        ReportSpecEntity reportSpec = reportSpecRepository.findById(request.reportSpecId())
                .orElseThrow(() -> new ResourceNotFoundException("ReportSpec not found for id: " + request.reportSpecId()));

        ReportEntity entity = reportDTOMapper.mapToEntity(request, cylinder, reportSpec);
        ReportEntity saved = reportRepository.save(entity);

        return reportDTOMapper.mapToDto(saved);
    }

    /**
     * Returns a single Report by id.
     */
    @Override
    public ReportResponseDTO getReportById(Long id) {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for id: " + id));

        return reportDTOMapper.mapToDto(report);
    }

    /**
     * Returns all reports for a given cylinder.
     */
    @Override
    public List<ReportResponseDTO> getReportsByCylinderId(Long cylinderId) {
        return reportDTOMapper.mapToDto(reportRepository.findAllByCylinderId(cylinderId));
    }

    /**
     * Updates an existing Report.
     */
    @Override
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO request) {
        ReportEntity existing = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for id: " + id));

        ReportSpecEntity reportSpec = reportSpecRepository.findById(request.reportSpecId())
                .orElseThrow(() -> new ResourceNotFoundException("ReportSpec not found for id: " + request.reportSpecId()));

        reportDTOMapper.updateEntity(existing, request, reportSpec);

        ReportEntity saved = reportRepository.save(existing);
        return reportDTOMapper.mapToDto(saved);
    }

    /**
     * Deletes a Report by id.
     */
    @Override
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found for id: " + id);
        }
        reportRepository.deleteById(id);
    }
}
