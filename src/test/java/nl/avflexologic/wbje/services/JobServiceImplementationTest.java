package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.repositories.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests.
 */
@ExtendWith(MockitoExtension.class)
public class JobServiceImplementationTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobServiceImplementation jobService;

    @Test
    void createJob_saves_job_and_returns_response() {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2025, 12, 22, 10, 0);

        JobRequestDTO request = new JobRequestDTO(date);
        request.jobDate = date;

        JobEntity savedEntity = new JobEntity(date);
        savedEntity.setId(1L);

        when(jobRepository.save(any(JobEntity.class))).thenReturn(savedEntity);

        // Act
        JobResponseDTO response = jobService.createJob(request);

        // Assert
        verify(jobRepository).save(any(JobEntity.class));
        assertNotNull(response);
        assertEquals(1L, response.id);
        assertEquals(date, response.jobDate);
    }

//    @Test
//    void createJob_throws_exception_when_jobDate_is_missing() {
//        // Arrange
//        JobRequestDTO request = new JobRequestDTO(null);
//        request.jobDate = null;
//
//        // Act
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> jobService.createJob(request)
//        );
//
//        // Assert
//        assertEquals("jobDate is required.", ex.getMessage());
//        verifyNoInteractions(jobRepository);
//    }
//
//    @Test
//    void createJob_throws_exception_when_request_is_null() {
//        // Act
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> jobService.createJob(null)
//        );
//
//        // Assert
//        assertEquals("Job request must not be null.", ex.getMessage());
//        verifyNoInteractions(jobRepository);
//    }
}
