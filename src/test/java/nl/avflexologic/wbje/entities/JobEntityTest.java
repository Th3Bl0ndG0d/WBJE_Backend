package nl.avflexologic.wbje.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JobEntityTest {

    @Test
    void setNote_sets_back_reference() {
        JobEntity job = new JobEntity(LocalDateTime.now());
        NoteEntity note = new NoteEntity("N1");
        job.setNote(note);
        assertNotNull(job.getNote());
        assertSame(note, job.getNote());
        assertSame(job, note.getJob());
    }
    @Test
    void job_fields_can_be_filled_in() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 22, 10, 0);
        JobEntity job = new JobEntity(date);

        job.setJobNumber("JB-1001");
        job.setJobName("WBJE Demo");
        job.setInfo("Test job");

        job.setCylinderWidth(800);
        job.setCylinderCircumference(950);

        assertEquals("JB-1001", job.getJobNumber());
        assertEquals(date, job.getJobDate());
        assertEquals("WBJE Demo", job.getJobName());
        assertEquals("Test job", job.getInfo());
        assertEquals(800, job.getCylinderWidth());
        assertEquals(950, job.getCylinderCircumference());
    }
    @Test
    void job_fields_can_be_updated() {
        JobEntity job = new JobEntity(LocalDateTime.of(2025, 12, 22, 10, 0));

        job.setJobNumber("JB-1");
        job.setJobName("A");
        job.setInfo("I1");

        job.setJobNumber("JB-2");
        job.setJobName("B");
        job.setInfo("I2");

        assertEquals("JB-2", job.getJobNumber());
        assertEquals("B", job.getJobName());
        assertEquals("I2", job.getInfo());
    }
//
//    @Test
//    void setNote_replaces_existing_note_consistently() {
//        JobEntity job = new JobEntity(LocalDateTime.now());
//        NoteEntity n1 = new NoteEntity("N1");
//        NoteEntity n2 = new NoteEntity("N2");
//
//        job.setNote(n1);
//        job.setNote(n2);
//
//        assertSame(n2, job.getNote());
//        assertNull(n1.getJob());
//        assertSame(job, n2.getJob());
//    }
}