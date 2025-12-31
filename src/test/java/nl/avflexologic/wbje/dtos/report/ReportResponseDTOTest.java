package nl.avflexologic.wbje.dtos.report;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportResponseDTOTest {

    @Test
    void response_fields_are_set_correctly() {
        ReportResponseDTO dto = new ReportResponseDTO(
                1L, 10, 500, 10, 20, 100L, 200L
        );

        assertEquals(1L, dto.id());
        assertEquals(10, dto.reportNr());
        assertEquals(100L, dto.cylinderId());
        assertEquals(200L, dto.reportSpecId());
    }
}
