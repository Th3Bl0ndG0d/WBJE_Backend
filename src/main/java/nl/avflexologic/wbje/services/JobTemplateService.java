package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service abstraction defining operations for storing, retrieving,
 * and deleting job templates that are stored externally on disk.
 */
public interface JobTemplateService {

    /**
     * Stores a new job template, persisting metadata and writing the binary template file to disk.
     *
     * @param file         uploaded binary file
     * @param templateName logical name of the template as supplied by the client
     * @return metadata DTO describing the stored template
     */
    JobTemplateResponseDTO storeTemplate(MultipartFile file, String templateName);

    /**
     * Retrieves metadata for a stored job template.
     *
     * @param id identifier of the template
     * @return metadata DTO
     */
    JobTemplateResponseDTO getTemplate(Long id);

    /**
     * Lists all available job templates.
     *
     * @return list of metadata DTOs
     */
    List<JobTemplateResponseDTO> getAllTemplates();

    /**
     * Loads the binary job template file for download.
     *
     * @param id identifier of the template
     * @return readable Resource representing the stored file
     */
    Resource loadTemplateFile(Long id);

    /**
     * Deletes both metadata and the associated physical file.
     *
     * @param id identifier of the template
     */
    void deleteTemplate(Long id);
}
