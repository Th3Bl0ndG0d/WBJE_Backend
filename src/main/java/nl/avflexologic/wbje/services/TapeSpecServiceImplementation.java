package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.TapeSpecDTOMapper;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implements TapeSpec operations using repository access and DTO mapping.
 */
@Service
public class TapeSpecServiceImplementation implements TapeSpecService {

    private final TapeSpecRepository tapeSpecRepository;
    private final TapeSpecDTOMapper tapeSpecDTOMapper;

    /**
     * Creates a new service instance with required dependencies.
     */
    public TapeSpecServiceImplementation(TapeSpecRepository tapeSpecRepository, TapeSpecDTOMapper tapeSpecDTOMapper) {
        this.tapeSpecRepository = tapeSpecRepository;
        this.tapeSpecDTOMapper = tapeSpecDTOMapper;
    }

    /**
     * Creates and persists a new TapeSpec.
     */
    @Override
    public TapeSpecResponseDTO createTapeSpec(TapeSpecRequestDTO request) {
        TapeSpecEntity entity = tapeSpecDTOMapper.mapToEntity(request);
        TapeSpecEntity saved = tapeSpecRepository.save(entity);
        return tapeSpecDTOMapper.mapToDto(saved);
    }

    /**
     * Returns a single TapeSpec by id.
     */
    @Override
    public TapeSpecResponseDTO getTapeSpecById(Long id) {
        TapeSpecEntity tapeSpec = tapeSpecRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TapeSpec not found for id: " + id));
        return tapeSpecDTOMapper.mapToDto(tapeSpec);
    }

    /**
     * Returns all TapeSpecs.
     */
    @Override
    public List<TapeSpecResponseDTO> getAllTapeSpecs() {
        return tapeSpecDTOMapper.mapToDto(tapeSpecRepository.findAll());
    }

    /**
     * Updates an existing TapeSpec.
     */
    @Override
    public TapeSpecResponseDTO updateTapeSpec(Long id, TapeSpecRequestDTO request) {
        TapeSpecEntity existing = tapeSpecRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TapeSpec not found for id: " + id));

        tapeSpecDTOMapper.updateEntity(existing, request);

        TapeSpecEntity saved = tapeSpecRepository.save(existing);
        return tapeSpecDTOMapper.mapToDto(saved);
    }

    /**
     * Deletes a TapeSpec by id.
     */
    @Override
    public void deleteTapeSpec(Long id) {
        if (!tapeSpecRepository.existsById(id)) {
            throw new ResourceNotFoundException("TapeSpec not found for id: " + id);
        }
        tapeSpecRepository.deleteById(id);
    }
}
