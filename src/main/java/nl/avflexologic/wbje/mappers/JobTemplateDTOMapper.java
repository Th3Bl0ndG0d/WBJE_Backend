package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
import nl.avflexologic.wbje.entities.JobTemplateEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper component responsible for transforming JobTemplateEntity instances into their
 * corresponding immutable response DTO representations.
 */
@Component
public class JobTemplateDTOMapper {

    /**
     * Maps a single entity instance into a response DTO.
     *
     * @param entity JobTemplateEntity instance (must not be null)
     * @return JobTemplateResponseDTO instance
     */
    public JobTemplateResponseDTO mapToDto(JobTemplateEntity entity) {
        return new JobTemplateResponseDTO(
                entity.getId(),
                entity.getTemplateName(),
                entity.getOriginalFileName(),
                entity.getContentType(),
                entity.getFileSize(),
                entity.getUploadedAt()
        );
    }

    /**
     * Maps a list of entities to a list of DTOs.
     *
     * @param entities list of JobTemplateEntity instances
     * @return list of DTOs
     */
    public List<JobTemplateResponseDTO> mapToDtoList(List<JobTemplateEntity> entities) {
        List<JobTemplateResponseDTO> result = new ArrayList<>();
        for (JobTemplateEntity e : entities) {
            result.add(mapToDto(e));
        }
        return result;
    }
}
