package nl.avflexologic.wbje.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CylinderEntityTest {

    @Test
    void constructor_requires_job_tapespec_and_cylinderNr() {
        JobEntity job = new JobEntity(LocalDateTime.now());
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        assertNotNull(cylinder);
        assertEquals(1, cylinder.getCylinderNr());
        assertSame(job, cylinder.getJob());
        assertSame(tapeSpec, cylinder.getTapeSpec());
    }

    @Test
    void job_addCylinder_sets_back_reference() {
        JobEntity job = new JobEntity(LocalDateTime.now());
        TapeSpecEntity tapeSpec = new TapeSpecEntity();

        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        JobEntity otherJob = new JobEntity(LocalDateTime.now());
        otherJob.addCylinder(cylinder);

        assertSame(otherJob, cylinder.getJob());
        assertTrue(otherJob.getCylinders().contains(cylinder));
    }
}
