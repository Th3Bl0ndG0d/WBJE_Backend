//package nl.avflexologic.wbje.controllers;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.ExampleObject;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
//import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
//import nl.avflexologic.wbje.services.CylinderService;
//import nl.avflexologic.wbje.services.JobAssemblyService;
//import nl.avflexologic.wbje.services.JobService;
//import nl.avflexologic.wbje.services.ReportService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * REST controller that exposes Job operations for the WBJE API.
// *
// * This controller provides the HTTP entry points for CRUD operations on jobs.
// * All domain logic is delegated to the service layer. Validation is enforced via
// * Bean Validation on request DTOs, while error responses are handled centrally
// * by the global exception handling mechanism.
// */
//@RestController
//@RequestMapping("/jobs")
//@Tag(name = "Jobs", description = "Endpoints for managing jobs in the Web Based Job Editor.")
//public class JobController {
//
////    private final JobService jobService;
//
//    /**
//     * Constructs a new instance of the JobController with the required service dependency.
//     *
//     * @param jobService service responsible for job-related business logic
//     */
//    private final JobService jobService;
//    private final JobAssemblyService jobAssemblyService;
//
//    public JobController(
//            JobService jobService, JobAssemblyService jobAssemblyService
//    ) {
//        this.jobService = jobService;
//        this.jobAssemblyService = jobAssemblyService;
//    }
//
//    /**
//     * Creates a new job based on the supplied request payload.
//     *
//     * @param request validated job request payload
//     * @return the created job as a response DTO
//     */
//    @Operation(
//            summary = "Create a new job",
//            description = "Creates a new job based on the supplied request payload."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Job successfully created.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobResponseExample",
//                                    value = """
//                                            {
//                                              "id": 1,
//                                              "jobNumber": "JB-1001",
//                                              "jobDate": "2025-12-22T10:00:00",
//                                              "jobName": "WBJE Demo",
//                                              "info": "Initial setup for customer order.",
//                                              "cylinderWidth": 120,
//                                              "cylinderCircumference": 800,
//                                              "noteInfo": "Note text"
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Validation failed for the request body.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "ValidationErrorExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 400,
//                                              "error": "Bad Request",
//                                              "message": "Validation failed for one or more fields.",
//                                              "path": "/jobs",
//                                              "fieldErrors": {
//                                                "jobDate": "jobDate is required."
//                                              }
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @PostMapping
//    public JobResponseDTO createJob(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Job request payload.",
//                    required = true,
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobRequestDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobRequestExample",
//                                    value = """
//                                            {
//                                              "jobNumber": "JB-1001",
//                                              "jobDate": "2025-12-22T10:00:00",
//                                              "jobName": "WBJE Demo",
//                                              "info": "Initial setup for customer order.",
//                                              "cylinderWidth": 120,
//                                              "cylinderCircumference": 800,
//                                              "noteInfo": "Note text"
//                                            }
//                                            """
//                            )
//                    )
//            )
//            @Valid @RequestBody JobRequestDTO request
//    ) {
//        return jobService.createJob(request);
//    }
//
//    /**
//     * Returns a single job for the given identifier.
//     *
//     * @param id identifier of the job to retrieve
//     * @return the requested job as a response DTO
//     */
//    @Operation(
//            summary = "Get a job by id",
//            description = "Returns a single job for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Job successfully retrieved.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobResponseExample",
//                                    value = """
//                                            {
//                                              "id": 1,
//                                              "jobNumber": "JB-1001",
//                                              "jobDate": "2025-12-22T10:00:00",
//                                              "jobName": "WBJE Demo",
//                                              "info": "Initial setup for customer order.",
//                                              "cylinderWidth": 120,
//                                              "cylinderCircumference": 800,
//                                              "noteInfo": "Note text"
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Job with the given id was not found.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "NotFoundExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Job not found for id: 42",
//                                              "path": "/jobs/42",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @GetMapping("/{id}")
//    public JobResponseDTO getJobById(@PathVariable Long id) {
//        return jobService.getJobById(id);
//    }
//
//    /**
//     * Returns all jobs.
//     *
//     * @return list of all jobs as response DTOs
//     */
//    @Operation(
//            summary = "Get all jobs",
//            description = "Returns all jobs."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Jobs successfully retrieved.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobListExample",
//                                    value = """
//                                            [
//                                              {
//                                                "id": 1,
//                                                "jobNumber": "JB-1001",
//                                                "jobDate": "2025-12-22T10:00:00",
//                                                "jobName": "WBJE Demo",
//                                                "info": "Initial setup for customer order.",
//                                                "cylinderWidth": 120,
//                                                "cylinderCircumference": 800,
//                                                "noteInfo": "Note text"
//                                              },
//                                              {
//                                                "id": 2,
//                                                "jobNumber": "JB-1002",
//                                                "jobDate": "2025-12-23T09:30:00",
//                                                "jobName": "WBJE Production",
//                                                "info": "Second production run.",
//                                                "cylinderWidth": 130,
//                                                "cylinderCircumference": 820,
//                                                "noteInfo": null
//                                              }
//                                            ]
//                                            """
//                            )
//                    )
//            )
//    })
//    @GetMapping
//    public List<JobResponseDTO> getAllJobs() {
//        return jobService.getAllJobs();
//    }
//
//    /**
//     * Updates an existing job for the given identifier.
//     *
//     * The update operation replaces the editable fields of the existing job with the
//     * values supplied in the request payload, subject to validation constraints.
//     *
//     * @param id identifier of the job to update
//     * @param request validated job request payload
//     * @return the updated job as a response DTO
//     */
//    @Operation(
//            summary = "Update a job",
//            description = "Updates an existing job for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Job successfully updated.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobResponseDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobUpdatedExample",
//                                    value = """
//                                            {
//                                              "id": 1,
//                                              "jobNumber": "JB-1001",
//                                              "jobDate": "2025-12-22T10:00:00",
//                                              "jobName": "WBJE Demo",
//                                              "info": "Updated info after customer feedback.",
//                                              "cylinderWidth": 120,
//                                              "cylinderCircumference": 800,
//                                              "noteInfo": "Updated note text"
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Validation failed for the request body.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "ValidationErrorExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 400,
//                                              "error": "Bad Request",
//                                              "message": "Validation failed for one or more fields.",
//                                              "path": "/jobs/1",
//                                              "fieldErrors": {
//                                                "cylinderWidth": "cylinderWidth must be a positive number."
//                                              }
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Job with the given id was not found.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "NotFoundExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Job not found for id: 99",
//                                              "path": "/jobs/99",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @PutMapping("/{id}")
//    public JobResponseDTO updateJob(
//            @PathVariable Long id,
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Job request payload.",
//                    required = true,
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = JobRequestDTO.class),
//                            examples = @ExampleObject(
//                                    name = "JobUpdateRequestExample",
//                                    value = """
//                                            {
//                                              "jobNumber": "JB-1001",
//                                              "jobDate": "2025-12-22T10:00:00",
//                                              "jobName": "WBJE Demo",
//                                              "info": "Updated info after customer feedback.",
//                                              "cylinderWidth": 120,
//                                              "cylinderCircumference": 800,
//                                              "noteInfo": "Updated note text"
//                                            }
//                                            """
//                            )
//                    )
//            )
//            @Valid @RequestBody JobRequestDTO request
//    ) {
//        return jobService.updateJob(id, request);
//    }
//
//    /**
//     * Deletes a job for the given identifier.
//     *
//     * Deletion is executed by identifier. If the job does not exist, a not-found
//     * response is returned by the centralized exception handler.
//     *
//     * @param id identifier of the job to delete
//     */
//    @Operation(
//            summary = "Delete a job",
//            description = "Deletes a job for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Job successfully deleted."
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Job with the given id was not found.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    name = "NotFoundExample",
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Job not found for id: 99",
//                                              "path": "/jobs/99",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @DeleteMapping("/{id}")
//    public void deleteJob(@PathVariable Long id) {
//        jobService.deleteJob(id);
//    }
//
//
////    @PostMapping("/full")
////    public ResponseEntity<JobResponseDTO> createFullJobFromJson(
////            @RequestBody Map<String, Object> body
////    ) {
////        JobResponseDTO response = jobAssemblyService.createFullJob(body);
////        return ResponseEntity.status(HttpStatus.CREATED).body(response);
////    }
//
//
//}
package nl.avflexologic.wbje.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.util.Map;

