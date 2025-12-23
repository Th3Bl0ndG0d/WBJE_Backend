package nl.avflexologic.wbje.reposetories;

import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.NoteEntity;
import nl.avflexologic.wbje.repositories.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic JPA tests for JobRepository using the configured PostgreSQL database.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Test
    @Rollback(false) // Use this if you want to see you test data in the DB!
    void save_and_findById_persists_job_and_note() {
        // Arrange: build a job aggregate with a linked note.
        JobEntity job = new JobEntity(LocalDateTime.of(2025, 12, 22, 10, 0));
        job.setJobNumber("JB-1001");
        job.setJobName("WBJE Demo");
        job.setCylinderWidth(800);
        job.setCylinderCircumference(950);
        job.setInfo("Initial job info");
        job.setNote(new NoteEntity("Note text"));

        // Act: persist and reload the job via the repository.
        JobEntity saved = jobRepository.save(job);
        Long id = saved.getId();
        JobEntity reloaded = jobRepository.findById(id).orElseThrow();

        // Assert: id, basic fields and the 1-1 relation are intact.
        assertNotNull(id, "Job id should be generated.");
        assertEquals("JB-1001", reloaded.getJobNumber());
        assertEquals("WBJE Demo", reloaded.getJobName());
        assertEquals(800, reloaded.getCylinderWidth());
        assertEquals(950, reloaded.getCylinderCircumference());
        assertEquals("Initial job info", reloaded.getInfo());
        assertNotNull(reloaded.getNote(), "Note should be persisted with the job.");
        assertEquals("Note text", reloaded.getNote().getInfo());
        assertEquals(reloaded, reloaded.getNote().getJob());
    }
}