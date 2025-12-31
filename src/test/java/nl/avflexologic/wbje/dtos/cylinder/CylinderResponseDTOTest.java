package nl.avflexologic.wbje.dtos.cylinder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CylinderResponseDTOTest {

    @Test
    void response_fields_are_set() {
        CylinderResponseDTO dto = new CylinderResponseDTO(
                1L,
                2,
                "Blue",
                "Cylinder info",
                10L,
                20L
        );

        assertEquals(1L, dto.id());
        assertEquals(2, dto.cylinderNr());
    }
}
