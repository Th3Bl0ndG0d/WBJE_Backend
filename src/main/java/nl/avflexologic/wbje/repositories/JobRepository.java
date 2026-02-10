package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.JobEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 */
@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
    @EntityGraph(attributePaths = {
            "note",
            "cylinders",
            "cylinders.tapeSpec",
            "cylinders.reports",
            "cylinders.reports.reportSpec"
    })
    Optional<JobEntity> findById(Long id);

    // Custom query methods can be added here when needed.
}
