package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(
        name = "Reports",
        description = "Endpoints for managing reports (plates) in the Web Based Job Editor. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER)."
)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Operation(
            summary = "Create a new report",
            description = "Creates a new report (plate) within a cylinder based on the supplied request payload. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
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
            ),
            @ApiResponse(responseCode = "422", description = "Domain validation failed (invalid ReportSpec or TapeSpec relation)",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponseDTO createReport(@Valid @RequestBody ReportRequestDTO request) {
        return reportService.createReport(request);
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Operation(
            summary = "Get a report by id",
            description = "Returns a single report (plate) for the given identifier. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
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

    // ---------------------------------------------------------
    // LIST BY CYLINDER
    // ---------------------------------------------------------

    @Operation(
            summary = "List reports by cylinder id",
            description = "Returns all reports (plates) that belong to the given cylinder. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
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

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Operation(
            summary = "Update a report",
            description = "Updates an existing report (plate) for the given identifier. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
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
            ),@ApiResponse(responseCode = "422", description = "Domain validation failed (e.g. incompatible ReportSpec -> TapeSpec relation)",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))

    })
    @PutMapping("/{id}")
    public ReportResponseDTO updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequestDTO request
    ) {
        return reportService.updateReport(id, request);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Operation(
            summary = "Delete a report",
            description = "Deletes a report (plate) for the given identifier. Accessible roles: service (ROLE_ADMIN), operator (ROLE_USER).",
            security = @SecurityRequirement(name = "keycloak", scopes = {"service", "operator"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Report successfully deleted."
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
