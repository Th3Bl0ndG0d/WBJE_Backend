//package nl.avflexologic.wbje.controllers;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.ExampleObject;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
//import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
//import nl.avflexologic.wbje.services.ReportService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * REST controller that exposes report (plate) operations for the WBJE API.
// */
//@RestController
//@RequestMapping("/reports")
//@Tag(name = "Reports", description = "Endpoints for managing reports (plates) in the Web Based Job Editor.")
//public class ReportController {
//
//    private final ReportService reportService;
//
//    /**
//     * Constructs a new instance of the ReportController with the required ReportService dependency.
//     */
//    public ReportController(ReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    /**
//     * Creates a new report (plate) within a cylinder based on the supplied request payload.
//     */
//    @Operation(
//            summary = "Create a new report",
//            description = "Creates a new report (plate) within a cylinder based on the supplied request payload."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Report successfully created.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ReportResponseDTO.class),
//                            examples = @ExampleObject(
//                                    value = """
//                                            {
//                                              "id": 100,
//                                              "reportNr": 1,
//                                              "reportWidth": 400,
//                                              "xOffset": 10,
//                                              "yOffset": 20,
//                                              "cylinderId": 12,
//                                              "reportSpecId": 5
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
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 400,
//                                              "error": "Bad Request",
//                                              "message": "Validation failed for one or more fields.",
//                                              "path": "/reports",
//                                              "fieldErrors": {
//                                                "reportNr": "reportNr is required."
//                                              }
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Cylinder or ReportSpec was not found for the given id.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Cylinder not found for id: 12",
//                                              "path": "/reports",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @PostMapping
//    public ReportResponseDTO createReport(@Valid @RequestBody ReportRequestDTO request) {
//        return reportService.createReport(request);
//    }
//
//    /**
//     * Returns a single report for the given identifier.
//     */
//    @Operation(
//            summary = "Get a report by id",
//            description = "Returns a single report (plate) for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Report successfully retrieved.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ReportResponseDTO.class),
//                            examples = @ExampleObject(
//                                    value = """
//                                            {
//                                              "id": 100,
//                                              "reportNr": 1,
//                                              "reportWidth": 400,
//                                              "xOffset": 10,
//                                              "yOffset": 20,
//                                              "cylinderId": 12,
//                                              "reportSpecId": 5
//                                            }
//                                            """
//                            )
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Report with the given id was not found.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class),
//                            examples = @ExampleObject(
//                                    value = """
//                                            {
//                                              "timestamp": "2025-01-10T14:32:11.123",
//                                              "status": 404,
//                                              "error": "Not Found",
//                                              "message": "Report not found for id: 100",
//                                              "path": "/reports/100",
//                                              "fieldErrors": null
//                                            }
//                                            """
//                            )
//                    )
//            )
//    })
//    @GetMapping("/{id}")
//    public ReportResponseDTO getReportById(@PathVariable Long id) {
//        return reportService.getReportById(id);
//    }
//
//    /**
//     * Returns all reports belonging to a given cylinder.
//     */
//    @Operation(
//            summary = "List reports by cylinder id",
//            description = "Returns all reports (plates) that belong to the given cylinder."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Reports successfully retrieved.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ReportResponseDTO.class),
//                            examples = @ExampleObject(
//                                    value = """
//                                            [
//                                              {
//                                                "id": 100,
//                                                "reportNr": 1,
//                                                "reportWidth": 400,
//                                                "xOffset": 10,
//                                                "yOffset": 20,
//                                                "cylinderId": 12,
//                                                "reportSpecId": 5
//                                              }
//                                            ]
//                                            """
//                            )
//                    )
//            )
//    })
//    @GetMapping
//    public List<ReportResponseDTO> getReportsByCylinderId(@RequestParam Long cylinderId) {
//        return reportService.getReportsByCylinderId(cylinderId);
//    }
//
//    /**
//     * Updates an existing report for the given identifier.
//     */
//    @Operation(
//            summary = "Update a report",
//            description = "Updates an existing report (plate) for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Report successfully updated.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ReportResponseDTO.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Validation failed for the request body.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Report or ReportSpec was not found for the given id.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class)
//                    )
//            )
//    })
//    @PutMapping("/{id}")
//    public ReportResponseDTO updateReport(@PathVariable Long id, @Valid @RequestBody ReportRequestDTO request) {
//        return reportService.updateReport(id, request);
//    }
//
//    /**
//     * Deletes a report for the given identifier.
//     */
//    @Operation(
//            summary = "Delete a report",
//            description = "Deletes a report (plate) for the given identifier."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Report successfully deleted."
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Report with the given id was not found.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = ApiErrorDTO.class)
//                    )
//            )
//    })
//    @DeleteMapping("/{id}")
//    public void deleteReport(@PathVariable Long id) {
//        reportService.deleteReport(id);
//    }
//}
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
import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.services.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes report (plate) operations for the WBJE API.
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
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Validation failed for one or more fields.",
                                              "path": "/reports",
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
                    description = "Cylinder or ReportSpec was not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
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
                            schema = @Schema(implementation = ReportResponseDTO.class),
                            examples = @ExampleObject(
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
                            schema = @Schema(implementation = ReportResponseDTO.class)
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
            @ApiResponse(
                    responseCode = "404",
                    description = "Report or ReportSpec was not found for the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ReportResponseDTO updateReport(@PathVariable Long id, @Valid @RequestBody ReportRequestDTO request) {
        return reportService.updateReport(id, request);
    }

    /**
     * Deletes a report for the given identifier.
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
                                    value = """
                                            // No content is returned for a successful delete (HTTP 200).
                                            // The response body is intentionally empty.
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
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Report not found for id: 99",
                                              "path": "/reports/99",
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
