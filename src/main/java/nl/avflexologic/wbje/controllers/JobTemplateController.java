package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
import nl.avflexologic.wbje.services.JobTemplateService;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller responsible for the full lifecycle of job templates stored on disk.
 * <p>
 * This implementation supports simplified multipart-based uploads where the template
 * consists solely of a binary file and a human-defined template name.
 * No metadata DTO is required for ingestion, which increases robustness and
 * reduces client-side complexity.
 */
@RestController
@RequestMapping("/job-templates")
@Tag(name = "Job Templates", description = "Endpoints for uploading, retrieving and deleting job templates.")
public class JobTemplateController {

    private final JobTemplateService jobTemplateService;

    public JobTemplateController(JobTemplateService jobTemplateService) {
        this.jobTemplateService = jobTemplateService;
    }

    /**
     * Uploads a job template consisting of a binary file and a simple string name.
     * <p>
     * Expected multipart request structure:
     * <ul>
     *     <li><b>file</b>: the uploaded binary template file</li>
     *     <li><b>name</b>: text field representing the template's logical name</li>
     * </ul>
     */
    @Operation(summary = "Upload a job template",
            description = "Receives a template file + name using simplified multipart form-data.")
    @ApiResponse(responseCode = "201", description = "Template successfully stored.",
            content = @Content(schema = @Schema(implementation = JobTemplateResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid multipart request.",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobTemplateResponseDTO> uploadTemplate(
            @RequestPart("file") MultipartFile file,
            @RequestPart("name") String templateName
    ) {
        JobTemplateResponseDTO saved = jobTemplateService.storeTemplate(file, templateName);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}/download")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Returns metadata for all stored job templates.
     */
    @Operation(summary = "List job templates")
    @GetMapping
    public List<JobTemplateResponseDTO> getAllTemplates() {
        return jobTemplateService.getAllTemplates();
    }

    /**
     * Returns metadata for a specific job template.
     */
    @Operation(summary = "Get a template by id")
    @GetMapping("/{id}")
    public JobTemplateResponseDTO getTemplate(@PathVariable Long id) {
        return jobTemplateService.getTemplate(id);
    }

    /**
     * Downloads the binary content associated with a stored job template.
     */
    @Operation(summary = "Download template file")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadTemplate(@PathVariable Long id) {
        JobTemplateResponseDTO metadata = jobTemplateService.getTemplate(id);
        Resource file = jobTemplateService.loadTemplateFile(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(metadata.contentType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metadata.originalFileName() + "\"")
                .body(file);
    }

    /**
     * Deletes both binary file and database metadata associated with the template.
     */
    @Operation(summary = "Delete a template")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        jobTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
