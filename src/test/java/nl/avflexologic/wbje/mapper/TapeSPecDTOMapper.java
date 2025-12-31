package nl.avflexologic.wbje.mapper;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.mappers.TapeSpecDTOMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TapeSpecDTOMapperTest {

    private final TapeSpecDTOMapper mapper = new TapeSpecDTOMapper();

    @Test
    void mapToEntity_maps_request_fields() {
        TapeSpecRequestDTO request = new TapeSpecRequestDTO(
                "Standard Tape",
                "PET",
                120,
                "Info"
        );

        TapeSpecEntity entity = mapper.mapToEntity(request);

        assertNotNull(entity);
        assertEquals("Standard Tape", entity.getTapeName());
        assertEquals("PET", entity.getTapeType());
        assertEquals(120, entity.getThickness());
        assertEquals("Info", entity.getInfo());
    }

    @Test
    void mapToDto_maps_entity_fields() {
        TapeSpecEntity entity = new TapeSpecEntity();
        entity.setTapeName("Standard Tape");
        entity.setTapeType("PET");
        entity.setThickness(120);
        entity.setInfo("Info");

        TapeSpecResponseDTO dto = mapper.mapToDto(entity);

        assertNotNull(dto);
        assertEquals("Standard Tape", dto.tapeName());
        assertEquals("PET", dto.tapeType());
    }
}
