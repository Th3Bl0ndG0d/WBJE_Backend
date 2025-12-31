package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.mappers.ReportSpecDTOMapper;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportSpecServiceImplementationTest {

    @Test
    void createReportSpec_saves_and_returns_dto() {
        ReportSpecRepository repository = mock(ReportSpecRepository.class);
        ReportSpecDTOMapper mapper = new ReportSpecDTOMapper();
        ReportSpecService service = new ReportSpecServiceImplementation(repository, mapper);

        ReportSpecEntity saved = new ReportSpecEntity();
        saved.setReportName("Plate A");
        saved.setReportType("Photopolymer");
        saved.setThickness(67);
        saved.setInfo("Default plate spec");

        when(repository.save(any(ReportSpecEntity.class))).thenReturn(saved);

        ReportSpecRequestDTO request = new ReportSpecRequestDTO("Plate A", "Photopolymer", 67, "Default plate spec");
        ReportSpecResponseDTO response = service.createReportSpec(request);

        assertNotNull(response);
        assertEquals("Plate A", response.reportName());
        verify(repository, times(1)).save(any(ReportSpecEntity.class));
    }
}
