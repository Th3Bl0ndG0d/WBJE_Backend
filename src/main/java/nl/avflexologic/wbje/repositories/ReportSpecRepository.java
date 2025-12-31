package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.ReportSpecEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persistence and retrieval of report specifications.
 */
public interface ReportSpecRepository extends JpaRepository<ReportSpecEntity, Long> {
}
