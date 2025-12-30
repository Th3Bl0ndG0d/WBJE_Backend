package nl.avflexologic.wbje.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportEntityTest {

    @Test
    void constructor_requires_reportSpec() {
        JobEntity job = new JobEntity(LocalDateTime.now());
        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new ReportEntity(cylinder, null, 1)
        );

        assertEquals("reportSpec is required.", ex.getMessage());
    }
}
