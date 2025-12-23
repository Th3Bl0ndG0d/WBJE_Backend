package nl.avflexologic.wbje.dtos.job;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class JobResponseDTOTest {

    @Test
    void response_fields_are_set_correctly() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 22, 10, 0);

        JobResponseDTO dto = new JobResponseDTO();
        dto.id = 1L;
        dto.jobNumber = "JB-1001";
        dto.jobDate = date;
        dto.jobName = "WBJE Demo";
        dto.info = "Info";
        dto.cylinderWidth = 120;
        dto.cylinderCircumference = 800;
        dto.noteInfo = "Note text";

        assertEquals(1L, dto.id);
        assertEquals("JB-1001", dto.jobNumber);
        assertEquals(date, dto.jobDate);
        assertEquals("WBJE Demo", dto.jobName);
        assertEquals("Info", dto.info);
        assertEquals(120, dto.cylinderWidth);
        assertEquals(800, dto.cylinderCircumference);
        assertEquals("Note text", dto.noteInfo);
    }
}
