package nl.avflexologic.wbje.dtos.jobTemplate;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Immutable response DTO representing stored job template metadata.
 * <p>
 * Contains no binary information. The physical job template file is retrieved separately
 * through template download operations.
 */
@Schema(description = "Metadata response representation of a stored job template.")
public record JobTemplateResponseDTO(

        @Schema(description = "Database identifier of the job template.", example = "1")
        Long id,

        @Schema(description = "Human-readable template name.", example = "Standard 4-color layout")
        String templateName,

        @Schema(description = "Original filename as uploaded.", example = "wbje-template.json")
        String originalFileName,

        @Schema(description = "MIME type of the stored template file.", example = "application/json")
        String contentType,

        @Schema(description = "File size in bytes.", example = "2048")
        long fileSize,

        @Schema(description = "Timestamp of upload.")
        LocalDateTime uploadedAt

) { }
