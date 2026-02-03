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

    @Test
    void constructor_sets_required_fields() {
        JobEntity job = new JobEntity(LocalDateTime.now());

        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        ReportSpecEntity reportSpec = new ReportSpecEntity();
        ReportEntity report = new ReportEntity(cylinder, reportSpec, 10);

        assertEquals(10, report.getReportNr());
        assertSame(cylinder, report.getCylinder());
        assertSame(reportSpec, report.getReportSpec());
    }

    @Test
    void setters_update_optional_fields() {
        JobEntity job = new JobEntity(LocalDateTime.now());

        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        ReportSpecEntity reportSpec = new ReportSpecEntity();
        ReportEntity report = new ReportEntity(cylinder, reportSpec, 10);

        report.setReportWidth(500);
        report.setXOffset(12);
        report.setYOffset(34);

        assertEquals(500, report.getReportWidth());
        assertEquals(12, report.getXOffset());
        assertEquals(34, report.getYOffset());
    }
}
