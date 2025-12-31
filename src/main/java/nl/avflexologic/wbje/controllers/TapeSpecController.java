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
import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.services.TapeSpecService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes TapeSpec operations for the WBJE API.
 */
@RestController
@RequestMapping("/tape-specs")
@Tag(name = "TapeSpecs", description = "Endpoints for managing tape specifications.")
public class TapeSpecController {

    private final TapeSpecService tapeSpecService;

    /**
     * Constructs a new instance of the TapeSpecController with the required service dependency.
     */
    public TapeSpecController(TapeSpecService tapeSpecService) {
        this.tapeSpecService = tapeSpecService;
    }

    /**
     * Creates a new TapeSpec based on the supplied request payload.
     */
    @Operation(
            summary = "Create a new tape specification",
            description = "Creates a new tape specification based on the supplied request payload."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecResponseExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Standard Tape",
                                              "tapeType": "PVC",
                                              "thickness": 120,
                                              "info": "Default tape spec"
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
                                              "path": "/tape-specs",
                                              "fieldErrors": {
                                                "tapeName": "tapeName is required."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public TapeSpecResponseDTO createTapeSpec(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "TapeSpec request payload.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecRequestExample",
                                    value = """
                                            {
                                              "tapeName": "Standard Tape",
                                              "tapeType": "3M",
                                              "thickness": 500,
                                              "info": "Default tape spec"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody TapeSpecRequestDTO request
    ) {
        return tapeSpecService.createTapeSpec(request);
    }

    /**
     * Returns a single TapeSpec for the given identifier.
     */
    @Operation(
            summary = "Get a tape specification by id",
            description = "Returns a single tape specification for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecResponseExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Standard Tape",
                                              "tapeType": "3M",
                                              "thickness": 500,
                                              "info": "Default tape spec"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec with the given id was not found.",
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
                                              "message": "TapeSpec not found for id: 99",
                                              "path": "/tape-specs/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public TapeSpecResponseDTO getTapeSpecById(@PathVariable Long id) {
        return tapeSpecService.getTapeSpecById(id);
    }

    /**
     * Returns all TapeSpecs.
     */
    @Operation(
            summary = "Get all tape specifications",
            description = "Returns all tape specifications."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpecs successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecListExample",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "tapeName": "Standard Tape",
                                                "tapeType": "3M",
                                                "thickness": 500,
                                                "info": "Default tape spec"
                                              },
                                              {
                                                "id": 2,
                                                "tapeName": "High Tack Tape",
                                                "tapeType": "TESSA",
                                                "thickness": 500,
                                                "info": "For high adhesion use cases"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public List<TapeSpecResponseDTO> getAllTapeSpecs() {
        return tapeSpecService.getAllTapeSpecs();
    }

    /**
     * Updates an existing TapeSpec.
     */
    @Operation(
            summary = "Update a tape specification",
            description = "Updates an existing tape specification for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecUpdatedExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Standard Tape",
                                              "tapeType": "3M",
                                              "thickness": 500,
                                              "info": "Updated thickness"
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
                                              "path": "/tape-specs/1",
                                              "fieldErrors": {
                                                "thickness": "thickness must be a positive number."
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec with the given id was not found.",
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
                                              "message": "TapeSpec not found for id: 99",
                                              "path": "/tape-specs/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public TapeSpecResponseDTO updateTapeSpec(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "TapeSpec request payload.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecUpdateRequestExample",
                                    value = """
                                            {
                                              "tapeName": "Standard Tape",
                                              "tapeType": "3M",
                                              "thickness": 500,
                                              "info": "Updated thickness"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody TapeSpecRequestDTO request
    ) {
        return tapeSpecService.updateTapeSpec(id, request);
    }

    /**
     * Deletes a TapeSpec for the given identifier.
     */
    @Operation(
            summary = "Delete a tape specification",
            description = "Deletes a tape specification for the given identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec successfully deleted."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec with the given id was not found.",
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
                                              "message": "TapeSpec not found for id: 99",
                                              "path": "/tape-specs/99",
                                              "fieldErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public void deleteTapeSpec(@PathVariable Long id) {
        tapeSpecService.deleteTapeSpec(id);
    }
}
