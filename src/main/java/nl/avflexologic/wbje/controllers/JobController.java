package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.services.CylinderService;
import nl.avflexologic.wbje.services.JobAssemblyService;
import nl.avflexologic.wbje.services.JobService;
import nl.avflexologic.wbje.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@Tag(
        name = "Jobs",
        description = "Endpoints for managing jobs in the Web Based Job Editor. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER)."
)
public class JobController {

    private final JobService jobService;
    private final JobAssemblyService jobAssemblyService;
    private final CylinderService cylinderService;
    private final ReportService reportService;

    public JobController(
            JobService jobService,
            JobAssemblyService jobAssemblyService,
            CylinderService cylinderService,
            ReportService reportService
    ) {
        this.jobService = jobService;
        this.jobAssemblyService = jobAssemblyService;
        this.cylinderService = cylinderService;
        this.reportService = reportService;
    }

    // ---------------------------------------------------------
    // CREATE — service + operator
    // ---------------------------------------------------------

    @Operation(
            summary = "Create a new job",
            description = "Creates a new job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "JobCreatedExample",
                                    value = """
                                            {
                                              "id": 42,
                                              "jobNr": "JOB-2025-001",
                                              "jobName": "Sleeve alignment test job",
                                              "jobDate": "2025-01-10",
                                              "repeatLength": 635.0
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) {
        JobResponseDTO response = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------------------
    // GET BY ID — service + operator
    // ---------------------------------------------------------

    @Operation(
            summary = "Get a job by id",
            description = "Returns a job for the given identifier. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully retrieved.",
                    content = @Content(schema = @Schema(implementation = JobResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long id) {
        JobResponseDTO response = jobService.getJobById(id);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // LIST ALL — service + operator
    // ---------------------------------------------------------

    @Operation(
            summary = "Get all jobs",
            description = "Returns all jobs. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponse(
            responseCode = "200",
            description = "List successfully retrieved.",
            content = @Content(schema = @Schema(implementation = JobResponseDTO.class))
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // ---------------------------------------------------------
    // UPDATE — service + operator
    // ---------------------------------------------------------

    @Operation(
            summary = "Update a job",
            description = "Updates an existing job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job updated."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Job not found.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequestDTO request
    ) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    // ---------------------------------------------------------
    // DELETE — service + operator
    // ---------------------------------------------------------

    @Operation(
            summary = "Delete a job",
            description = "Deletes a job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job deleted."),
            @ApiResponse(responseCode = "404", description = "Job not found.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
    //    ////    @PostMapping("/full")
//    ////    public ResponseEntity<JobResponseDTO> createFullJobFromJson(
//    ////            @RequestBody Map<String, Object> body
//    ////    ) {
//    ////        JobResponseDTO response = jobAssemblyService.createFullJob(body);
//    ////        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    ////    }
}
