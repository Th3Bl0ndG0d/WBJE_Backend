package nl.avflexologic.wbje.dtos.tape;

import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TapeSpecRequestDTOTest {

    @Test
    void constructor_requires_tapeName() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TapeSpecRequestDTO(
                        null,
                        "PET",
                        120,
                        "Info"
                )
        );

        assertEquals("tapeName is required.", ex.getMessage());
    }
}
