package nl.avflexologic.wbje.dtos.job;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JobRequestDTOTest {

    @Test
    void jobDate_can_be_null_in_dto() {
        JobRequestDTO dto = new JobRequestDTO(LocalDateTime.now());

        dto.jobNumber = "JB-1";
        dto.jobDate = null;
        dto.jobName = "Name";

        assertNull(dto.jobDate);
    }
}
