package nl.avflexologic.wbje.repositories;

import nl.avflexologic.wbje.entities.JobTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository abstraction responsible for persistent metadata management of job templates.
 * <p>
 * Only metadata is persisted; the physical file is stored externally on the filesystem.
 */
public interface JobTemplateRepository extends JpaRepository<JobTemplateEntity, Long> {

    /**
     * Retrieves a job template by its functional template name.
     *
     * @param templateName unique human-readable template identifier
     * @return an Optional containing the matching entity, if present
     */
    Optional<JobTemplateEntity> findByTemplateName(String templateName);
}
