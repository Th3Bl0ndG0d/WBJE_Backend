package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.mappers.TapeSpecDTOMapper;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TapeSpecServiceImplementation.
 * Focuses on service behavior only.
 */
@ExtendWith(MockitoExtension.class)
class TapeSpecServiceImplementationTest {

    @Mock
    private TapeSpecRepository tapeSpecRepository;

    @Mock
    private TapeSpecDTOMapper tapeSpecDTOMapper;

    @InjectMocks
    private TapeSpecServiceImplementation tapeSpecService;

    @Test
    void createTapeSpec_returns_created_tapeSpec() {
        TapeSpecRequestDTO request = new TapeSpecRequestDTO(
                "Standard Tape",
                "PVC",
                120,
                "Default tape spec"
        );

        TapeSpecEntity entity = new TapeSpecEntity();
        entity.setTapeName("Standard Tape");
        entity.setTapeType("PVC");
        entity.setThickness(120);
        entity.setInfo("Default tape spec");

        TapeSpecResponseDTO response = new TapeSpecResponseDTO(
                1L,
                "Standard Tape",
                "PVC",
                120,
                "Default tape spec"
        );

        when(tapeSpecDTOMapper.mapToEntity(request)).thenReturn(entity);
        when(tapeSpecRepository.save(entity)).thenReturn(entity);
        when(tapeSpecDTOMapper.mapToDto(entity)).thenReturn(response);

        TapeSpecResponseDTO result = tapeSpecService.createTapeSpec(request);

        assertEquals(1L, result.id());
        assertEquals("Standard Tape", result.tapeName());
        assertEquals("PVC", result.tapeType());

        verify(tapeSpecRepository).save(entity);
        verify(tapeSpecDTOMapper).mapToEntity(request);
        verify(tapeSpecDTOMapper).mapToDto(entity);
    }

    @Test
    void getTapeSpecById_returns_existing_tapeSpec() {
        TapeSpecEntity entity = new TapeSpecEntity();
        entity.setTapeName("Standard Tape");
        entity.setTapeType("PVC");
        entity.setThickness(120);
        entity.setInfo("Default tape spec");

        TapeSpecResponseDTO response = new TapeSpecResponseDTO(
                1L,
                "Standard Tape",
                "PVC",
                120,
                "Default tape spec"
        );

        when(tapeSpecRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tapeSpecDTOMapper.mapToDto(entity)).thenReturn(response);

        TapeSpecResponseDTO result = tapeSpecService.getTapeSpecById(1L);

        assertEquals(1L, result.id());
        assertEquals("Standard Tape", result.tapeName());

        verify(tapeSpecRepository).findById(1L);
        verify(tapeSpecDTOMapper).mapToDto(entity);
    }
}
