package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {

    // Custom query methods can be added here when needed.
}
