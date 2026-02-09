package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for persistence and retrieval of reports (plates).
 */
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    /**
     * Returns all reports that belong to a given cylinder.
     */
    List<ReportEntity> findAllByCylinderId(Long cylinderId);
    boolean existsByReportSpecId(Long reportSpecId);
    boolean existsByCylinderId(Long cylinderId);

    /**
     * Returns a single report by its cylinder scope and report number.
     * This aligns with the domain uniqueness constraint (cylinder_id, report_nr).
     */
    Optional<ReportEntity> findByCylinderIdAndReportNr(Long cylinderId, Integer reportNr);
}
