package nl.avflexologic.wbje.services_unitTests;

import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.exceptions.EntityInUseException;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.ReportSpecDTOMapper;
import nl.avflexologic.wbje.repositories.ReportRepository;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import nl.avflexologic.wbje.services.ReportSpecServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ReportSpecServiceImplementation}.
 *
 * The suite focuses on deterministic behavior at service level:
 * - create/read/list/update/delete flows
 * - not-found branches for read/update/delete
 *
 * Tests follow AAA (Arrange, Act, Assert) with concise scenario-oriented naming.
 */
@ExtendWith(MockitoExtension.class)
class ReportSpecServiceImplementationTest {

    @Mock
    private ReportSpecRepository reportSpecRepository;
    @Mock
    private ReportRepository reportRepository;


    @Mock
    private ReportSpecDTOMapper reportSpecDTOMapper;

    @InjectMocks
    private ReportSpecServiceImplementation reportSpecService;

    @Test
    void createReportSpec_validRequest() {
        // Arrange
        ReportSpecRequestDTO request = new ReportSpecRequestDTO(
                "Plate A",
                "Photopolymer",
                67,
                "Default plate spec"
        );

        ReportSpecEntity mapped = mock(ReportSpecEntity.class);
        ReportSpecEntity saved = mock(ReportSpecEntity.class);

        ReportSpecResponseDTO response = new ReportSpecResponseDTO(
                1L,
                "Plate A",
                "Photopolymer",
                67,
                "Default plate spec"
        );

        // Duplicate check must be stubbed
        when(reportSpecRepository.existsByReportName("Plate A"))
                .thenReturn(false);

        when(reportSpecDTOMapper.mapToEntity(request)).thenReturn(mapped);
        when(reportSpecRepository.save(mapped)).thenReturn(saved);
        when(reportSpecDTOMapper.mapToDto(saved)).thenReturn(response);

        ArgumentCaptor<ReportSpecEntity> captor =
                ArgumentCaptor.forClass(ReportSpecEntity.class);

        // Act
        ReportSpecResponseDTO result =
                reportSpecService.createReportSpec(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Plate A", result.reportName());
        assertEquals("Photopolymer", result.reportType());
        assertEquals(67, result.thickness());
        assertEquals("Default plate spec", result.info());

        verify(reportSpecRepository, times(1))
                .existsByReportName("Plate A");

        verify(reportSpecDTOMapper, times(1))
                .mapToEntity(request);

        verify(reportSpecRepository, times(1))
                .save(captor.capture());

        verify(reportSpecDTOMapper, times(1))
                .mapToDto(saved);

        verifyNoMoreInteractions(reportSpecRepository, reportSpecDTOMapper);

        assertSame(mapped, captor.getValue());
    }

    @Test
    void getReportSpecById_found() {
        // Arrange
        Long id = 10L;

        ReportSpecEntity entity = mock(ReportSpecEntity.class);
        when(reportSpecRepository.findById(id)).thenReturn(Optional.of(entity));

        ReportSpecResponseDTO response = new ReportSpecResponseDTO(
                id,
                "Plate X",
                "Type",
                50,
                "Info"
        );
        when(reportSpecDTOMapper.mapToDto(entity)).thenReturn(response);

        // Act
        ReportSpecResponseDTO result = reportSpecService.getReportSpecById(id);

        // Assert
        assertNotNull(result, "A successful read operation must return a response DTO.");
        assertEquals(id, result.id(), "The response id must match the requested identifier.");
        assertEquals("Plate X", result.reportName(), "The response must reflect the mapper output.");

        verify(reportSpecRepository, times(1)).findById(id);
        verify(reportSpecDTOMapper, times(1)).mapToDto(entity);
        verifyNoMoreInteractions(reportSpecRepository, reportSpecDTOMapper);
    }

    @Test
    void getReportSpecById_notFound() {
        // Arrange
        Long id = 404L;
        when(reportSpecRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportSpecService.getReportSpecById(id)
        );

        // Assert
        assertEquals("ReportSpec not found for id: 404", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(reportSpecRepository, times(1)).findById(id);
        verifyNoMoreInteractions(reportSpecRepository);
        verifyNoInteractions(reportSpecDTOMapper);
    }

    @Test
    void getAllReportSpecs_empty() {
        // Arrange
        List<ReportSpecEntity> entities = List.of();
        List<ReportSpecResponseDTO> mapped = List.of();

        when(reportSpecRepository.findAll()).thenReturn(entities);
        when(reportSpecDTOMapper.mapToDto(entities)).thenReturn(mapped);

        // Act
        List<ReportSpecResponseDTO> result = reportSpecService.getAllReportSpecs();

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertTrue(result.isEmpty(), "The list must be empty when the repository returns no entities.");

        verify(reportSpecRepository, times(1)).findAll();
        verify(reportSpecDTOMapper, times(1)).mapToDto(entities);
        verifyNoMoreInteractions(reportSpecRepository, reportSpecDTOMapper);
    }

    @Test
    void getAllReportSpecs_populated() {
        // Arrange
        ReportSpecEntity e1 = mock(ReportSpecEntity.class);
        ReportSpecEntity e2 = mock(ReportSpecEntity.class);

        List<ReportSpecEntity> entities = List.of(e1, e2);

        List<ReportSpecResponseDTO> mapped = List.of(
                new ReportSpecResponseDTO(1L, "A", "T", 10, "I1"),
                new ReportSpecResponseDTO(2L, "B", "T", 12, "I2")
        );

        when(reportSpecRepository.findAll()).thenReturn(entities);
        when(reportSpecDTOMapper.mapToDto(entities)).thenReturn(mapped);

        // Act
        List<ReportSpecResponseDTO> result = reportSpecService.getAllReportSpecs();

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertEquals(2, result.size(), "Each entity must be mapped to exactly one DTO.");
        assertEquals(1L, result.get(0).id(), "The first DTO must reflect the mapper output.");
        assertEquals(2L, result.get(1).id(), "The second DTO must reflect the mapper output.");

        verify(reportSpecRepository, times(1)).findAll();
        verify(reportSpecDTOMapper, times(1)).mapToDto(entities);
        verifyNoMoreInteractions(reportSpecRepository, reportSpecDTOMapper);
    }

    @Test
    void updateReportSpec_notFound() {
        // Arrange
        Long id = 999L;

        ReportSpecRequestDTO request = new ReportSpecRequestDTO(
                "Plate A",
                "Photopolymer",
                67,
                "Default plate spec"
        );

        when(reportSpecRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportSpecService.updateReportSpec(id, request)
        );

        // Assert
        assertEquals("ReportSpec not found for id: 999", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(reportSpecRepository, times(1)).findById(id);
        verifyNoMoreInteractions(reportSpecRepository);
        verifyNoInteractions(reportSpecDTOMapper);
    }

    @Test
    void updateReportSpec_success() {
        // Arrange
        Long id = 10L;

        ReportSpecRequestDTO request = new ReportSpecRequestDTO(
                "Plate B",
                "Rubber",
                80,
                "Updated spec"
        );

        ReportSpecEntity existing = mock(ReportSpecEntity.class);
        ReportSpecEntity saved = mock(ReportSpecEntity.class);

        when(reportSpecRepository.findById(id))
                .thenReturn(Optional.of(existing));

        // 🔹 Duplicate check must be stubbed
        when(reportSpecRepository.existsByReportNameAndIdNot("Plate B", id))
                .thenReturn(false);

        doNothing().when(reportSpecDTOMapper)
                .updateEntity(existing, request);

        when(reportSpecRepository.save(existing))
                .thenReturn(saved);

        ReportSpecResponseDTO response = new ReportSpecResponseDTO(
                id,
                "Plate B",
                "Rubber",
                80,
                "Updated spec"
        );

        when(reportSpecDTOMapper.mapToDto(saved))
                .thenReturn(response);

        // Act
        ReportSpecResponseDTO result =
                reportSpecService.updateReportSpec(id, request);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Plate B", result.reportName());
        assertEquals("Updated spec", result.info());

        verify(reportSpecRepository, times(1))
                .findById(id);

        verify(reportSpecRepository, times(1))
                .existsByReportNameAndIdNot("Plate B", id);

        verify(reportSpecDTOMapper, times(1))
                .updateEntity(existing, request);

        verify(reportSpecRepository, times(1))
                .save(existing);

        verify(reportSpecDTOMapper, times(1))
                .mapToDto(saved);

        verifyNoMoreInteractions(reportSpecRepository, reportSpecDTOMapper);
    }


    @Test
    void deleteReportSpec_notFound() {
        // Arrange
        Long id = 404L;
        when(reportSpecRepository.existsById(id)).thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportSpecService.deleteReportSpec(id)
        );

        // Assert
        assertEquals("ReportSpec not found for id: 404", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(reportSpecRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(reportSpecRepository);
        verifyNoInteractions(reportSpecDTOMapper);
    }
    @Test
    void createReportSpec_whenDuplicateName_throwsIllegalArgumentException() {
        // Arrange
        ReportSpecRequestDTO request =
                new ReportSpecRequestDTO("R1", "Toyobo", 170, "type");

        when(reportSpecRepository.existsByReportName("R1"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> reportSpecService.createReportSpec(request));

        verify(reportSpecRepository).existsByReportName("R1");
        verify(reportSpecRepository, never()).save(any());
    }

    @Test
    void updateReportSpec_whenDuplicateName_throwsIllegalArgumentException() {
        // Arrange
        Long id = 1L;

        ReportSpecEntity existing = new ReportSpecEntity();
        existing.setReportName("OLD");

        ReportSpecRequestDTO request =
                new ReportSpecRequestDTO("NEW", "Toyobo", 170, "type");

        when(reportSpecRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(reportSpecRepository.existsByReportNameAndIdNot("NEW", id))
                .thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> reportSpecService.updateReportSpec(id, request));

        verify(reportSpecRepository).existsByReportNameAndIdNot("NEW", id);
        verify(reportSpecRepository, never()).save(any());
    }

    @Test
    void deleteReportSpec_whenNotFound_throwsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        when(reportSpecRepository.existsById(id))
                .thenReturn(false);

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> reportSpecService.deleteReportSpec(id));

        verify(reportSpecRepository).existsById(id);
        verifyNoInteractions(reportRepository);
    }

    @Test
    void deleteReportSpec_whenInUse_throwsEntityInUseException() {
        // Arrange
        Long id = 1L;

        when(reportSpecRepository.existsById(id))
                .thenReturn(true);

        when(reportRepository.existsByReportSpecId(id))
                .thenReturn(true);

        // Act + Assert
        assertThrows(EntityInUseException.class,
                () -> reportSpecService.deleteReportSpec(id));

        verify(reportSpecRepository).existsById(id);
        verify(reportRepository).existsByReportSpecId(id);
        verify(reportSpecRepository, never()).deleteById(any());
    }

    @Test
    void deleteReportSpec_success() {
        // Arrange
        Long id = 10L;
        when(reportSpecRepository.existsById(id)).thenReturn(true);

        // Act
        reportSpecService.deleteReportSpec(id);

        // Assert
        verify(reportSpecRepository, times(1)).existsById(id);
        verify(reportSpecRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(reportSpecRepository);
        verifyNoInteractions(reportSpecDTOMapper);
    }
}
