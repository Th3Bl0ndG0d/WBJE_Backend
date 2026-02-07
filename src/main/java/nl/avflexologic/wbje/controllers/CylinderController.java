//package nl.avflexologic.wbje.controllers;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
//import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
//import nl.avflexologic.wbje.services.CylinderService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(
//        name = "Cylinders",
//        description = "Operations related to cylinders within a job"
//)
//@RestController
//@RequestMapping("/jobs/{jobId}/cylinders")
//public class CylinderController {
//
//    private final CylinderService cylinderService;
//
//    public CylinderController(CylinderService cylinderService) {
//        this.cylinderService = cylinderService;
//    }
//
//    @Operation(
//            summary = "Create a cylinder for a job",
//            description = "Creates a new cylinder within the given job. " +
//                    "Cylinder numbers must be unique per job."
//    )
//    @PostMapping
//    public ResponseEntity<CylinderResponseDTO> createCylinder(
//            @PathVariable Long jobId,
//            @Valid @RequestBody CylinderRequestDTO request
//    ) {
//        CylinderResponseDTO response =
//                cylinderService.createCylinder(jobId, request);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @Operation(
//            summary = "Get all cylinders for a job",
//            description = "Returns all cylinders that belong to the specified job."
//    )
//    @GetMapping
//    public ResponseEntity<List<CylinderResponseDTO>> getCylindersByJob(
//            @PathVariable Long jobId
//    ) {
//        return ResponseEntity.ok(
//                cylinderService.getCylindersByJob(jobId)
//        );
//    }
//
//    @Operation(
//            summary = "Get a single cylinder",
//            description = "Returns a specific cylinder by id, scoped to the given job."
//    )
//    @GetMapping("/{cylinderId}")
//    public ResponseEntity<CylinderResponseDTO> getCylinderById(
//            @PathVariable Long jobId,
//            @PathVariable Long cylinderId
//    ) {
//        return ResponseEntity.ok(
//                cylinderService.getCylinderById(jobId, cylinderId)
//        );
//    }
//}
package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
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
@Tag(name = "Cylinders", description = "Endpoints for managing cylinders within a job.")
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
            description = "Creates a new cylinder in the specified job. Cylinder numbering must remain unique.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
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
                                      "cylinderNumber": 3,
                                      "webPosition": 125.0,
                                      "diameter": 620.5,
                                      "sidelay": 2.5,
                                      "reports": []
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Validation failed.",
                                      "path": "/jobs/1/cylinders",
                                      "fieldErrors": {
                                        "cylinderNumber": "must be positive"
                                      }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Job not found for id: 1",
                                      "path": "/jobs/1/cylinders",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cylinder number already exists.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 409,
                                      "error": "Conflict",
                                      "message": "Cylinder number already exists in job.",
                                      "path": "/jobs/1/cylinders",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @RequestBody(
            description = "Cylinder request payload.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CylinderRequestDTO.class),
                    examples = @ExampleObject(
                            value = """
                            {
                              "cylinderNumber": 3,
                              "webPosition": 125.0,
                              "diameter": 620.5,
                              "sidelay": 2.5
                            }
                            """
                    )
            )
    )
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<CylinderResponseDTO> createCylinder(
            @PathVariable Long jobId,
            @Valid @RequestBody CylinderRequestDTO request
    ) {
        CylinderResponseDTO response = cylinderService.createCylinder(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ========================================================================
    // READ ALL (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Get all cylinders for a job",
            description = "Returns all cylinders associated with the given job.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinders retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CylinderResponseDTO.class)),
                            examples = @ExampleObject(
                                    value = """
                                    [
                                      {
                                        "id": 10,
                                        "cylinderNumber": 3,
                                        "webPosition": 125.0,
                                        "diameter": 620.5,
                                        "sidelay": 2.5,
                                        "reports": []
                                      },
                                      {
                                        "id": 11,
                                        "cylinderNumber": 4,
                                        "webPosition": 210.0,
                                        "diameter": 618.0,
                                        "sidelay": 1.5,
                                        "reports": []
                                      }
                                    ]
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Job not found for id: 1",
                                      "path": "/jobs/1/cylinders",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<CylinderResponseDTO> getCylindersByJob(@PathVariable Long jobId) {
        return cylinderService.getCylindersByJob(jobId);
    }

    // ========================================================================
    // READ ONE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Get a cylinder by id",
            description = "Returns a single cylinder identified by its ID.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinder retrieved successfully.",
                    content = @Content(
                            schema = @Schema(implementation = CylinderResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "id": 10,
                                      "cylinderNumber": 3,
                                      "webPosition": 125.0,
                                      "diameter": 620.5,
                                      "sidelay": 2.5,
                                      "reports": []
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Cylinder not found for id: 10",
                                      "path": "/jobs/1/cylinders/10",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{cylinderId}")
    public CylinderResponseDTO getCylinderById(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId
    ) {
        return cylinderService.getCylinderById(jobId, cylinderId);
    }

    // ========================================================================
    // UPDATE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Update a cylinder",
            description = "Updates the fields of an existing cylinder.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @RequestBody(
            description = "Cylinder update payload.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CylinderRequestDTO.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "cylinderNumber": 3,
                                      "webPosition": 130.0,
                                      "diameter": 621.0,
                                      "sidelay": 1.5
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cylinder updated successfully.",
                    content = @Content(
                            schema = @Schema(implementation = CylinderResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "id": 10,
                                      "cylinderNumber": 3,
                                      "webPosition": 130.0,
                                      "diameter": 621.0,
                                      "sidelay": 1.5,
                                      "reports": []
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Validation failed.",
                                      "path": "/jobs/1/cylinders/10",
                                      "fieldErrors": {
                                        "webPosition": "must be positive"
                                      }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Cylinder not found for id: 10",
                                      "path": "/jobs/1/cylinders/10",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{cylinderId}")
    public CylinderResponseDTO updateCylinder(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId,
            @Valid @RequestBody CylinderRequestDTO request
    ) {
        return cylinderService.updateCylinder(jobId, cylinderId, request);
    }

    // ========================================================================
    // DELETE (ADMIN + USER)
    // ========================================================================

    @Operation(
            summary = "Delete a cylinder",
            description = "Deletes a cylinder belonging to the specified job.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Cylinder successfully deleted.",
                    content = @Content(examples = @ExampleObject(
                            value = """
                            // No content returned
                            """
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder not found.",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "timestamp": "2025-01-10T14:32:11.123",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Cylinder not found for id: 10",
                                      "path": "/jobs/1/cylinders/10",
                                      "fieldErrors": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{cylinderId}")
    public ResponseEntity<Void> deleteCylinder(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId
    ) {
        cylinderService.deleteCylinder(jobId, cylinderId);
        return ResponseEntity.noContent().build();
    }
}
