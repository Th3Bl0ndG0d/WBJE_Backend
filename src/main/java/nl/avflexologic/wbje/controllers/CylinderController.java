package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.services.CylinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/{jobId}/cylinders")
@Tag(
        name = "Cylinders",
        description = "Endpoints for managing cylinders within a job. Accessible roles: service (ROLE_ADMIN) and operator (ROLE_USER)."
)
public class CylinderController {

    private final CylinderService cylinderService;

    public CylinderController(CylinderService cylinderService) {
        this.cylinderService = cylinderService;
    }

    // ========================================================================
    // CREATE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Create a new cylinder",
            description = "Creates a new cylinder in the specified job. Cylinder numbering must remain unique per job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cylinder successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CylinderResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderCreatedExample",
                                    value = """
                                            {
                                              "id": 10,
                                              "cylinderNr": 1,
                                              "color": "Cyan",
                                              "cylinderInfo": "TechSleeve",
                                              "jobId": 100,
                                              "tapeSpecId": 10
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
                                    name = "CylinderValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/jobs/100/cylinders",
                                              "fieldErrors": {
                                                "cylinderNr": "cylinderNr is required.",
                                                "tapeSpecId": "tapeSpecId is required."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderJobNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Job not found for id: 100",
                                              "path": "/jobs/100/cylinders",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Domain validation failed (e.g. duplicate cylinderNr or invalid TapeSpec relation)",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    })
    @RequestBody(
            description = "Cylinder request payload for creating a new cylinder within a job.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CylinderRequestDTO.class),
                    examples = @ExampleObject(
                            name = "CylinderRequestExample",
                            value = """
                                    {
                                      "cylinderNr": 1,
                                      "color": "Cyan",
                                      "cylinderInfo": "TechSleeve",
                                      "tapeSpecId": 10
                                    }
                                    """
                    )
            )
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CylinderResponseDTO> createCylinder(
            @PathVariable Long jobId,
            @Valid @org.springframework.web.bind.annotation.RequestBody CylinderRequestDTO request
    ) {
        CylinderResponseDTO response = cylinderService.createCylinder(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================================================================
    // READ ALL (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Get all cylinders for a job",
            description = "Returns all cylinders associated with the given job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinders retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CylinderResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "CylinderListExample",
                                    value = """
                                            [
                                              {
                                                "id": 10,
                                                "cylinderNr": 1,
                                                "color": "Cyan",
                                                "cylinderInfo": "TechSleeve",
                                                "jobId": 100,
                                                "tapeSpecId": 10
                                              },
                                              {
                                                "id": 11,
                                                "cylinderNr": 2,
                                                "color": "Cyan",
                                                "cylinderInfo": "TechSleeve",
                                                "jobId": 100,
                                                "tapeSpecId": 10
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderJobNotFoundListExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Job not found for id: 100",
                                              "path": "/jobs/100/cylinders",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<CylinderResponseDTO>> getCylindersByJob(
            @PathVariable Long jobId
    ) {
        List<CylinderResponseDTO> cylinders = cylinderService.getCylindersByJob(jobId);
        return ResponseEntity.ok(cylinders);
    }

    // ========================================================================
    // READ ONE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Get a single cylinder",
            description = "Returns a specific cylinder by id, scoped to the given job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinder retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CylinderResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "SingleCylinderExample",
                                    value = """
                                            {
                                              "id": 10,
                                              "cylinderNr": 1,
                                              "color": "Cyan",
                                              "cylinderInfo": "TechSleeve",
                                              "jobId": 100,
                                              "tapeSpecId": 10
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder or job not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Cylinder not found for id: 10",
                                              "path": "/jobs/100/cylinders/10",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{cylinderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CylinderResponseDTO> getCylinderById(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId
    ) {
        CylinderResponseDTO response = cylinderService.getCylinderById(jobId, cylinderId);
        return ResponseEntity.ok(response);
    }

    // ========================================================================
    // UPDATE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Update a cylinder",
            description = "Updates the fields of an existing cylinder. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinder updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CylinderResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderUpdatedExample",
                                    value = """
                                            {
                                              "id": 10,
                                              "cylinderNr": 1,
                                              "color": "Cyan",
                                              "cylinderInfo": "TechSleeve",
                                              "jobId": 100,
                                              "tapeSpecId": 10
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
                                    name = "CylinderUpdateValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/jobs/100/cylinders/10",
                                              "fieldErrors": {
                                                "cylinderNr": "cylinderNr is required.",
                                                "tapeSpecId": "tapeSpecId is required."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder or job not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderUpdateNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Cylinder not found for id: 10",
                                              "path": "/jobs/100/cylinders/10",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Domain validation failed (invalid updates or incompatible specs)",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    })
    @RequestBody(
            description = "Cylinder request payload for updating an existing cylinder.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CylinderRequestDTO.class),
                    examples = @ExampleObject(
                            name = "CylinderUpdateRequestExample",
                            value = """
                                    {
                                      "cylinderNr": 1,
                                      "color": "Cyan",
                                      "cylinderInfo": "TechSleeve",
                                      "tapeSpecId": 10
                                    }
                                    """
                    )
            )
    )
    @PutMapping("/{cylinderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CylinderResponseDTO> updateCylinder(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId,
            @Valid @org.springframework.web.bind.annotation.RequestBody CylinderRequestDTO request
    ) {
        CylinderResponseDTO response = cylinderService.updateCylinder(jobId, cylinderId, request);
        return ResponseEntity.ok(response);
    }

    // ========================================================================
    // DELETE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Delete a cylinder",
            description = "Deletes a cylinder belonging to the specified job. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Cylinder successfully deleted."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder or job not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "CylinderDeleteNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Cylinder not found for id: 10",
                                              "path": "/jobs/100/cylinders/10",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{cylinderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> deleteCylinder(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId
    ) {
        cylinderService.deleteCylinder(jobId, cylinderId);
        return ResponseEntity.noContent().build();
    }
}
