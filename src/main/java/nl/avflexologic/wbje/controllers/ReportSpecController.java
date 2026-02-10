package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecRequestDTO;
import nl.avflexologic.wbje.dtos.reportSpec.ReportSpecResponseDTO;
import nl.avflexologic.wbje.services.ReportSpecService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes report specification operations for the WBJE API.
 */
@RestController
@RequestMapping("/report-specs")
@Tag(
        name = "ReportSpecs",
        description = "Endpoints for managing report specifications. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER)."
)
public class ReportSpecController {

    private final ReportSpecService reportSpecService;

    public ReportSpecController(ReportSpecService reportSpecService) {
        this.reportSpecService = reportSpecService;
    }

    // ========================================================================
    // CREATE — ADMIN ONLY → service
    // ========================================================================

    @Operation(
            summary = "Create a new report specification",
            description = "Creates a new report specification. Accessible role: service (ROLE_ADMIN).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "ReportSpec successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Created ReportSpec",
                                    value = """
                                            {
                                              "id": 5,
                                              "reportName": "Plate A",
                                              "reportType": "Photopolymer",
                                              "thickness": 67,
                                              "info": "Default plate spec"
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
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Domain validation failed (invalid ReportSpec structure or rules)",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))

    })
    @PostMapping
    public ReportSpecResponseDTO createReportSpec(
            @Valid
            @RequestBody(
                    description = "ReportSpec payload used to create a new specification.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecRequestDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody ReportSpecRequestDTO request
    ) {
        return reportSpecService.createReportSpec(request);
    }

    // ========================================================================
    // GET BY ID — ADMIN + USER → service + operator
    // ========================================================================

    @Operation(
            summary = "Get a report specification by id",
            description = "Returns a single report specification. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ReportSpec successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "ReportSpec with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Domain validation failed (invalid fields or incompatible specifications)",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))

    })
    @GetMapping("/{id}")
    public ReportSpecResponseDTO getReportSpecById(@PathVariable Long id) {
        return reportSpecService.getReportSpecById(id);
    }

    // ========================================================================
    // GET ALL — ADMIN + USER → service + operator
    // ========================================================================

    @Operation(
            summary = "List all report specifications",
            description = "Retrieves all report specifications. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponse(
            responseCode = "200",
            description = "ReportSpecs successfully retrieved.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReportSpecResponseDTO.class)
            )
    )
    @GetMapping
    public List<ReportSpecResponseDTO> getAllReportSpecs() {
        return reportSpecService.getAllReportSpecs();
    }

    // ========================================================================
    // UPDATE — ADMIN ONLY → service
    // ========================================================================

    @Operation(
            summary = "Update a report specification",
            description = "Updates an existing report specification. Accessible role: service (ROLE_ADMIN).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "ReportSpec successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ReportSpec not found."
            )
    })
    @PutMapping("/{id}")
    public ReportSpecResponseDTO updateReportSpec(
            @PathVariable Long id,
            @Valid
            @RequestBody(
                    description = "Payload to update an existing report specification.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecRequestDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody ReportSpecRequestDTO request
    ) {
        return reportSpecService.updateReportSpec(id, request);
    }

    // ========================================================================
    // DELETE — ADMIN ONLY → service
    // ========================================================================

    @Operation(
            summary = "Delete a report specification",
            description = "Deletes a report specification. Accessible role: service (ROLE_ADMIN).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service"})
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "ReportSpec successfully deleted."),
            @ApiResponse(responseCode = "404", description = "ReportSpec not found."),
            @ApiResponse(responseCode = "409", description = "ReportSpec is in use by one or more Reports (EntityInUseException)",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))

    })
    @DeleteMapping("/{id}")
    public void deleteReportSpec(@PathVariable Long id) {
        reportSpecService.deleteReportSpec(id);
    }
}
