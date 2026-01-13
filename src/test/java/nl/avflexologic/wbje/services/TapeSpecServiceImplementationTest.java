package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.TapeSpecDTOMapper;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
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
 * Unit tests for {@link TapeSpecServiceImplementation}.
 *
 * The suite targets deterministic behavior at service layer:
 * - create/read/list/update/delete flows
 * - not-found branches for read/update/delete
 *
 * Tests follow AAA (Arrange, Act, Assert) with concise scenario-oriented naming.
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
    void createTapeSpec_validRequest() {
        // Arrange
        TapeSpecRequestDTO request = new TapeSpecRequestDTO(
                "Tape A",
                "Foam",
                120,
                "Default tape"
        );

        /*
         * The TapeSpec entity id is database-generated (JPA @GeneratedValue).
         * Therefore, the test should not attempt to set ids via setters.
         * The returned DTO is validated through mapper stubbing.
         */
        TapeSpecEntity mapped = mock(TapeSpecEntity.class);
        TapeSpecEntity saved = mock(TapeSpecEntity.class);

        TapeSpecResponseDTO response = new TapeSpecResponseDTO(
                1L,
                "Tape A",
                "Foam",
                120,
                "Default tape"
        );

        when(tapeSpecDTOMapper.mapToEntity(request)).thenReturn(mapped);
        when(tapeSpecRepository.save(mapped)).thenReturn(saved);
        when(tapeSpecDTOMapper.mapToDto(saved)).thenReturn(response);

        ArgumentCaptor<TapeSpecEntity> captor = ArgumentCaptor.forClass(TapeSpecEntity.class);

        // Act
        TapeSpecResponseDTO result = tapeSpecService.createTapeSpec(request);

        // Assert
        assertNotNull(result, "A successful create operation must return a response DTO.");
        assertEquals(1L, result.id(), "The response id must reflect the mapper output.");
        assertEquals("Tape A", result.tapeName(), "The response tapeName must reflect the request.");
        assertEquals("Foam", result.tapeType(), "The response tapeType must reflect the request.");
        assertEquals(120, result.thickness(), "The response thickness must reflect the request.");
        assertEquals("Default tape", result.info(), "The response info must reflect the request.");

        verify(tapeSpecDTOMapper, times(1)).mapToEntity(request);
        verify(tapeSpecRepository, times(1)).save(captor.capture());
        verify(tapeSpecDTOMapper, times(1)).mapToDto(saved);
        verifyNoMoreInteractions(tapeSpecRepository, tapeSpecDTOMapper);

        assertSame(mapped, captor.getValue(),
                "The service must persist the entity instance returned by the mapper.");
    }

    @Test
    void getTapeSpecById_found() {
        // Arrange
        Long id = 10L;

        TapeSpecEntity entity = mock(TapeSpecEntity.class);
        TapeSpecResponseDTO response = new TapeSpecResponseDTO(
                id,
                "Tape X",
                "Foam",
                150,
                "Info"
        );

        when(tapeSpecRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tapeSpecDTOMapper.mapToDto(entity)).thenReturn(response);

        // Act
        TapeSpecResponseDTO result = tapeSpecService.getTapeSpecById(id);

        // Assert
        assertNotNull(result, "A successful read operation must return a response DTO.");
        assertEquals(id, result.id(), "The response id must match the requested identifier.");
        assertEquals("Tape X", result.tapeName(), "The response must reflect the mapper output.");

        verify(tapeSpecRepository, times(1)).findById(id);
        verify(tapeSpecDTOMapper, times(1)).mapToDto(entity);
        verifyNoMoreInteractions(tapeSpecRepository, tapeSpecDTOMapper);
    }

    @Test
    void getTapeSpecById_notFound() {
        // Arrange
        Long id = 404L;
        when(tapeSpecRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tapeSpecService.getTapeSpecById(id)
        );

        // Assert
        assertEquals("TapeSpec not found for id: 404", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(tapeSpecRepository, times(1)).findById(id);
        verifyNoMoreInteractions(tapeSpecRepository);
        verifyNoInteractions(tapeSpecDTOMapper);
    }

    @Test
    void getAllTapeSpecs_empty() {
        // Arrange
        List<TapeSpecEntity> entities = List.of();
        List<TapeSpecResponseDTO> mapped = List.of();

        when(tapeSpecRepository.findAll()).thenReturn(entities);
        when(tapeSpecDTOMapper.mapToDto(entities)).thenReturn(mapped);

        // Act
        List<TapeSpecResponseDTO> result = tapeSpecService.getAllTapeSpecs();

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertTrue(result.isEmpty(), "The list must be empty when the repository returns no entities.");

        verify(tapeSpecRepository, times(1)).findAll();
        verify(tapeSpecDTOMapper, times(1)).mapToDto(entities);
        verifyNoMoreInteractions(tapeSpecRepository, tapeSpecDTOMapper);
    }

    @Test
    void getAllTapeSpecs_populated() {
        // Arrange
        TapeSpecEntity e1 = mock(TapeSpecEntity.class);
        TapeSpecEntity e2 = mock(TapeSpecEntity.class);
        List<TapeSpecEntity> entities = List.of(e1, e2);

        List<TapeSpecResponseDTO> mapped = List.of(
                new TapeSpecResponseDTO(1L, "A", "Foam", 100, "I1"),
                new TapeSpecResponseDTO(2L, "B", "Rubber", 120, "I2")
        );

        when(tapeSpecRepository.findAll()).thenReturn(entities);
        when(tapeSpecDTOMapper.mapToDto(entities)).thenReturn(mapped);

        // Act
        List<TapeSpecResponseDTO> result = tapeSpecService.getAllTapeSpecs();

        // Assert
        assertNotNull(result, "The service must never return a null list.");
        assertEquals(2, result.size(), "Each entity must be mapped to exactly one DTO.");
        assertEquals(1L, result.get(0).id(), "The first DTO must reflect the mapper output.");
        assertEquals(2L, result.get(1).id(), "The second DTO must reflect the mapper output.");

        verify(tapeSpecRepository, times(1)).findAll();
        verify(tapeSpecDTOMapper, times(1)).mapToDto(entities);
        verifyNoMoreInteractions(tapeSpecRepository, tapeSpecDTOMapper);
    }

    @Test
    void updateTapeSpec_notFound() {
        // Arrange
        Long id = 999L;

        TapeSpecRequestDTO request = new TapeSpecRequestDTO(
                "Tape A",
                "Foam",
                120,
                "Default tape"
        );

        when(tapeSpecRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tapeSpecService.updateTapeSpec(id, request)
        );

        // Assert
        assertEquals("TapeSpec not found for id: 999", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(tapeSpecRepository, times(1)).findById(id);
        verifyNoMoreInteractions(tapeSpecRepository);
        verifyNoInteractions(tapeSpecDTOMapper);
    }

    @Test
    void updateTapeSpec_success() {
        // Arrange
        Long id = 10L;

        TapeSpecRequestDTO request = new TapeSpecRequestDTO(
                "Tape B",
                "Rubber",
                140,
                "Updated"
        );

        TapeSpecEntity existing = mock(TapeSpecEntity.class);
        TapeSpecEntity saved = mock(TapeSpecEntity.class);

        when(tapeSpecRepository.findById(id)).thenReturn(Optional.of(existing));

        /*
         * updateEntity is side-effect based; correctness is validated through interaction checks.
         * This avoids coupling the test to mapper implementation details.
         */
        doNothing().when(tapeSpecDTOMapper).updateEntity(existing, request);

        when(tapeSpecRepository.save(existing)).thenReturn(saved);

        TapeSpecResponseDTO response = new TapeSpecResponseDTO(
                id,
                "Tape B",
                "Rubber",
                140,
                "Updated"
        );
        when(tapeSpecDTOMapper.mapToDto(saved)).thenReturn(response);

        // Act
        TapeSpecResponseDTO result = tapeSpecService.updateTapeSpec(id, request);

        // Assert
        assertNotNull(result, "A successful update operation must return a response DTO.");
        assertEquals(id, result.id(), "The response id must preserve the requested identifier.");
        assertEquals("Tape B", result.tapeName(), "The response must reflect the updated tapeName.");
        assertEquals("Rubber", result.tapeType(), "The response must reflect the updated tapeType.");
        assertEquals(140, result.thickness(), "The response must reflect the updated thickness.");
        assertEquals("Updated", result.info(), "The response must reflect the updated info.");

        verify(tapeSpecRepository, times(1)).findById(id);
        verify(tapeSpecDTOMapper, times(1)).updateEntity(existing, request);
        verify(tapeSpecRepository, times(1)).save(existing);
        verify(tapeSpecDTOMapper, times(1)).mapToDto(saved);
        verifyNoMoreInteractions(tapeSpecRepository, tapeSpecDTOMapper);
    }

    @Test
    void deleteTapeSpec_notFound() {
        // Arrange
        Long id = 404L;
        when(tapeSpecRepository.existsById(id)).thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tapeSpecService.deleteTapeSpec(id)
        );

        // Assert
        assertEquals("TapeSpec not found for id: 404", exception.getMessage(),
                "The exception message must remain stable for consistent API error handling.");

        verify(tapeSpecRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(tapeSpecRepository);
        verifyNoInteractions(tapeSpecDTOMapper);
    }

    @Test
    void deleteTapeSpec_success() {
        // Arrange
        Long id = 10L;
        when(tapeSpecRepository.existsById(id)).thenReturn(true);

        // Act
        tapeSpecService.deleteTapeSpec(id);

        // Assert
        verify(tapeSpecRepository, times(1)).existsById(id);
        verify(tapeSpecRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(tapeSpecRepository);
        verifyNoInteractions(tapeSpecDTOMapper);
    }
}
