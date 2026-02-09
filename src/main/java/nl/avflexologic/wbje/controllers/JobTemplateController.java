//////package nl.avflexologic.wbje.controllers;
//////
//////import io.swagger.v3.oas.annotations.Operation;
//////import io.swagger.v3.oas.annotations.media.Schema;
//////import io.swagger.v3.oas.annotations.media.Content;
//////import io.swagger.v3.oas.annotations.responses.ApiResponse;
//////import io.swagger.v3.oas.annotations.tags.Tag;
//////
//////import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//////import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
//////import nl.avflexologic.wbje.services.JobTemplateService;
//////
//////import org.springframework.core.io.Resource;
//////import org.springframework.http.*;
//////import org.springframework.web.bind.annotation.*;
//////import org.springframework.web.multipart.MultipartFile;
//////import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//////
//////import java.net.URI;
//////import java.util.List;
//////
///////**
////// * Controller responsible for the full lifecycle of job templates stored on disk.
////// * <p>
////// * This implementation supports simplified multipart-based uploads where the template
////// * consists solely of a binary file and a human-defined template name.
////// * No metadata DTO is required for ingestion, which increases robustness and
////// * reduces client-side complexity.
////// */
//////@RestController
//////@RequestMapping("/job-templates")
//////@Tag(name = "Job Templates", description = "Endpoints for uploading, retrieving and deleting job templates.")
//////public class JobTemplateController {
//////
//////    private final JobTemplateService jobTemplateService;
//////
//////    public JobTemplateController(JobTemplateService jobTemplateService) {
//////        this.jobTemplateService = jobTemplateService;
//////    }
//////
//////    /**
//////     * Uploads a job template consisting of a binary file and a simple string name.
//////     * <p>
//////     * Expected multipart request structure:
//////     * <ul>
//////     *     <li><b>file</b>: the uploaded binary template file</li>
//////     *     <li><b>name</b>: text field representing the template's logical name</li>
//////     * </ul>
//////     */
//////    @Operation(summary = "Upload a job template",
//////            description = "Receives a template file + name using simplified multipart form-data.")
//////    @ApiResponse(responseCode = "201", description = "Template successfully stored.",
//////            content = @Content(schema = @Schema(implementation = JobTemplateResponseDTO.class)))
//////    @ApiResponse(responseCode = "400", description = "Invalid multipart request.",
//////            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
//////    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//////    public ResponseEntity<JobTemplateResponseDTO> uploadTemplate(
//////            @RequestPart("file") MultipartFile file,
//////            @RequestPart("name") String templateName
//////    ) {
//////        JobTemplateResponseDTO saved = jobTemplateService.storeTemplate(file, templateName);
//////
//////        URI location = ServletUriComponentsBuilder
//////                .fromCurrentRequest()
//////                .path("/{id}/download")
//////                .buildAndExpand(saved.id())
//////                .toUri();
//////
//////        return ResponseEntity.created(location).body(saved);
//////    }
//////
//////    /**
//////     * Returns metadata for all stored job templates.
//////     */
//////    @Operation(summary = "List job templates")
//////    @GetMapping
//////    public List<JobTemplateResponseDTO> getAllTemplates() {
//////        return jobTemplateService.getAllTemplates();
//////    }
//////
//////    /**
//////     * Returns metadata for a specific job template.
//////     */
//////    @Operation(summary = "Get a template by id")
//////    @GetMapping("/{id}")
//////    public JobTemplateResponseDTO getTemplate(@PathVariable Long id) {
//////        return jobTemplateService.getTemplate(id);
//////    }
//////
//////    /**
//////     * Downloads the binary content associated with a stored job template.
//////     */
//////    @Operation(summary = "Download template file")
//////    @GetMapping("/{id}/download")
//////    public ResponseEntity<Resource> downloadTemplate(@PathVariable Long id) {
//////        JobTemplateResponseDTO metadata = jobTemplateService.getTemplate(id);
//////        Resource file = jobTemplateService.loadTemplateFile(id);
//////
//////        MediaType mediaType;
//////        try {
//////            mediaType = MediaType.parseMediaType(metadata.contentType());
//////        } catch (Exception e) {
//////            mediaType = MediaType.APPLICATION_OCTET_STREAM;
//////        }
//////
//////        return ResponseEntity.ok()
//////                .contentType(mediaType)
//////                .header(HttpHeaders.CONTENT_DISPOSITION,
//////                        "attachment; filename=\"" + metadata.originalFileName() + "\"")
//////                .body(file);
//////    }
//////
//////    /**
//////     * Deletes both binary file and database metadata associated with the template.
//////     */
//////    @Operation(summary = "Delete a template")
//////    @DeleteMapping("/{id}")
//////    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
//////        jobTemplateService.deleteTemplate(id);
//////        return ResponseEntity.noContent().build();
//////    }
//////}
////package nl.avflexologic.wbje.controllers;
////
////import io.swagger.v3.oas.annotations.Operation;
////import io.swagger.v3.oas.annotations.media.*;
////import io.swagger.v3.oas.annotations.parameters.RequestBody;
////import io.swagger.v3.oas.annotations.responses.*;
////import io.swagger.v3.oas.annotations.security.SecurityRequirement;
////import io.swagger.v3.oas.annotations.tags.Tag;
////
////import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
////import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
////import nl.avflexologic.wbje.services.JobTemplateService;
////
////import org.springframework.core.io.Resource;
////import org.springframework.http.*;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
////
////import java.net.URI;
////import java.util.List;
////
/////**
//// * Controller responsible for the lifecycle of job templates stored on disk.
//// * A template consists of a binary JSON file + a human-readable template name.
//// * Metadata is stored in the database; the raw file is stored on disk.
//// */
////@RestController
////@RequestMapping("/job-templates")
////@Tag(name = "Job Templates", description = "Endpoints for uploading, retrieving and deleting job templates.")
////public class JobTemplateController {
////
////    private final JobTemplateService jobTemplateService;
////
////    public JobTemplateController(JobTemplateService jobTemplateService) {
////        this.jobTemplateService = jobTemplateService;
////    }
////
////    // ========================================================================
////    // POST — UPLOAD TEMPLATE (ADMIN ONLY)
////    // ========================================================================
////
////    @Operation(
////            summary = "Upload a job template",
////            description = "Receives a template file + name using multipart/form-data.",
////            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN"})
////    )
////    @ApiResponses({
////            @ApiResponse(
////                    responseCode = "201",
////                    description = "Template successfully stored.",
////                    content = @Content(
////                            mediaType = "application/json",
////                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
////                            examples = @ExampleObject(
////                                    name = "TemplateCreatedExample",
////                                    value = """
////                                            {
////                                              "id": 1,
////                                              "originalFileName": "wbje-template.json",
////                                              "contentType": "application/json",
////                                              "fileSize": 4096,
////                                              "uploadedAt": "2025-12-22T10:00:00",
////                                              "templateName": "Customer A Default"
////                                            }
////                                            """
////                            )
////                    )
////            ),
////            @ApiResponse(
////                    responseCode = "400",
////                    description = "Invalid multipart request.",
////                    content = @Content(
////                            mediaType = "application/json",
////                            schema = @Schema(implementation = ApiErrorDTO.class),
////                            examples = @ExampleObject(
////                                    name = "UploadErrorExample",
////                                    value = """
////                                            {
////                                              "timestamp": "2025-01-10T14:32:11.123",
////                                              "status": 400,
////                                              "error": "Bad Request",
////                                              "message": "Invalid file upload request.",
////                                              "path": "/job-templates",
////                                              "fieldErrors": null
////                                            }
////                                            """
////                            )
////                    )
////            )
////    })
////    @RequestBody(
////            description = "Multipart request containing the template JSON file and its logical name.",
////            required = true,
////            content = @Content(
////                    mediaType = "multipart/form-data",
////                    schema = @Schema(type = "object"),
////                    schemaProperties = {
////                            @SchemaProperty(
////                                    name = "file",
////                                    schema = @Schema(
////                                            type = "string",
////                                            format = "binary",
////                                            description = "JSON file containing the full job template."
////                                    )
////                            ),
////                            @SchemaProperty(
////                                    name = "name",
////                                    schema = @Schema(
////                                            type = "string",
////                                            description = "Human-readable name for this template."
////                                    )
////                            )
////                    },
////                    examples = @ExampleObject(
////                            name = "TemplateUploadExample",
////                            value = "{ \"file\": \"(binary JSON)\", \"name\": \"Customer X Default\" }"
////                    )
////            )
////    )
////    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    @PreAuthorize("hasRole('ADMIN')")
////    public ResponseEntity<JobTemplateResponseDTO> uploadTemplate(
////            @RequestPart("file") MultipartFile file,
////            @RequestPart("name") String templateName
////    ) {
////        JobTemplateResponseDTO saved = jobTemplateService.storeTemplate(file, templateName);
////
////        URI location = ServletUriComponentsBuilder
////                .fromCurrentRequest()
////                .path("/{id}/download")
////                .buildAndExpand(saved.id())
////                .toUri();
////
////        return ResponseEntity.created(location).body(saved);
////    }
////
////    // ========================================================================
////    // GET ALL — ADMIN + USER
////    // ========================================================================
////
////    @Operation(
////            summary = "List job templates",
////            description = "Returns metadata for all stored job templates.",
////            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
////    )
////    @ApiResponse(
////            responseCode = "200",
////            description = "Template metadata successfully retrieved.",
////            content = @Content(
////                    mediaType = "application/json",
////                    schema = @Schema(implementation = JobTemplateResponseDTO.class),
////                    examples = @ExampleObject(
////                            name = "TemplateListExample",
////                            value = """
////                                    [
////                                      {
////                                        "id": 1,
////                                        "originalFileName": "wbje-template.json",
////                                        "contentType": "application/json",
////                                        "fileSize": 4096,
////                                        "uploadedAt": "2025-12-22T10:00:00",
////                                        "templateName": "Default Template"
////                                      }
////                                    ]
////                                    """
////                    )
////            )
////    )
////    @GetMapping
////    @PreAuthorize("hasAnyRole('ADMIN','USER')")
////    public List<JobTemplateResponseDTO> getAllTemplates() {
////        return jobTemplateService.getAllTemplates();
////    }
////
////    // ========================================================================
////    // GET ONE — ADMIN + USER
////    // ========================================================================
////
////    @Operation(
////            summary = "Get a template by id",
////            description = "Returns metadata for a specific job template.",
////            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
////    )
////    @ApiResponses({
////            @ApiResponse(
////                    responseCode = "200",
////                    description = "Template metadata retrieved.",
////                    content = @Content(
////                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
////                            examples = @ExampleObject(
////                                    name = "TemplateMetadataExample",
////                                    value = """
////                                            {
////                                              "id": 1,
////                                              "originalFileName": "wbje-template.json",
////                                              "contentType": "application/json",
////                                              "fileSize": 4096,
////                                              "uploadedAt": "2025-12-22T10:00:00",
////                                              "templateName": "Default Template"
////                                            }
////                                            """
////                            )
////                    )
////            ),
////            @ApiResponse(
////                    responseCode = "404",
////                    description = "Template not found.",
////                    content = @Content(
////                            schema = @Schema(implementation = ApiErrorDTO.class),
////                            examples = @ExampleObject(
////                                    name = "TemplateNotFoundExample",
////                                    value = """
////                                            {
////                                              "timestamp": "2025-01-10T14:32:11.123",
////                                              "status": 404,
////                                              "error": "Not Found",
////                                              "message": "Template not found for id: 99",
////                                              "path": "/job-templates/99",
////                                              "fieldErrors": null
////                                            }
////                                            """
////                            )
////                    )
////            )
////    })
////    @GetMapping("/{id}")
////    @PreAuthorize("hasAnyRole('ADMIN','USER')")
////    public JobTemplateResponseDTO getTemplate(@PathVariable Long id) {
////        return jobTemplateService.getTemplate(id);
////    }
////
////    // ========================================================================
////    // DOWNLOAD — ADMIN + USER
////    // ========================================================================
////
////    @Operation(
////            summary = "Download template file",
////            description = "Downloads the JSON file belonging to the template.",
////            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
////    )
////    @ApiResponses({
////            @ApiResponse(
////                    responseCode = "200",
////                    description = "Template file returned.",
////                    content = @Content(
////                            mediaType = "application/json",
////                            examples = @ExampleObject(
////                                    name = "TemplateFileExample",
////                                    value = """
////                                            {
////                                              "jobNumber": "JB-1001",
////                                              "jobName": "Demo Job from Template",
////                                              "cylinders": [ ... ]
////                                            }
////                                            """
////                            )
////                    )
////            ),
////            @ApiResponse(
////                    responseCode = "404",
////                    description = "Template file not found.",
////                    content = @Content(
////                            schema = @Schema(implementation = ApiErrorDTO.class)
////                    )
////            )
////    })
////    @GetMapping("/{id}/download")
////    @PreAuthorize("hasAnyRole('ADMIN','USER')")
////    public ResponseEntity<Resource> downloadTemplate(@PathVariable Long id) {
////        JobTemplateResponseDTO metadata = jobTemplateService.getTemplate(id);
////        Resource file = jobTemplateService.loadTemplateFile(id);
////
////        MediaType mediaType;
////        try {
////            mediaType = MediaType.parseMediaType(metadata.contentType());
////        } catch (Exception e) {
////            mediaType = MediaType.APPLICATION_OCTET_STREAM;
////        }
////
////        return ResponseEntity.ok()
////                .contentType(mediaType)
////                .header(
////                        HttpHeaders.CONTENT_DISPOSITION,
////                        "attachment; filename=\"" + metadata.originalFileName() + "\""
////                )
////                .body(file);
////    }
////
////    // ========================================================================
////    // DELETE — ADMIN ONLY
////    // ========================================================================
////
////    @Operation(
////            summary = "Delete a template",
////            description = "Deletes both stored metadata and the template file.",
////            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN"})
////    )
////    @ApiResponses({
////            @ApiResponse(
////                    responseCode = "204",
////                    description = "Template deleted."
////            ),
////            @ApiResponse(
////                    responseCode = "404",
////                    description = "Template not found.",
////                    content = @Content(
////                            schema = @Schema(implementation = ApiErrorDTO.class)
////                    )
////            )
////    })
////    @DeleteMapping("/{id}")
////    @PreAuthorize("hasRole('ADMIN')")
////    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
////        jobTemplateService.deleteTemplate(id);
////        return ResponseEntity.noContent().build();
////    }
////}
//package nl.avflexologic.wbje.controllers;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.*;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import io.swagger.v3.oas.annotations.responses.*;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
//import nl.avflexologic.wbje.services.JobTemplateService;
//
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
///**
// * Controller responsible for the lifecycle of job templates stored on disk.
// * A template consists of a binary JSON file + a human-readable template name.
// * Metadata is stored in the database; the raw file is stored on disk.
// */
//@RestController
//@RequestMapping("/job-templates")
//@Tag(name = "Job Templates", description = "Endpoints for uploading, retrieving and deleting job templates.")
//public class JobTemplateController {
//
//    private final JobTemplateService jobTemplateService;
//
//    public JobTemplateController(JobTemplateService jobTemplateService) {
//        this.jobTemplateService = jobTemplateService;
//    }
//
//    // ========================================================================
//    // POST — UPLOAD TEMPLATE (ADMIN ONLY → service)
//    // ========================================================================
//
//    @Operation(
//            summary = "Upload a job template",
//            description = "Receives a template file + name using multipart/form-data.",
//            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "201",
//                    description = "Template successfully stored.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "TemplateCreatedExample",
//                                    value = """
//                                            {
//                                              "id": 1,
//                                              "originalFileName": "wbje-template.json",
//                                              "contentType": "application/json",
//                                              "fileSize": 4096,
//                                              "uploadedAt": "2025-12-22T10:00:00",
//                                              "templateName": "Customer A Default"
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid multipart request.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "UploadErrorExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 400,
//                                              "error": "Bad Request",
//                                              "message": "Invalid file upload request.",
//                                              "path": "/job-templates",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @RequestBody(
//            description = "Multipart request containing the template JSON file and its logical name.",
//            required = true,
//            content = @Content(
//                    mediaType = "multipart/form-data",
//                    schema = @Schema(type = "object"),
//                    schemaProperties = {
//                            @SchemaProperty(
//                                    name = "file",
//                                    schema = @Schema(
//                                            type = "string",
//                                            format = "binary",
//                                            description = "JSON file containing the full job template."
//                                    )
//                            ),
//                            @SchemaProperty(
//                                    name = "name",
//                                    schema = @Schema(
//                                            type = "string",
//                                            description = "Human-readable name for this template."
//                                    )
//                            )
//                    },
//                    examples = @ExampleObject(
//                            name = "TemplateUploadExample",
//                            value = "{ \"file\": \"(binary JSON)\", \"name\": \"Customer X Default\" }"
//                    )
//            )
//    )
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<JobTemplateResponseDTO> uploadTemplate(
//            @RequestPart("file") MultipartFile file,
//            @RequestPart("name") String templateName
//    ) {
//        JobTemplateResponseDTO saved = jobTemplateService.storeTemplate(file, templateName);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}/download")
//                .buildAndExpand(saved.id())
//                .toUri();
//
//        return ResponseEntity.created(location).body(saved);
//    }
//
//    // ========================================================================
//    // GET ALL — ADMIN + USER → service + operator
//    // ========================================================================
//
//    @Operation(
//            summary = "List job templates",
//            description = "Returns metadata for all stored job templates.",
//            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "Template metadata successfully retrieved.",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = JobTemplateResponseDTO.class),
//                    examples = @ExampleObject(
//                            name = "TemplateListExample",
//                            value = """
//                                    [
//                                      {
//                                        "id": 1,
//                                        "originalFileName": "wbje-template.json",
//                                        "contentType": "application/json",
//                                        "fileSize": 4096,
//                                        "uploadedAt": "2025-12-22T10:00:00",
//                                        "templateName": "Default Template"
//                                      }
//                                    ]
//                                    """
//                    )
//            )
//    )
//    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    public List<JobTemplateResponseDTO> getAllTemplates() {
//        return jobTemplateService.getAllTemplates();
//    }
//
//    // ========================================================================
//    // GET ONE — ADMIN + USER → service + operator
//    // ========================================================================
//
//    @Operation(
//            summary = "Get a template by id",
//            description = "Returns metadata for a specific job template.",
//            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Template metadata retrieved.",
//                    content = @Content(
//                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "TemplateMetadataExample",
//                                    value = """
//                                            {
//                                              "id": 1,
//                                              "originalFileName": "wbje-template.json",
//                                              "contentType": "application/json",
//                                              "fileSize": 4096,
//                                              "uploadedAt": "2025-12-22T10:00:00",
//                                              "templateName": "Default Template"
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Template not found.",
//                    content = @Content(
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "TemplateNotFoundExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Template not found for id: 99",
//                                              "path": "/job-templates/99",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    public JobTemplateResponseDTO getTemplate(@PathVariable Long id) {
//        return jobTemplateService.getTemplate(id);
//    }
//
//    // ========================================================================
//    // DOWNLOAD — ADMIN + USER → service + operator
//    // ========================================================================
//
//    @Operation(
//            summary = "Download template file",
//            description = "Downloads the JSON file belonging to the template.",
//            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Template file returned.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            examples = @ExampleObject(
//                                    name = "TemplateFileExample",
//                                    value = """
//                                            {
//                                              "jobNumber": "JB-1001",
//                                              "jobName": "Demo Job from Template",
//                                              "cylinders": [ ... ]
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Template file not found.",
//                    content = @Content(
//                            schema = @Schema(implementation = ApiErrorDTO.class)
//                    )
//            )
//    })
//    @GetMapping("/{id}/download")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    public ResponseEntity<Resource> downloadTemplate(@PathVariable Long id) {
//        JobTemplateResponseDTO metadata = jobTemplateService.getTemplate(id);
//        Resource file = jobTemplateService.loadTemplateFile(id);
//
//        MediaType mediaType;
//        try {
//            mediaType = MediaType.parseMediaType(metadata.contentType());
//        } catch (Exception e) {
//            mediaType = MediaType.APPLICATION_OCTET_STREAM;
//        }
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(
//                        HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + metadata.originalFileName() + "\""
//                )
//                .body(file);
//    }
//
//    // ========================================================================
//    // DELETE — ADMIN ONLY → service only
//    // ========================================================================
//
//    @Operation(
//            summary = "Delete a template",
//            description = "Deletes both stored metadata and the template file.",
//            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "204",
//                    description = "Template deleted."
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Template not found.",
//                    content = @Content(
//                            schema = @Schema(implementation = ApiErrorDTO.class)
//                    )
//            )
//    })
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
//        jobTemplateService.deleteTemplate(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.jobTemplate.JobTemplateResponseDTO;
import nl.avflexologic.wbje.services.JobTemplateService;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller responsible for the lifecycle of job templates stored on disk.
 * A template consists of a binary JSON file + a human-readable template name.
 * Metadata is stored in the database; the raw file is stored on disk.
 */
@RestController
@RequestMapping("/job-templates")
@Tag(
        name = "Job Templates",
        description = "Endpoints for uploading, retrieving and deleting job templates. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER)."
)
public class JobTemplateController {

    private final JobTemplateService jobTemplateService;

    public JobTemplateController(JobTemplateService jobTemplateService) {
        this.jobTemplateService = jobTemplateService;
    }

    // ========================================================================
    // POST — UPLOAD TEMPLATE (ADMIN ONLY → service)
    // ========================================================================

    @Operation(
            summary = "Upload a job template",
            description = "Receives a template file + name using multipart/form-data. Accessible roles: service (ROLE_ADMIN).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Template successfully stored.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TemplateCreatedExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "originalFileName": "wbje-template.json",
                                              "contentType": "application/json",
                                              "fileSize": 4096,
                                              "uploadedAt": "2025-12-22T10:00:00",
                                              "templateName": "Customer A Default"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid multipart request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "UploadErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Invalid file upload request.",
                                              "path": "/job-templates",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @RequestBody(
            description = "Multipart request containing the template JSON file and its logical name.",
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(type = "object"),
                    schemaProperties = {
                            @SchemaProperty(
                                    name = "file",
                                    schema = @Schema(
                                            type = "string",
                                            format = "binary",
                                            description = "JSON file containing the full job template."
                                    )
                            ),
                            @SchemaProperty(
                                    name = "name",
                                    schema = @Schema(
                                            type = "string",
                                            description = "Human-readable name for this template."
                                    )
                            )
                    },
                    examples = @ExampleObject(
                            name = "TemplateUploadExample",
                            value = "{ \"file\": \"(binary JSON)\", \"name\": \"Customer X Default\" }"
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
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

    // ========================================================================
    // GET ALL — ADMIN + USER → service + operator
    // ========================================================================

    @Operation(
            summary = "List job templates",
            description = "Returns metadata for all stored job templates. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponse(
            responseCode = "200",
            description = "Template metadata successfully retrieved.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JobTemplateResponseDTO.class),
                    examples = @ExampleObject(
                            name = "TemplateListExample",
                            value = """
                                    [
                                      {
                                        "id": 1,
                                        "originalFileName": "wbje-template.json",
                                        "contentType": "application/json",
                                        "fileSize": 4096,
                                        "uploadedAt": "2025-12-22T10:00:00",
                                        "templateName": "Default Template"
                                      }
                                    ]
                                    """
                    )
            )
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<JobTemplateResponseDTO> getAllTemplates() {
        return jobTemplateService.getAllTemplates();
    }

    // ========================================================================
    // GET ONE — ADMIN + USER → service + operator
    // ========================================================================

    @Operation(
            summary = "Get a template by id",
            description = "Returns metadata for a specific job template. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Template metadata retrieved.",
                    content = @Content(
                            schema = @Schema(implementation = JobTemplateResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TemplateMetadataExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "originalFileName": "wbje-template.json",
                                              "contentType": "application/json",
                                              "fileSize": 4096,
                                              "uploadedAt": "2025-12-22T10:00:00",
                                              "templateName": "Default Template"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Template not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "TemplateNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Template not found for id: 99",
                                              "path": "/job-templates/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public JobTemplateResponseDTO getTemplate(@PathVariable Long id) {
        return jobTemplateService.getTemplate(id);
    }

    // ========================================================================
    // DOWNLOAD — ADMIN + USER → service + operator
    // ========================================================================

    @Operation(
            summary = "Download template file",
            description = "Downloads the JSON file belonging to the template. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Template file returned.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "TemplateFileExample",
                                    value = """
                                            {
                                              "jobNumber": "JB-1001",
                                              "jobName": "Demo Job from Template",
                                              "cylinders": [ ... ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Template file not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metadata.originalFileName() + "\""
                )
                .body(file);
    }

    // ========================================================================
    // DELETE — ADMIN ONLY → service
    // ========================================================================

    @Operation(
            summary = "Delete a template",
            description = "Deletes both stored metadata and the template file. Accessible roles: service (ROLE_ADMIN).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Template deleted."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Template not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        jobTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
