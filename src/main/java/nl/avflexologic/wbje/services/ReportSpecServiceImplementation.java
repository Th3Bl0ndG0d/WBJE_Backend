package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.ReportSpecDTOMapper;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements ReportSpec operations using repository access and DTO mapping.
 */
@Service
public class ReportSpecServiceImplementation implements ReportSpecService {

    private final ReportSpecRepository reportSpecRepository;
    private final ReportSpecDTOMapper reportSpecDTOMapper;

    /**
     * Creates a new service instance with required dependencies.
     */
    public ReportSpecServiceImplementation(ReportSpecRepository reportSpecRepository, ReportSpecDTOMapper reportSpecDTOMapper) {
        this.reportSpecRepository = reportSpecRepository;
        this.reportSpecDTOMapper = reportSpecDTOMapper;
    }

    @Override
    public ReportSpecResponseDTO createReportSpec(ReportSpecRequestDTO request) {
        ReportSpecEntity entity = reportSpecDTOMapper.mapToEntity(request);
        ReportSpecEntity saved = reportSpecRepository.save(entity);
        return reportSpecDTOMapper.mapToDto(saved);
    }

    @Override
    public ReportSpecResponseDTO getReportSpecById(Long id) {
        ReportSpecEntity entity = reportSpecRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportSpec not found for id: " + id));
        return reportSpecDTOMapper.mapToDto(entity);
    }

    @Override
    public List<ReportSpecResponseDTO> getAllReportSpecs() {
        return reportSpecDTOMapper.mapToDto(reportSpecRepository.findAll());
    }

    @Override
    public ReportSpecResponseDTO updateReportSpec(Long id, ReportSpecRequestDTO request) {
        ReportSpecEntity existing = reportSpecRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportSpec not found for id: " + id));

        reportSpecDTOMapper.updateEntity(existing, request);

        ReportSpecEntity saved = reportSpecRepository.save(existing);
        return reportSpecDTOMapper.mapToDto(saved);
    }

    @Override
    public void deleteReportSpec(Long id) {
        if (!reportSpecRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReportSpec not found for id: " + id);
        }
        reportSpecRepository.deleteById(id);
    }
}
