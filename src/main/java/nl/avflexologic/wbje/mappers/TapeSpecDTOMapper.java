package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps TapeSpec entities to DTOs and back.
 */
@Component
public class TapeSpecDTOMapper {

    /**
     * Maps a single entity instance to a response DTO.
     */
    public TapeSpecResponseDTO mapToDto(TapeSpecEntity tapeSpec) {
        if (tapeSpec == null) return null;

        return new TapeSpecResponseDTO(
                tapeSpec.getId(),
                tapeSpec.getTapeName(),
                tapeSpec.getTapeType(),
                tapeSpec.getThickness(),
                tapeSpec.getInfo()
        );
    }

    /**
     * Maps a list of entities to response DTOs.
     */
    public List<TapeSpecResponseDTO> mapToDto(List<TapeSpecEntity> tapeSpecs) {
        List<TapeSpecResponseDTO> result = new ArrayList<>();
        if (tapeSpecs == null) return result;

        for (TapeSpecEntity tapeSpec : tapeSpecs) {
            result.add(mapToDto(tapeSpec));
        }
        return result;
    }

    /**
     * Creates a new entity from request input.
     */
    public TapeSpecEntity mapToEntity(TapeSpecRequestDTO dto) {
        if (dto == null) return null;

        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        tapeSpec.setTapeName(dto.tapeName());
        tapeSpec.setTapeType(dto.tapeType());
        tapeSpec.setThickness(dto.thickness());
        tapeSpec.setInfo(dto.info());
        return tapeSpec;
    }

    /**
     * Applies request input to an existing entity instance.
     */
    public void updateEntity(TapeSpecEntity tapeSpec, TapeSpecRequestDTO dto) {
        if (tapeSpec == null || dto == null) return;

        tapeSpec.setTapeName(dto.tapeName());
        tapeSpec.setTapeType(dto.tapeType());
        tapeSpec.setThickness(dto.thickness());
        tapeSpec.setInfo(dto.info());
    }
}
