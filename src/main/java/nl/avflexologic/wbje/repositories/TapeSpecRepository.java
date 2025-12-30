package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.TapeSpecEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TapeSpecRepository extends JpaRepository<TapeSpecEntity, Long> { }
