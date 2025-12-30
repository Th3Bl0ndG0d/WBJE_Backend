package nl.avflexologic.wbje.services;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void createCylinder_creates_and_returns_response() {
        Long jobId = 1L;
        Long tapeSpecId = 10L;

        CylinderRequestDTO request = new CylinderRequestDTO(
                1,
                "Cyan",
                "Cylinder info",
                tapeSpecId
        );

        JobEntity job = new JobEntity(java.time.LocalDateTime.now());
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        CylinderEntity mappedEntity = mock(CylinderEntity.class);
        CylinderEntity savedEntity = mock(CylinderEntity.class);

        CylinderResponseDTO response = new CylinderResponseDTO(
                100L,
                1,
                "Cyan",
                "Cylinder info",
                jobId,
                tapeSpecId
        );

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(tapeSpecRepository.findById(tapeSpecId)).thenReturn(Optional.of(tapeSpec));
        when(cylinderDTOMapper.mapToEntity(request, job, tapeSpec)).thenReturn(mappedEntity);
        when(cylinderRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(cylinderDTOMapper.mapToDto(savedEntity)).thenReturn(response);

        CylinderResponseDTO result = cylinderService.createCylinder(jobId, request);

        assertNotNull(result);
        assertEquals(100L, result.id());
        assertEquals(1, result.cylinderNr());
        assertEquals(jobId, result.jobId());
        assertEquals(tapeSpecId, result.tapeSpecId());

        verify(jobRepository).findById(jobId);
        verify(tapeSpecRepository).findById(tapeSpecId);
        verify(cylinderRepository).save(mappedEntity);
        verify(cylinderDTOMapper).mapToDto(savedEntity);
    }

    @Test
    void createCylinder_throws_when_job_not_found() {
        Long jobId = 999L;

        CylinderRequestDTO request = new CylinderRequestDTO(
                1,
                "Cyan",
                null,
                10L
        );

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> cylinderService.createCylinder(jobId, request)
        );

        assertTrue(ex.getMessage().contains("Job not found"));

        verify(jobRepository).findById(jobId);
        verifyNoInteractions(tapeSpecRepository);
        verifyNoInteractions(cylinderRepository);
        verifyNoInteractions(cylinderDTOMapper);
    }
}
