package nl.avflexologic.wbje.reposetories;

import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that TapeSpecEntity can be persisted and retrieved using JPA.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TapeSpecRepositoryTest {

    @Autowired
    private TapeSpecRepository tapeSpecRepository;

    @Test
    void save_and_findById_persists_tapeSpec() {
        // Arrange
        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        tapeSpec.setTapeName("Standard Tape");
        tapeSpec.setTapeType("PET");
        tapeSpec.setThickness(120);
        tapeSpec.setInfo("Repository test");

        // Act
        TapeSpecEntity saved = tapeSpecRepository.save(tapeSpec);

        // Assert
        assertNotNull(saved.getId(), "After persisting, the entity is expected to have a generated identifier.");

        Optional<TapeSpecEntity> found = tapeSpecRepository.findById(saved.getId());
        assertTrue(found.isPresent(), "The persisted TapeSpecEntity must be retrievable by its id.");

        assertEquals("Standard Tape", found.get().getTapeName(), "The persisted tapeName must be stored and retrieved unchanged.");
        assertEquals("PET", found.get().getTapeType(), "The persisted tapeType must be stored and retrieved unchanged.");
        assertEquals(120, found.get().getThickness(), "The persisted thickness must be stored and retrieved unchanged.");
        assertEquals("Repository test", found.get().getInfo(), "The persisted info must be stored and retrieved unchanged.");
    }
}
