package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "ReportSpecs", description = "Endpoints for managing report specifications.")
public class ReportSpecController {

    private final ReportSpecService reportSpecService;

    /**
     * Constructs a new instance of the ReportSpecController with the required ReportSpecService dependency.
     */
    public ReportSpecController(ReportSpecService reportSpecService) {
        this.reportSpecService = reportSpecService;
    }

    @Operation(summary = "Create a new report specification")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
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
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "Validation error",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/report-specs",
                                              "fieldErrors": {
                                                "reportName": "reportName is required."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ReportSpecResponseDTO createReportSpec(
            @Valid
            @RequestBody(
                    description = "ReportSpec payload used to create a new specification.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Create ReportSpec request",
                                    value = """
                                            {
                                              "reportName": "Plate A",
                                              "reportType": "Photopolymer",
                                              "thickness": 67,
                                              "info": "Default plate spec"
                                            }
                                            """
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody ReportSpecRequestDTO request
    ) {
        return reportSpecService.createReportSpec(request);
    }

    @Operation(summary = "Get a report specification by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ReportSpec successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportSpec response",
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
                    responseCode = "404",
                    description = "ReportSpec with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "Not found",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "ReportSpec not found for id: 42",
                                              "path": "/report-specs/42",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ReportSpecResponseDTO getReportSpecById(@PathVariable Long id) {
        return reportSpecService.getReportSpecById(id);
    }

    @Operation(summary = "List all report specifications")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ReportSpecs successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportSpec list response",
                                    value = """
                                            [
                                              {
                                                "id": 5,
                                                "reportName": "Plate A",
                                                "reportType": "Photopolymer",
                                                "thickness": 67,
                                                "info": "Default plate spec"
                                              },
                                              {
                                                "id": 6,
                                                "reportName": "Plate B",
                                                "reportType": "Rubber",
                                                "thickness": 80,
                                                "info": "Secondary spec"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public List<ReportSpecResponseDTO> getAllReportSpecs() {
        return reportSpecService.getAllReportSpecs();
    }

    @Operation(summary = "Update a report specification")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ReportSpec successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Updated ReportSpec",
                                    value = """
                                            {
                                              "id": 5,
                                              "reportName": "Plate A",
                                              "reportType": "Photopolymer",
                                              "thickness": 70,
                                              "info": "Adjusted thickness"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for the request body.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ReportSpec with the given id was not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ReportSpecResponseDTO updateReportSpec(
            @PathVariable Long id,
            @Valid
            @RequestBody(
                    description = "ReportSpec payload used to update an existing specification.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportSpecRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Update ReportSpec request",
                                    value = """
                                            {
                                              "reportName": "Plate A",
                                              "reportType": "Photopolymer",
                                              "thickness": 70,
                                              "info": "Adjusted thickness"
                                            }
                                            """
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody ReportSpecRequestDTO request
    ) {
        return reportSpecService.updateReportSpec(id, request);
    }

    @Operation(summary = "Delete a report specification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ReportSpec successfully deleted."),
            @ApiResponse(
                    responseCode = "404",
                    description = "ReportSpec with the given id was not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public void deleteReportSpec(@PathVariable Long id) {
        reportSpecService.deleteReportSpec(id);
    }
}
