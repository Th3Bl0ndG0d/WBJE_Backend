package nl.avflexologic.wbje.dtos.report;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportRequestDTOTest {

    @Test
    void constructor_requires_reportNr() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new ReportRequestDTO(null, 500, 10, 20, 1L, 2L)
        );

        assertEquals("reportNr is required.", ex.getMessage());
    }
}