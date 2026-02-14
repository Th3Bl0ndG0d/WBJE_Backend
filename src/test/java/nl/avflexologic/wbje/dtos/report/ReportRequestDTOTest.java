package nl.avflexologic.wbje.dtos.report;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportRequestDTOTest {

    @Test
    void constructor_allows_null_without_validation_context() {
        assertDoesNotThrow(
                () -> new ReportRequestDTO(null, 500, 10, 20, 1L, 2L)
        );
    }

}