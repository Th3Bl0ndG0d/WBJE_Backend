package nl.avflexologic.wbje.dtos.tape;

import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TapeSpecResponseDTOTest {

    @Test
    void response_fields_are_set_correctly() {
        TapeSpecResponseDTO dto = new TapeSpecResponseDTO(
                1L,
                "Standard Tape",
                "3M",
                120,
                "Test"
        );

        assertEquals(1L, dto.id());
        assertEquals("Standard Tape", dto.tapeName());
    }
}
