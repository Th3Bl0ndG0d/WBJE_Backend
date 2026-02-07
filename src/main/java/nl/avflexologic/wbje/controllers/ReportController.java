package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.services.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes report (plate) operations for the WBJE API.
 *
 * A report represents a single plate mounted on a cylinder. The actual plate
 * material (e.g. Toyobo, Asahi, Flint, Kodak) is modeled in the linked
 * ReportSpec entity, referenced by reportSpecId.
 */
@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Endpoints for managing reports (plates) in the Web Based Job Editor.")
public class ReportController {

    private final ReportService reportService;

    /**
     * Constructs a new instance of the ReportController with the required ReportService dependency.
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Creates a new report (plate) within a cylinder based on the supplied request payload.
     *
     * The ReportRequestDTO specifies plate geometry and cylinder linkage. The actual plate
     * material brand (Toyobo, Asahi, Flint, Kodak) is determined via the referenced ReportSpec.
     */
    @Operation(
            summary = "Create a new report",
            description = "Creates a new report (plate) within a cylinder based on the supplied request payload."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportCreatedExample",
                                    value = """
                                            {
                                              "id": 100,
                                              "reportNr": 1,
                                              "reportWidth": 400,
                                              "xOffset": 10,
                                              "yOffset": 20,
                                              "cylinderId": 12,
                                              "reportSpecId": 5
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
                                    name = "ReportValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/reports",
                                              "fieldErrors": {
                                                "reportNr": "reportNr is required.",
                                                "cylinderId": "cylinderId is required.",
                                                "reportSpecId": "reportSpecId is required."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cylinder or ReportSpec was not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportCreateNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Cylinder not found for id: 12",
                                              "path": "/reports",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ReportResponseDTO createReport(@Valid @RequestBody ReportRequestDTO request) {
        return reportService.createReport(request);
    }

    /**
     * Returns a single report for the given identifier.
     *
     * This endpoint exposes the geometric and linkage information for a plate. The linked
     * ReportSpec referenced by reportSpecId defines the actual plate material (Toyobo, Asahi, Flint, Kodak).
     */
    @Operation(
            summary = "Get a report by id",
            description = "Returns a single report (plate) for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportExample",
                                    value = """
                                            {
                                              "id": 100,
                                              "reportNr": 1,
                                              "reportWidth": 400,
                                              "xOffset": 10,
                                              "yOffset": 20,
                                              "cylinderId": 12,
                                              "reportSpecId": 5
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Report not found for id: 100",
                                              "path": "/reports/100",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ReportResponseDTO getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    /**
     * Returns all reports belonging to a given cylinder.
     *
     * The cylinderId references the parent cylinder. Each report may reference a different
     * ReportSpec, corresponding bijvoorbeeld met verschillende plaatmaterialen
     * (Toyobo, Asahi, Flint, Kodak).
     */
    @Operation(
            summary = "List reports by cylinder id",
            description = "Returns all reports (plates) that belong to the given cylinder."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reports successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReportResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "ReportListExample",
                                    value = """
                                            [
                                              {
                                                "id": 100,
                                                "reportNr": 1,
                                                "reportWidth": 400,
                                                "xOffset": 10,
                                                "yOffset": 20,
                                                "cylinderId": 12,
                                                "reportSpecId": 5
                                              },
                                              {
                                                "id": 101,
                                                "reportNr": 2,
                                                "reportWidth": 380,
                                                "xOffset": 420,
                                                "yOffset": 20,
                                                "cylinderId": 12,
                                                "reportSpecId": 6
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public List<ReportResponseDTO> getReportsByCylinderId(@RequestParam Long cylinderId) {
        return reportService.getReportsByCylinderId(cylinderId);
    }

    /**
     * Updates an existing report for the given identifier.
     *
     * The update operation replaces the editable fields of the existing report with the values
     * supplied in the request payload, subject to validation constraints.
     */
    @Operation(
            summary = "Update a report",
            description = "Updates an existing report (plate) for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportUpdatedExample",
                                    value = """
                                            {
                                              "id": 100,
                                              "reportNr": 1,
                                              "reportWidth": 420,
                                              "xOffset": 15,
                                              "yOffset": 25,
                                              "cylinderId": 12,
                                              "reportSpecId": 5
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
                                    name = "ReportUpdateValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/reports/100",
                                              "fieldErrors": {
                                                "reportNr": "reportNr is required."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report or ReportSpec was not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportUpdateNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Report not found for id: 999",
                                              "path": "/reports/999",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ReportResponseDTO updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequestDTO request
    ) {
        return reportService.updateReport(id, request);
    }

    /**
     * Deletes a report for the given identifier.
     *
     * Deletion is executed by identifier. If the report does not exist, a not-found
     * response is returned by the centralized exception handler.
     */
    @Operation(
            summary = "Delete a report",
            description = "Deletes a report (plate) for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report successfully deleted.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ReportDeleteExample",
                                    value = """
                                            // No response body is returned for a successful delete (HTTP 200).
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report with the given id was not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "ReportDeleteNotFoundExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Report not found for id: 999",
                                              "path": "/reports/999",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