/**
 * REST controller that exposes Job operations for the WBJE API.
 *
 * This controller provides the HTTP entry points for CRUD operations on jobs.
 * All domain logic is delegated to the service layer. Validation is enforced via
 * Bean Validation on request DTOs, while error responses are handled centrally
 * by the global exception handling mechanism.
 */
@RestController
@RequestMapping("/jobs")
@Tag(name = "Jobs", description = "Endpoints for managing jobs in the Web Based Job Editor.")
public class JobController {

//    private final JobService jobService;

    /**
     * Constructs a new instance of the JobController with the required service dependency.
     *
     * @param jobService service responsible for job-related business logic
     */
    private final JobService jobService;
    private final JobAssemblyService jobAssemblyService;

    public JobController(
            JobService jobService, JobAssemblyService jobAssemblyService
    ) {
        this.jobService = jobService;
        this.jobAssemblyService = jobAssemblyService;
    }

    /**
     * Creates a new job based on the supplied request payload.
     *
     * @param request validated job request payload
     * @return the created job as a response DTO
     */
    @Operation(
            summary = "Create a new job",
            description = "Creates a new job based on the supplied request payload.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "JobResponseExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "jobNumber": "JB-1001",
                                              "jobDate": "2025-12-22T10:00:00",
                                              "jobName": "WBJE Demo",
                                              "info": "Initial setup for customer order.",
                                              "cylinderWidth": 120,
                                              "cylinderCircumference": 800,
                                              "noteInfo": "Note text"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for the request body.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ValidationErrorExample",
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
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public JobResponseDTO createJob(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Job request payload.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "JobRequestExample",
                                    value = """
                                            {
                                              "jobNumber": "JB-1001",
                                              "jobDate": "2025-12-22T10:00:00",
                                              "jobName": "WBJE Demo",
                                              "info": "Initial setup for customer order.",
                                              "cylinderWidth": 120,
                                              "cylinderCircumference": 800,
                                              "noteInfo": "Note text"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody JobRequestDTO request
    ) {
        return jobService.createJob(request);
    }

    /**
     * Returns a single job for the given identifier.
     *
     * @param id identifier of the job to retrieve
     * @return the requested job as a response DTO
     */
    @Operation(
            summary = "Get a job by id",
            description = "Returns a single job for the given identifier.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "JobResponseExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "jobNumber": "JB-1001",
                                              "jobDate": "2025-12-22T10:00:00",
                                              "jobName": "WBJE Demo",
                                              "info": "Initial setup for customer order.",
                                              "cylinderWidth": 120,
                                              "cylinderCircumference": 800,
                                              "noteInfo": "Note text"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "NotFoundExample",
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
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public JobResponseDTO getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    /**
     * Returns all jobs.
     *
     * @return list of all jobs as response DTOs
     */
    @Operation(
            summary = "Get all jobs",
            description = "Returns all jobs.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Jobs successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "JobListExample",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "jobNumber": "JB-1001",
                                                "jobDate": "2025-12-22T10:00:00",
                                                "jobName": "WBJE Demo",
                                                "info": "Initial setup for customer order.",
                                                "cylinderWidth": 120,
                                                "cylinderCircumference": 800,
                                                "noteInfo": "Note text"
                                              },
                                              {
                                                "id": 2,
                                                "jobNumber": "JB-1002",
                                                "jobDate": "2025-12-23T09:30:00",
                                                "jobName": "WBJE Production",
                                                "info": "Second production run.",
                                                "cylinderWidth": 130,
                                                "cylinderCircumference": 820,
                                                "noteInfo": null
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    /**
     * Updates an existing job for the given identifier.
     *
     * The update operation replaces the editable fields of the existing job with the
     * values supplied in the request payload, subject to validation constraints.
     *
     * @param id identifier of the job to update
     * @param request validated job request payload
     * @return the updated job as a response DTO
     */
    @Operation(
            summary = "Update a job",
            description = "Updates an existing job for the given identifier.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "JobUpdatedExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "jobNumber": "JB-1001",
                                              "jobDate": "2025-12-22T10:00:00",
                                              "jobName": "WBJE Demo",
                                              "info": "Updated info after customer feedback.",
                                              "cylinderWidth": 120,
                                              "cylinderCircumference": 800,
                                              "noteInfo": "Updated note text"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for the request body.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/jobs/1",
                                              "fieldErrors": {
                                                "cylinderWidth": "cylinderWidth must be a positive number."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "NotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Job not found for id: 99",
                                              "path": "/jobs/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public JobResponseDTO updateJob(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Job request payload.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "JobUpdateRequestExample",
                                    value = """
                                            {
                                              "jobNumber": "JB-1001",
                                              "jobDate": "2025-12-22T10:00:00",
                                              "jobName": "WBJE Demo",
                                              "info": "Updated info after customer feedback.",
                                              "cylinderWidth": 120,
                                              "cylinderCircumference": 800,
                                              "noteInfo": "Updated note text"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody JobRequestDTO request
    ) {
        return jobService.updateJob(id, request);
    }

    /**
     * Deletes a job for the given identifier.
     *
     * Deletion is executed by identifier. If the job does not exist, a not-found
     * response is returned by the centralized exception handler.
     *
     * @param id identifier of the job to delete
     */
    @Operation(
            summary = "Delete a job",
            description = "Deletes a job for the given identifier.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job successfully deleted."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "NotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Job not found for id: 99",
                                              "path": "/jobs/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }


//    @PostMapping("/full")
//    public ResponseEntity<JobResponseDTO> createFullJobFromJson(
//            @RequestBody Map<String, Object> body
//    ) {
//        JobResponseDTO response = jobAssemblyService.createFullJob(body);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }


}
