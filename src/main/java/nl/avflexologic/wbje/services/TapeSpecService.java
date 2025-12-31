package nl.avflexologic.wbje.services;


import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;

import java.util.List;

public interface TapeSpecService {

    TapeSpecResponseDTO createTapeSpec(TapeSpecRequestDTO request);

    TapeSpecResponseDTO getTapeSpecById(Long id);

    List<TapeSpecResponseDTO> getAllTapeSpecs();

    TapeSpecResponseDTO updateTapeSpec(Long id, TapeSpecRequestDTO request);

    void deleteTapeSpec(Long id);
}
