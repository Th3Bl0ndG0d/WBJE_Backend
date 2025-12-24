package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.services.JobService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes job-related operations for the WBJE API.
 */
@RestController
@RequestMapping("/jobs")
@Tag(name = "Jobs", description = "Endpoints for managing jobs in the Web Based Job Editor.")
public class JobController {

    private final JobService jobService;

    /**
     * Constructs a new instance of the JobController with the required JobService dependency.
     */
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Creates a new job based on the supplied request payload.
     */
    @Operation(
            summary = "Create a new job",
            description = "Creates a new job based on the supplied request payload."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for the request body.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/jobs",
                                              "fieldErrors": {
                                                "jobDate": "jobDate is required."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public JobResponseDTO createJob(@Valid @RequestBody JobRequestDTO request) {
        return jobService.createJob(request);
    }

    /**
     * Returns a single job for the given identifier.
     */
    @Operation(
            summary = "Get a job by id",
            description = "Returns a single job for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Job not found for id: 42",
                                              "path": "/jobs/42",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public JobResponseDTO getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }
}
