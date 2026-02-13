package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
import nl.avflexologic.wbje.entities.JobTemplateEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.JobTemplateDTOMapper;
import nl.avflexologic.wbje.repositories.JobTemplateRepository;


import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Concrete service implementation handling:
 * <ul>
 *     <li>Template metadata persistence</li>
 *     <li>Filesystem storage operations</li>
 *     <li>Metadata-to-DTO translations</li>
 * </ul>
 */
@Service
public class JobTemplateServiceImplementation implements JobTemplateService {

    private final JobTemplateRepository repository;
    private final TemplateStorage storage;
    private final JobTemplateDTOMapper mapper;

    public JobTemplateServiceImplementation(
            JobTemplateRepository repository,
            TemplateStorage storage,
            JobTemplateDTOMapper mapper
    ) {
        this.repository = repository;
        this.storage = storage;
        this.mapper = mapper;
    }

    @Override
    public JobTemplateResponseDTO storeTemplate(MultipartFile file, String templateName) {

        // prevent duplicate templateName
        if (repository.existsByTemplateName(templateName)) {
            throw new IllegalArgumentException(
                    "JobTemplate with name '" + templateName + "' already exists."
            );
        }
        String storedFileName = generateStoredFileName(file.getOriginalFilename());

        JobTemplateEntity entity = new JobTemplateEntity(
                null,
                templateName,
                storedFileName,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                LocalDateTime.now()
        );

        JobTemplateEntity saved = repository.save(entity);

        try {
            storage.saveFile(saved.getId(), file);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store template file for id " + saved.getId(), e);
        }

        return mapper.mapToDto(saved);
    }

    @Override
    public JobTemplateResponseDTO getTemplate(Long id) {
        JobTemplateEntity template = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job template not found for id: " + id));

        return mapper.mapToDto(template);
    }

    @Override
    public List<JobTemplateResponseDTO> getAllTemplates() {
        return mapper.mapToDtoList(repository.findAll());
    }

    @Override
    public Resource loadTemplateFile(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job template not found for id: " + id);
        }
        return storage.loadFile(id);
    }

    @Override
    public void deleteTemplate(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job template not found for id: " + id);
        }

        storage.deleteFile(id);
        repository.deleteById(id);
    }

    /**
     * Generates a stable, collision-resistant stored filename using a random UUID
     * while preserving the original file extension where possible.
     */
    private String generateStoredFileName(String originalFileName) {
        String extension = ".bin";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }
}
