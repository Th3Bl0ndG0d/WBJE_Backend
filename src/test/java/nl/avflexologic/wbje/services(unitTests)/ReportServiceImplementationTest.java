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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ReportServiceImplementation}.
 *
 * The test suite is designed for full branch coverage on service level:
 * - successful flows (create/read/list/update/delete)
 * - not-found branches (Cylinder/Report/ReportSpec)
 *
 * The tests follow AAA (Arrange, Act, Assert) and use concise scenario-oriented naming.
 */
@ExtendWith(MockitoExtension.class)
class ReportServiceImplementationTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private CylinderRepository cylinderRepository;

    @Mock
    private ReportSpecRepository reportSpecRepository;

    @Mock
    private ReportDTOMapper reportDTOMapper;

    @InjectMocks
    private ReportServiceImplementation reportService;

    @Test
    void createReport_validRequest() {
        // Arrange
        Long cylinderId = 10L;
        Long reportSpecId = 20L;

        ReportRequestDTO request = new ReportRequestDTO(
                1,
                400,
                10,
                20,
                cylinderId,
                reportSpecId
        );

        CylinderEntity cylinder = mock(CylinderEntity.class);
        ReportSpecEntity reportSpec = mock(ReportSpecEntity.class);

        ReportEntity mapped = mock(ReportEntity.class);
        ReportEntity saved = mock(ReportEntity.class);

        ReportResponseDTO response = new ReportResponseDTO(
                100L,
                1,
                400,
                10,
                20,
                cylinderId,
                reportSpecId
        );

        when(cylinderRepository.findById(cylinderId)).thenReturn(Optional.of(cylinder));
        when(reportSpecRepository.findById(reportSpecId)).thenReturn(Optional.of(reportSpec));
        when(reportDTOMapper.mapToEntity(request, cylinder, reportSpec)).thenReturn(mapped);
        when(reportRepository.save(mapped)).thenReturn(saved);
        when(reportDTOMapper.mapToDto(saved)).thenReturn(response);

        // Act
        ReportResponseDTO result = reportService.createReport(request);

        // Assert
        assertNotNull(result, "A successful create operation must return a response DTO.");
        assertEquals(100L, result.id(), "The returned id must reflect the mapper output.");
        assertEquals(1, result.reportNr(), "reportNr must reflect the request.");
        assertEquals(400, result.reportWidth(), "reportWidth must reflect the request.");
        assertEquals(10, result.xOffset(), "xOffset must reflect the request.");
        assertEquals(20, result.yOffset(), "yOffset must reflect the request.");
        assertEquals(cylinderId, result.cylinderId(), "cylinderId must reflect the request context.");
        assertEquals(reportSpecId, result.reportSpecId(), "reportSpecId must reflect the request.");

        verify(cylinderRepository, times(1)).findById(cylinderId);
        verify(reportSpecRepository, times(1)).findById(reportSpecId);
        verify(reportDTOMapper, times(1)).mapToEntity(request, cylinder, reportSpec);
        verify(reportRepository, times(1)).save(mapped);
        verify(reportDTOMapper, times(1)).mapToDto(saved);
        verifyNoMoreInteractions(cylinderRepository, reportSpecRepository, reportRepository, reportDTOMapper);
    }

    @Test
    void createReport_cylinderMissing() {
        // Arrange
        Long cylinderId = 999L;
        Long reportSpecId = 20L;

        ReportRequestDTO request = new ReportRequestDTO(
                1,
                400,
                10,
                20,
                cylinderId,
                reportSpecId
        );

        when(cylinderRepository.findById(cylinderId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.createReport(request)
        );

        // Assert
        assertEquals("Cylinder not found for id: 999", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findById(cylinderId);
        verifyNoMoreInteractions(cylinderRepository);
        verifyNoInteractions(reportSpecRepository, reportRepository, reportDTOMapper);
    }

    @Test
    void createReport_reportSpecMissing() {
        // Arrange
        Long cylinderId = 10L;
        Long reportSpecId = 404L;

        ReportRequestDTO request = new ReportRequestDTO(
                1,
                400,
                10,
                20,
                cylinderId,
                reportSpecId
        );

        CylinderEntity cylinder = mock(CylinderEntity.class);

        when(cylinderRepository.findById(cylinderId)).thenReturn(Optional.of(cylinder));
        when(reportSpecRepository.findById(reportSpecId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.createReport(request)
        );

        // Assert
        assertEquals("ReportSpec not found for id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findById(cylinderId);
        verify(reportSpecRepository, times(1)).findById(reportSpecId);
        verifyNoMoreInteractions(cylinderRepository, reportSpecRepository);
        verifyNoInteractions(reportRepository, reportDTOMapper);
    }

    @Test
    void getReportById_found() {
        // Arrange
        Long reportId = 10L;

        ReportEntity entity = mock(ReportEntity.class);
        ReportResponseDTO response = new ReportResponseDTO(
                reportId, 1, 400, 10, 20, 10L, 20L
        );

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(entity));
        when(reportDTOMapper.mapToDto(entity)).thenReturn(response);

        // Act
        ReportResponseDTO result = reportService.getReportById(reportId);

        // Assert
        assertNotNull(result, "A successful read operation must return a response DTO.");
        assertEquals(reportId, result.id(), "The DTO id must match the requested id.");

        verify(reportRepository, times(1)).findById(reportId);
        verify(reportDTOMapper, times(1)).mapToDto(entity);
        verifyNoMoreInteractions(reportRepository, reportDTOMapper);
        verifyNoInteractions(cylinderRepository, reportSpecRepository);
    }

    @Test
    void getReportById_notFound() {
        // Arrange
        Long reportId = 404L;
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.getReportById(reportId)
        );

        // Assert
        assertEquals("Report not found for id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(reportRepository, times(1)).findById(reportId);
        verifyNoMoreInteractions(reportRepository);
        verifyNoInteractions(cylinderRepository, reportSpecRepository, reportDTOMapper);
    }

    @Test
    void getReportsByCylinderId_empty() {
        // Arrange
        Long cylinderId = 10L;

        when(reportRepository.findAllByCylinderId(cylinderId)).thenReturn(List.of());
        when(reportDTOMapper.mapToDto(List.of())).thenReturn(List.of());

        // Act
        List<ReportResponseDTO> result = reportService.getReportsByCylinderId(cylinderId);

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertTrue(result.isEmpty(), "The list must be empty when the repository returns no entities.");

        verify(reportRepository, times(1)).findAllByCylinderId(cylinderId);
        verify(reportDTOMapper, times(1)).mapToDto(List.of());
        verifyNoMoreInteractions(reportRepository, reportDTOMapper);
        verifyNoInteractions(cylinderRepository, reportSpecRepository);
    }

    @Test
    void getReportsByCylinderId_populated() {
        // Arrange
        Long cylinderId = 10L;

        ReportEntity e1 = mock(ReportEntity.class);
        ReportEntity e2 = mock(ReportEntity.class);
        List<ReportEntity> entities = List.of(e1, e2);

        List<ReportResponseDTO> mapped = List.of(
                new ReportResponseDTO(1L, 1, 400, 10, 20, cylinderId, 20L),
                new ReportResponseDTO(2L, 2, 450, 15, 25, cylinderId, 20L)
        );

        when(reportRepository.findAllByCylinderId(cylinderId)).thenReturn(entities);
        when(reportDTOMapper.mapToDto(entities)).thenReturn(mapped);

        // Act
        List<ReportResponseDTO> result = reportService.getReportsByCylinderId(cylinderId);

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertEquals(2, result.size(), "Each entity must be mapped to exactly one DTO.");
        assertEquals(1L, result.get(0).id(), "The first DTO must reflect the mapper output.");
        assertEquals(2L, result.get(1).id(), "The second DTO must reflect the mapper output.");

        verify(reportRepository, times(1)).findAllByCylinderId(cylinderId);
        verify(reportDTOMapper, times(1)).mapToDto(entities);
        verifyNoMoreInteractions(reportRepository, reportDTOMapper);
        verifyNoInteractions(cylinderRepository, reportSpecRepository);
    }

    @Test
    void updateReport_notFound() {
        // Arrange
        Long reportId = 999L;

        ReportRequestDTO request = new ReportRequestDTO(
                1, 400, 10, 20, 10L, 20L
        );

        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.updateReport(reportId, request)
        );

        // Assert
        assertEquals("Report not found for id: 999", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(reportRepository, times(1)).findById(reportId);
        verifyNoMoreInteractions(reportRepository);
        verifyNoInteractions(reportSpecRepository, cylinderRepository, reportDTOMapper);
    }

    @Test
    void updateReport_reportSpecMissing() {
        // Arrange
        Long reportId = 10L;
        Long reportSpecId = 404L;

        ReportRequestDTO request = new ReportRequestDTO(
                1, 400, 10, 20, 10L, reportSpecId
        );

        ReportEntity existing = mock(ReportEntity.class);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(existing));
        when(reportSpecRepository.findById(reportSpecId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.updateReport(reportId, request)
        );

        // Assert
        assertEquals("ReportSpec not found for id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(reportRepository, times(1)).findById(reportId);
        verify(reportSpecRepository, times(1)).findById(reportSpecId);
        verifyNoMoreInteractions(reportRepository, reportSpecRepository);
        verifyNoInteractions(cylinderRepository, reportDTOMapper);
    }

    @Test
    void updateReport_success() {
        // Arrange
        Long reportId = 10L;
        Long reportSpecId = 20L;

        ReportRequestDTO request = new ReportRequestDTO(
                2, 450, 15, 25, 10L, reportSpecId
        );

        ReportEntity existing = mock(ReportEntity.class);
        ReportSpecEntity reportSpec = mock(ReportSpecEntity.class);

        ReportEntity saved = mock(ReportEntity.class);

        ReportResponseDTO response = new ReportResponseDTO(
                reportId, 2, 450, 15, 25, 10L, reportSpecId
        );

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(existing));
        when(reportSpecRepository.findById(reportSpecId)).thenReturn(Optional.of(reportSpec));

        // updateEntity is side-effect based; verification is done by interaction checks.
        doNothing().when(reportDTOMapper).updateEntity(existing, request, reportSpec);

        when(reportRepository.save(existing)).thenReturn(saved);
        when(reportDTOMapper.mapToDto(saved)).thenReturn(response);

        // Act
        ReportResponseDTO result = reportService.updateReport(reportId, request);

        // Assert
        assertNotNull(result, "A successful update operation must return a response DTO.");
        assertEquals(reportId, result.id(), "The DTO id must match the updated report id.");
        assertEquals(2, result.reportNr(), "The DTO reportNr must reflect the request.");
        assertEquals(450, result.reportWidth(), "The DTO reportWidth must reflect the request.");
        assertEquals(15, result.xOffset(), "The DTO xOffset must reflect the request.");
        assertEquals(25, result.yOffset(), "The DTO yOffset must reflect the request.");
        assertEquals(10L, result.cylinderId(), "The DTO cylinderId must reflect the request.");
        assertEquals(reportSpecId, result.reportSpecId(), "The DTO reportSpecId must reflect the request.");

        verify(reportRepository, times(1)).findById(reportId);
        verify(reportSpecRepository, times(1)).findById(reportSpecId);
        verify(reportDTOMapper, times(1)).updateEntity(existing, request, reportSpec);
        verify(reportRepository, times(1)).save(existing);
        verify(reportDTOMapper, times(1)).mapToDto(saved);
        verifyNoMoreInteractions(reportRepository, reportSpecRepository, reportDTOMapper);
        verifyNoInteractions(cylinderRepository);
    }

    @Test
    void deleteReport_notFound() {
        // Arrange
        Long reportId = 404L;
        when(reportRepository.existsById(reportId)).thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reportService.deleteReport(reportId)
        );

        // Assert
        assertEquals("Report not found for id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(reportRepository, times(1)).existsById(reportId);
        verifyNoMoreInteractions(reportRepository);
        verifyNoInteractions(cylinderRepository, reportSpecRepository, reportDTOMapper);
    }

    @Test
    void deleteReport_success() {
        // Arrange
        Long reportId = 10L;
        when(reportRepository.existsById(reportId)).thenReturn(true);

        // Act
        reportService.deleteReport(reportId);

        // Assert
        verify(reportRepository, times(1)).existsById(reportId);
        verify(reportRepository, times(1)).deleteById(reportId);
        verifyNoMoreInteractions(reportRepository);
        verifyNoInteractions(cylinderRepository, reportSpecRepository, reportDTOMapper);
    }
}
