package nl.avflexologic.wbje.repositories;


import nl.avflexologic.wbje.entities.CylinderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CylinderRepository extends JpaRepository<CylinderEntity, Long> {

    List<CylinderEntity> findAllByJob_Id(Long jobId);

    Optional<CylinderEntity> findByIdAndJob_Id(Long cylinderId, Long jobId);

    boolean existsByIdAndJob_Id(Long cylinderId, Long jobId);
}
