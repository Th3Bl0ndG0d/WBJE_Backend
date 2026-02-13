package nl.avflexologic.wbje.services_unitTests;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.repositories.JobRepository;
import nl.avflexologic.wbje.services.JobServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JobServiceImplementation}.
 *
 * The tests follow the AAA pattern (Arrange, Act, Assert) and focus on
 * deterministic validation of both successful and failing execution paths.
 *
 * The naming convention is intentionally concise and scenario-oriented,
 * which is common in manually maintained service-layer test suites.
 */
@ExtendWith(MockitoExtension.class)
class JobServiceImplementationTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobServiceImplementation jobService;

    @Test
    void createJob_nullRequest_throwsException() {
        // Arrange
        // No setup is required because the service validates input immediately.

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jobService.createJob(null)
        );

        // Assert
        assertEquals("Job request must not be null.", exception.getMessage(),
                "Input validation must remain explicit and deterministic.");
        verifyNoInteractions(jobRepository);
    }

    @Test
    void createJob_validRequest() {
        // Arrange
        JobRequestDTO request = buildValidRequest();

        /*
         * The service assigns persistence responsibilities to the repository.
         * Therefore, the repository stub is expected to return the same entity instance,
         * enriched with an identifier, to emulate typical JPA behavior.
         */
        when(jobRepository.save(any(JobEntity.class))).thenAnswer(invocation -> {
            JobEntity entity = invocation.getArgument(0, JobEntity.class);
            entity.setId(1L);
            return entity;
        });

        ArgumentCaptor<JobEntity> entityCaptor = ArgumentCaptor.forClass(JobEntity.class);

        // Act
        JobResponseDTO response = jobService.createJob(request);

        // Assert
        verify(jobRepository, times(1)).save(entityCaptor.capture());
        verifyNoMoreInteractions(jobRepository);

        JobEntity savedEntity = entityCaptor.getValue();
        assertNotNull(savedEntity, "The service must persist a non-null entity instance.");

        // Entity-level checks validate request -> entity mapping.
        assertEquals(request.getJobNumber(), savedEntity.getJobNumber(), "jobNumber must be mapped onto the entity.");
        assertEquals(request.getJobDate(), savedEntity.getJobDate(), "jobDate must be mapped onto the entity.");
        assertEquals(request.getJobName(), savedEntity.getJobName(), "jobName must be mapped onto the entity.");
        assertEquals(request.getCylinderWidth(), savedEntity.getCylinderWidth(), "cylinderWidth must be mapped onto the entity.");
        assertEquals(request.getCylinderCircumference(), savedEntity.getCylinderCircumference(), "cylinderCircumference must be mapped onto the entity.");
        assertEquals(request.getInfo(), savedEntity.getInfo(), "info must be mapped onto the entity.");

        // DTO-level checks validate entity -> response mapping (DTO uses public fields, not getters).
        assertNotNull(response, "A successful create operation must return a response DTO.");
        assertEquals(1L, response.id, "The response id must reflect the persisted identifier.");
        assertEquals(request.getJobNumber(), response.jobNumber, "The response jobNumber must reflect the persisted entity.");
        assertEquals(request.getJobDate(), response.jobDate, "The response jobDate must reflect the persisted entity.");
        assertEquals(request.getJobName(), response.jobName, "The response jobName must reflect the persisted entity.");
        assertEquals(request.getCylinderWidth(), response.cylinderWidth, "The response cylinderWidth must reflect the persisted entity.");
        assertEquals(request.getCylinderCircumference(), response.cylinderCircumference, "The response cylinderCircumference must reflect the persisted entity.");
        assertEquals(request.getInfo(), response.info, "The response info must reflect the persisted entity.");
        assertEquals(request.getNoteInfo(), response.noteInfo, "The response noteInfo must reflect the mapped note information.");
    }

    @Test
    void getJobById_found() {
        // Arrange
        JobEntity entity = buildEntity(
                10L,
                "JOB-10",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                "Example job",
                850,
                1300,
                "Info"
        );
        when(jobRepository.findById(10L)).thenReturn(Optional.of(entity));

        // Act
        JobResponseDTO response = jobService.getJobById(10L);

        // Assert
        verify(jobRepository, times(1)).findById(10L);
        verifyNoMoreInteractions(jobRepository);

        assertNotNull(response, "The service must return a DTO if the entity exists.");
        assertEquals(10L, response.id, "The DTO id must match the entity id.");
        assertEquals("JOB-10", response.jobNumber, "The DTO jobNumber must match the entity jobNumber.");
        assertEquals(entity.getJobDate(), response.jobDate, "The DTO jobDate must match the entity jobDate.");
    }

    @Test
    void getJobById_notFound() {
        // Arrange
        when(jobRepository.findById(404L)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.getJobById(404L)
        );

        // Assert
        verify(jobRepository, times(1)).findById(404L);
        verifyNoMoreInteractions(jobRepository);

        assertEquals("Job not found for id: 404", exception.getMessage(),
                "The exception message must remain stable for reliable API error handling.");
    }

    @Test
    void getAllJobs_empty() {
        // Arrange
        when(jobRepository.findAll()).thenReturn(List.of());

        // Act
        List<JobResponseDTO> result = jobService.getAllJobs();

        // Assert
        verify(jobRepository, times(1)).findAll();
        verifyNoMoreInteractions(jobRepository);

        assertNotNull(result, "The service must never return null lists.");
        assertTrue(result.isEmpty(), "The list must be empty when no entities exist.");
    }

    @Test
    void getAllJobs_populated() {
        // Arrange
        JobEntity entity1 = buildEntity(
                1L,
                "JOB-1",
                LocalDateTime.of(2025, 1, 1, 8, 0),
                "Job 1",
                800,
                1200,
                "Info 1"
        );
        JobEntity entity2 = buildEntity(
                2L,
                "JOB-2",
                LocalDateTime.of(2025, 1, 2, 9, 0),
                "Job 2",
                850,
                1300,
                "Info 2"
        );

        when(jobRepository.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<JobResponseDTO> result = jobService.getAllJobs();

        // Assert
        verify(jobRepository, times(1)).findAll();
        verifyNoMoreInteractions(jobRepository);

        assertNotNull(result, "The service must never return null lists.");
        assertEquals(2, result.size(), "Each entity must be mapped to exactly one DTO.");

        assertEquals(1L, result.get(0).id, "The first DTO must reflect the first entity id.");
        assertEquals("JOB-1", result.get(0).jobNumber, "The first DTO must reflect the first entity jobNumber.");
        assertEquals(2L, result.get(1).id, "The second DTO must reflect the second entity id.");
        assertEquals("JOB-2", result.get(1).jobNumber, "The second DTO must reflect the second entity jobNumber.");
    }

    @Test
    void updateJob_nullRequest_throwsException() {
        // Arrange
        // No setup is required because the service validates input immediately.

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jobService.updateJob(1L, null)
        );

        // Assert
        assertEquals("Job request must not be null.", exception.getMessage(),
                "Input validation must remain explicit and deterministic.");
        verifyNoInteractions(jobRepository);
    }

    @Test
    void updateJob_notFound() {
        // Arrange
        JobRequestDTO request = buildValidRequest();
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.updateJob(999L, request)
        );

        // Assert
        verify(jobRepository, times(1)).findById(999L);
        verify(jobRepository, never()).save(any(JobEntity.class));
        verifyNoMoreInteractions(jobRepository);

        assertEquals("Job not found for id: 999", exception.getMessage(),
                "The exception message must remain stable for reliable API error handling.");
    }

    @Test
    void updateJob_success() {
        // Arrange
        JobEntity existing = buildEntity(
                5L,
                "JOB-OLD",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                "Old name",
                800,
                1200,
                "Old info"
        );

        JobRequestDTO request = new JobRequestDTO(LocalDateTime.of(2025, 2, 2, 12, 0));
        request.setJobNumber("JOB-NEW");
        request.setJobDate(LocalDateTime.of(2025, 2, 2, 12, 0));
        request.setJobName("New name");
        request.setCylinderWidth(850);
        request.setCylinderCircumference(1300);
        request.setInfo("New info");
        request.setNoteInfo("New note");

        when(jobRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(jobRepository.save(any(JobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0, JobEntity.class));

        ArgumentCaptor<JobEntity> entityCaptor = ArgumentCaptor.forClass(JobEntity.class);

        // Act
        JobResponseDTO response = jobService.updateJob(5L, request);

        // Assert
        verify(jobRepository, times(1)).findById(5L);
        verify(jobRepository, times(1)).save(entityCaptor.capture());
        verifyNoMoreInteractions(jobRepository);

        JobEntity saved = entityCaptor.getValue();
        assertNotNull(saved, "The service must save a non-null entity instance.");
        assertSame(existing, saved, "The service is expected to update the loaded entity instance in-place.");

        // Entity-level checks validate update mapping.
        assertEquals("JOB-NEW", saved.getJobNumber(), "jobNumber must be updated from the request.");
        assertEquals(request.getJobDate(), saved.getJobDate(), "jobDate must be updated from the request.");
        assertEquals("New name", saved.getJobName(), "jobName must be updated from the request.");
        assertEquals(850, saved.getCylinderWidth(), "cylinderWidth must be updated from the request.");
        assertEquals(1300, saved.getCylinderCircumference(), "cylinderCircumference must be updated from the request.");
        assertEquals("New info", saved.getInfo(), "info must be updated from the request.");

        // DTO-level checks validate returned mapping (DTO uses public fields).
        assertNotNull(response, "A successful update operation must return a response DTO.");
        assertEquals(5L, response.id, "The response id must preserve the existing entity identifier.");
        assertEquals("JOB-NEW", response.jobNumber, "The response jobNumber must reflect the updated value.");
        assertEquals(request.getJobDate(), response.jobDate, "The response jobDate must reflect the updated value.");
        assertEquals("New name", response.jobName, "The response jobName must reflect the updated value.");
        assertEquals(850, response.cylinderWidth, "The response cylinderWidth must reflect the updated value.");
        assertEquals(1300, response.cylinderCircumference, "The response cylinderCircumference must reflect the updated value.");
        assertEquals("New info", response.info, "The response info must reflect the updated value.");
        assertEquals("New note", response.noteInfo, "The response noteInfo must reflect the updated note mapping.");
    }

    @Test
    void deleteJob_found() {
        // Arrange
        JobEntity entity = buildEntity(
                7L,
                "JOB-7",
                LocalDateTime.of(2025, 3, 3, 10, 0),
                "Job 7",
                800,
                1200,
                "Info 7"
        );
        when(jobRepository.findById(7L)).thenReturn(Optional.of(entity));

        // Act
        jobService.deleteJob(7L);

        // Assert
        verify(jobRepository, times(1)).findById(7L);
        verify(jobRepository, times(1)).delete(entity);
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void deleteJob_notFound() {
        // Arrange
        when(jobRepository.findById(123L)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> jobService.deleteJob(123L)
        );

        // Assert
        verify(jobRepository, times(1)).findById(123L);
        verify(jobRepository, never()).delete(any(JobEntity.class));
        verifyNoMoreInteractions(jobRepository);

        assertEquals("Job not found for id: 123", exception.getMessage(),
                "The exception message must remain stable for reliable API error handling.");
    }

    @Test
    void getJobById_whenNotFound_throwsException() {
        // Arrange
        long id = 999L;
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> jobService.getJobById(id));

        verify(jobRepository).findById(id);
    }

    @Test
    void deleteJob_whenNotFound_throwsException() {
        // Arrange
        long id = 999L;
        when(jobRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> jobService.deleteJob(id));

        verify(jobRepository).findById(id);
        verify(jobRepository, never()).delete(any());
    }
    @Test
    void createJob_whenRequestIsNull_throwsIllegalArgumentException() {
        // Arrange
        JobRequestDTO request = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> jobService.createJob(request));

        verifyNoInteractions(jobRepository);
    }

    @Test
    void updateJob_whenRequestIsNull_throwsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        JobRequestDTO request = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> jobService.updateJob(id, request));

        verifyNoInteractions(jobRepository);
    }



    @Test
    void deleteJob_whenExists_deletesEntity() {
        // Arrange
        long id = 1L;

        JobEntity existing = new JobEntity(LocalDateTime.now());
        existing.setJobNumber("JOB-1");

        when(jobRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act
        jobService.deleteJob(id);

        // Assert
        verify(jobRepository).findById(id);
        verify(jobRepository).delete(existing);
    }






    // -------------------------------------------------------------------------
    // Test utilities
    // -------------------------------------------------------------------------

    private static JobRequestDTO buildValidRequest() {
        /*
         * Centralized test data creation reduces duplication and keeps each test focused
         * on behavioral intent rather than repetitive object initialization.
         */
        LocalDateTime date = LocalDateTime.of(2025, 12, 22, 10, 0);

        JobRequestDTO request = new JobRequestDTO(date);
        request.setJobNumber("JOB-2025-001");
        request.setJobDate(date);
        request.setJobName("Print job for customer X – 4 colors");
        request.setCylinderWidth(850);
        request.setCylinderCircumference(1300);
        request.setInfo("Operator note: validate substrate batch before start.");
        request.setNoteInfo("Repeat run, verify plate wear on station 2.");

        return request;
    }

    private static JobEntity buildEntity(
            Long id,
            String jobNumber,
            LocalDateTime jobDate,
            String jobName,
            Integer cylinderWidth,
            Integer cylinderCircumference,
            String info
    ) {
        /*
         * The entity constructor requires a non-null jobDate by design.
         * Additional fields are populated via setters to match the domain structure.
         */
        JobEntity entity = new JobEntity(jobDate);
        entity.setId(id);
        entity.setJobNumber(jobNumber);
        entity.setJobName(jobName);
        entity.setCylinderWidth(cylinderWidth);
        entity.setCylinderCircumference(cylinderCircumference);
        entity.setInfo(info);
        return entity;
    }
}
