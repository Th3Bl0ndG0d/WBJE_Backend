package nl.avflexologic.wbje.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Abstraction for retrieving, storing, and deleting job template files on disk.
 */
public interface TemplateStorage {

    /**
     * Persists the given file under a deterministic path for the specified template id.
     *
     * @param id   identifier of the template
     * @param file uploaded file contents
     */
    void saveFile(Long id, MultipartFile file) throws IOException;

    /**
     * Returns a readable resource pointing to the stored template file.
     *
     * @param id identifier of the template
     * @return Resource that can be streamed to the client
     */
    Resource loadFile(Long id);

    /**
     * Deletes the physical file associated with the given template id.
     *
     * @param id identifier of the template
     */
    void deleteFile(Long id);
}
