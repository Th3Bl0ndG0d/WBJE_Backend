package nl.avflexologic.wbje.services_unitTests;

import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.CylinderDTOMapper;
import nl.avflexologic.wbje.repositories.CylinderRepository;
import nl.avflexologic.wbje.repositories.JobRepository;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import nl.avflexologic.wbje.services.CylinderServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CylinderServiceImplementation}.
 *
 * The tests follow the AAA pattern (Arrange, Act, Assert) and cover:
 * - validation and not-found branches (Job / Cylinder / TapeSpec)
 * - successful create/read/update/delete flows
 * - expected repository and mapper interactions
 */
@ExtendWith(MockitoExtension.class)
class CylinderServiceImplementationTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CylinderRepository cylinderRepository;

    @Mock
    private TapeSpecRepository tapeSpecRepository;

    @Mock
    private CylinderDTOMapper cylinderDTOMapper;

    @InjectMocks
    private CylinderServiceImplementation cylinderService;

    @Test
    void createCylinder_validRequest() {
        // Arrange
        Long jobId = 1L;
        Long tapeSpecId = 10L;

        CylinderRequestDTO request = new CylinderRequestDTO(
                1,
                "Cyan",
                "Cylinder info",
                tapeSpecId
        );

        /*
         * A spy is used to validate that the service manipulates the aggregate root (Job) as intended.
         * The production code adds the cylinder via Job to keep the association consistent.
         */
        JobEntity job = spy(new JobEntity(LocalDateTime.now()));
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        CylinderEntity mappedCylinder = mock(CylinderEntity.class);
        CylinderEntity savedCylinder = mock(CylinderEntity.class);



        CylinderResponseDTO response = new CylinderResponseDTO(
                100L,
                1,
                "Cyan",
                "Cylinder info",
                jobId,
                tapeSpecId,
                null
        );


        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(tapeSpecRepository.findById(tapeSpecId)).thenReturn(Optional.of(tapeSpec));
        when(cylinderDTOMapper.mapToEntity(request, job, tapeSpec)).thenReturn(mappedCylinder);
        when(cylinderRepository.save(mappedCylinder)).thenReturn(savedCylinder);
        when(cylinderDTOMapper.mapToDto(savedCylinder)).thenReturn(response);

        // Act
        CylinderResponseDTO result = cylinderService.createCylinder(jobId, request);

        // Assert
        assertNotNull(result, "A successful create operation must return a response DTO.");
        assertEquals(100L, result.id(), "The returned id must match the mapped response.");
        assertEquals(1, result.cylinderNr(), "The returned cylinderNr must match the mapped response.");
        assertEquals(jobId, result.jobId(), "The returned jobId must match the request context.");
        assertEquals(tapeSpecId, result.tapeSpecId(), "The returned tapeSpecId must match the request.");

        /*
         * The job aggregate is expected to be updated via addCylinder() before persisting.
         * This verifies that the service enforces aggregate consistency (bidirectional relation).
         */
        verify(job, times(1)).addCylinder(mappedCylinder);

        verify(jobRepository, times(1)).findById(jobId);
        verify(tapeSpecRepository, times(1)).findById(tapeSpecId);
        verify(cylinderDTOMapper, times(1)).mapToEntity(request, job, tapeSpec);
        verify(cylinderRepository, times(1)).save(mappedCylinder);
        verify(cylinderDTOMapper, times(1)).mapToDto(savedCylinder);
        verifyNoMoreInteractions(jobRepository, tapeSpecRepository, cylinderRepository, cylinderDTOMapper);
    }

    @Test
    void createCylinder_jobMissing() {
        // Arrange
        Long jobId = 999L;
        CylinderRequestDTO request = new CylinderRequestDTO(1, "Cyan", null, 10L);

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.createCylinder(jobId, request)
        );

        // Assert
        assertEquals("Job not found with id: 999", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(jobRepository, times(1)).findById(jobId);
        verifyNoInteractions(tapeSpecRepository, cylinderRepository, cylinderDTOMapper);
    }

    @Test
    void createCylinder_tapeSpecMissing() {
        // Arrange
        Long jobId = 1L;
        Long tapeSpecId = 404L;

        CylinderRequestDTO request = new CylinderRequestDTO(1, "Cyan", "Info", tapeSpecId);
        JobEntity job = new JobEntity(LocalDateTime.now());

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(tapeSpecRepository.findById(tapeSpecId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.createCylinder(jobId, request)
        );

        // Assert
        assertEquals("TapeSpec not found with id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(jobRepository, times(1)).findById(jobId);
        verify(tapeSpecRepository, times(1)).findById(tapeSpecId);
        verifyNoInteractions(cylinderRepository, cylinderDTOMapper);
        verifyNoMoreInteractions(jobRepository, tapeSpecRepository);
    }

    @Test
    void getCylinderById_found() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 10L;

        CylinderEntity cylinder = mock(CylinderEntity.class);

        CylinderResponseDTO response = new CylinderResponseDTO(
                cylinderId,
                1,
                "Magenta",
                "Info",
                jobId,
                10L,
                null
        );

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.of(cylinder));
        when(cylinderDTOMapper.mapToDto(cylinder)).thenReturn(response);

        // Act
        CylinderResponseDTO result = cylinderService.getCylinderById(jobId, cylinderId);

        // Assert
        assertNotNull(result, "A successful read operation must return a response DTO.");
        assertEquals(cylinderId, result.id(), "The response id must match the requested cylinder id.");
        assertEquals(jobId, result.jobId(), "The response jobId must match the requested job context.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verify(cylinderDTOMapper, times(1)).mapToDto(cylinder);
        verifyNoMoreInteractions(cylinderRepository, cylinderDTOMapper);
        verifyNoInteractions(jobRepository, tapeSpecRepository);
    }

    @Test
    void getCylinderById_notFound() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 404L;

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.getCylinderById(jobId, cylinderId)
        );

        // Assert
        assertEquals("Cylinder not found with id: 404 for job: 1", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verifyNoMoreInteractions(cylinderRepository);
        verifyNoInteractions(jobRepository, tapeSpecRepository, cylinderDTOMapper);
    }

    @Test
    void getCylindersByJob_jobMissing() {
        // Arrange
        Long jobId = 123L;
        when(jobRepository.existsById(jobId)).thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.getCylindersByJob(jobId)
        );

        // Assert
        assertEquals("Job not found with id: 123", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(jobRepository, times(1)).existsById(jobId);
        verifyNoMoreInteractions(jobRepository);
        verifyNoInteractions(cylinderRepository, tapeSpecRepository, cylinderDTOMapper);
    }

    @Test
    void getCylindersByJob_populated() {
        // Arrange
        Long jobId = 1L;

        List<CylinderEntity> cylinders = List.of(mock(CylinderEntity.class), mock(CylinderEntity.class));
        List<CylinderResponseDTO> mapped = List.of(
                new CylinderResponseDTO(1L, 1, "Cyan", "Info 1", jobId, 10L,null),
                new CylinderResponseDTO(2L, 2, "Magenta", "Info 2", jobId, 10L,null)
        );

        when(jobRepository.existsById(jobId)).thenReturn(true);
        when(cylinderRepository.findAllByJob_Id(jobId)).thenReturn(cylinders);
        when(cylinderDTOMapper.mapToDto(cylinders)).thenReturn(mapped);

        // Act
        List<CylinderResponseDTO> result = cylinderService.getCylindersByJob(jobId);

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertEquals(2, result.size(), "Each entity must be mapped to exactly one DTO.");
        assertEquals(1L, result.get(0).id(), "The first mapped DTO must reflect the mapper output.");
        assertEquals(2L, result.get(1).id(), "The second mapped DTO must reflect the mapper output.");

        verify(jobRepository, times(1)).existsById(jobId);
        verify(cylinderRepository, times(1)).findAllByJob_Id(jobId);
        verify(cylinderDTOMapper, times(1)).mapToDto(cylinders);
        verifyNoMoreInteractions(jobRepository, cylinderRepository, cylinderDTOMapper);
        verifyNoInteractions(tapeSpecRepository);
    }

    @Test
    void updateCylinder_notFound() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 999L;

        CylinderRequestDTO request = new CylinderRequestDTO(1, "Cyan", "Info", 10L);

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.updateCylinder(jobId, cylinderId, request)
        );

        // Assert
        assertEquals("Cylinder not found with id: 999 for job: 1", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verifyNoMoreInteractions(cylinderRepository);
        verifyNoInteractions(jobRepository, tapeSpecRepository, cylinderDTOMapper);
    }

    @Test
    void updateCylinder_tapeSpecMissing() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 10L;
        Long tapeSpecId = 404L;

        CylinderRequestDTO request = new CylinderRequestDTO(1, "Cyan", "Info", tapeSpecId);

        CylinderEntity cylinder = mock(CylinderEntity.class);

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.of(cylinder));
        when(tapeSpecRepository.findById(tapeSpecId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.updateCylinder(jobId, cylinderId, request)
        );

        // Assert
        assertEquals("TapeSpec not found with id: 404", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verify(tapeSpecRepository, times(1)).findById(tapeSpecId);
        verifyNoMoreInteractions(cylinderRepository, tapeSpecRepository);
        verifyNoInteractions(jobRepository, cylinderDTOMapper);
    }

    @Test
    void updateCylinder_success() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 10L;
        Long tapeSpecId = 10L;

        CylinderRequestDTO request = new CylinderRequestDTO(2, "Black", "Updated", tapeSpecId);

        CylinderEntity cylinder = mock(CylinderEntity.class);
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        CylinderEntity saved = mock(CylinderEntity.class);

        CylinderResponseDTO response = new CylinderResponseDTO(
                cylinderId,
                2,
                "Black",
                "Updated",
                jobId,
                tapeSpecId,
                null
        );

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.of(cylinder));
        when(tapeSpecRepository.findById(tapeSpecId)).thenReturn(Optional.of(tapeSpec));

        // updateEntity has side effects only; it is validated through interaction verification.
        doNothing().when(cylinderDTOMapper).updateEntity(cylinder, request, tapeSpec);

        when(cylinderRepository.save(cylinder)).thenReturn(saved);
        when(cylinderDTOMapper.mapToDto(saved)).thenReturn(response);

        // Act
        CylinderResponseDTO result = cylinderService.updateCylinder(jobId, cylinderId, request);

        // Assert
        assertNotNull(result, "A successful update operation must return a response DTO.");
        assertEquals(cylinderId, result.id(), "The response id must match the requested cylinder id.");
        assertEquals(2, result.cylinderNr(), "The updated cylinderNr must reflect the request.");
        assertEquals("Black", result.color(), "The updated color must reflect the request.");
        assertEquals("Updated", result.cylinderInfo(), "The updated cylinderInfo must reflect the request.");
        assertEquals(jobId, result.jobId(), "The response must preserve the job context.");
        assertEquals(tapeSpecId, result.tapeSpecId(), "The response must preserve the TapeSpec reference.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verify(tapeSpecRepository, times(1)).findById(tapeSpecId);
        verify(cylinderDTOMapper, times(1)).updateEntity(cylinder, request, tapeSpec);
        verify(cylinderRepository, times(1)).save(cylinder);
        verify(cylinderDTOMapper, times(1)).mapToDto(saved);
        verifyNoMoreInteractions(cylinderRepository, tapeSpecRepository, cylinderDTOMapper);
        verifyNoInteractions(jobRepository);
    }

    @Test
    void deleteCylinder_notFound() {
        // Arrange
        Long jobId = 1L;
        Long cylinderId = 404L;

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.deleteCylinder(jobId, cylinderId)
        );

        // Assert
        assertEquals("Cylinder not found with id: 404 for job: 1", exception.getMessage(),
                "The error message must remain stable for consistent API error handling.");

        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verifyNoMoreInteractions(cylinderRepository);
        verifyNoInteractions(jobRepository, tapeSpecRepository, cylinderDTOMapper);
    }

    @Test
    void deleteCylinder_success() {
        Long jobId = 1L;
        Long cylinderId = 10L;

        JobEntity job = spy(new JobEntity(LocalDateTime.now()));
        CylinderEntity cylinder = mock(CylinderEntity.class);

        when(cylinderRepository.findByIdAndJob_Id(cylinderId, jobId))
                .thenReturn(Optional.of(cylinder));

        when(cylinder.getJob()).thenReturn(job);

        job.addCylinder(cylinder);

        // Act
        cylinderService.deleteCylinder(jobId, cylinderId);

        // Assert
        verify(cylinderRepository, times(1)).findByIdAndJob_Id(cylinderId, jobId);
        verify(job, times(1)).removeCylinder(cylinder);
        verify(cylinderRepository, times(1)).delete(cylinder);
        verifyNoMoreInteractions(cylinderRepository);
        verifyNoInteractions(jobRepository, tapeSpecRepository, cylinderDTOMapper);
    }

}
