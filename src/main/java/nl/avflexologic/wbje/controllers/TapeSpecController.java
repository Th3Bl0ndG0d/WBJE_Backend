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
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecRequestDTO;
import nl.avflexologic.wbje.dtos.tape.TapeSpecResponseDTO;
import nl.avflexologic.wbje.services.TapeSpecService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tape-specs")
@Tag(name = "Tape Specifications", description = "Endpoints for managing tape specifications.")
public class TapeSpecController {

    private final TapeSpecService tapeSpecService;

    public TapeSpecController(TapeSpecService tapeSpecService) {
        this.tapeSpecService = tapeSpecService;
    }

    // =========================================================================
    // CREATE (ADMIN ONLY)
    // =========================================================================

    @Operation(
            summary = "Create a new tape specification",
            description = "Creates a new tape specification based on the supplied properties.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec successfully created.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecCreatedExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Tesa",
                                              "thickness": 0.45,
                                              "description": "Tesa premium flexo mounting tape."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecValidationErrorExample",
                                    value = """
                                            {
                                              "timestamp": "2025-01-10T14:32:11.123",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "tapeName is required",
                                              "path": "/tape-specs",
                                              "fieldErrors": {
                                                "tapeName": "tapeName is required"
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    @RequestBody(
            description = "Payload for creating a new TapeSpec.",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = TapeSpecRequestDTO.class),
                    examples = @ExampleObject(
                            name = "TapeSpecRequestExample",
                            value = """
                                    {
                                      "tapeName": "3M",
                                      "thickness": 0.50,
                                      "description": "3M high-performance flexo tape."
                                    }
                                    """
                    )
            )
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TapeSpecResponseDTO createTapeSpec(
            @Valid @org.springframework.web.bind.annotation.RequestBody TapeSpecRequestDTO request
    ) {
        return tapeSpecService.createTapeSpec(request);
    }

    // =========================================================================
    // READ ALL (ADMIN + USER)
    // =========================================================================

    @Operation(
            summary = "Get all tape specifications",
            description = "Returns all tape specifications.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of TapeSpecs retrieved.",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TapeSpecResponseDTO.class)),
                    examples = @ExampleObject(
                            name = "TapeSpecListExample",
                            value = """
                                    [
                                      {
                                        "id": 1,
                                        "tapeName": "Tesa",
                                        "thickness": 0.45,
                                        "description": "Tesa premium flexo mounting tape."
                                      },
                                      {
                                        "id": 2,
                                        "tapeName": "3M",
                                        "thickness": 0.50,
                                        "description": "3M high-performance flexo tape."
                                      }
                                    ]
                                    """
                    )
            )
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<TapeSpecResponseDTO> getAllTapeSpecs() {
        return tapeSpecService.getAllTapeSpecs();
    }

    // =========================================================================
    // READ ONE (ADMIN + USER)
    // =========================================================================

    @Operation(
            summary = "Get a tape specification by id",
            description = "Returns a single TapeSpec.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN", "USER"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Tesa",
                                              "thickness": 0.45,
                                              "description": "Tesa premium flexo mounting tape."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecNotFoundExample",
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
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public TapeSpecResponseDTO getTapeSpec(@PathVariable Long id) {
        return tapeSpecService.getTapeSpecById(id);
    }

    // =========================================================================
    // UPDATE (ADMIN ONLY)
    // =========================================================================

    @Operation(
            summary = "Update a tape specification",
            description = "Updates an existing TapeSpec.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN"})
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TapeSpec updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TapeSpecResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "TapeSpecUpdatedExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "tapeName": "Tesa",
                                              "thickness": 0.48,
                                              "description": "Updated Tesa performance flexo tape."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @RequestBody(
            description = "Updated TapeSpec payload.",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = TapeSpecRequestDTO.class),
                    examples = @ExampleObject(
                            name = "TapeSpecUpdateRequestExample",
                            value = """
                                    {
                                      "tapeName": "Tesa",
                                      "thickness": 0.48,
                                      "description": "Updated Tesa performance flexo tape."
                                    }
                                    """
                    )
            )
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TapeSpecResponseDTO updateTapeSpec(
            @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody TapeSpecRequestDTO request
    ) {
        return tapeSpecService.updateTapeSpec(id, request);
    }

    // =========================================================================
    // DELETE (ADMIN ONLY)
    // =========================================================================

    @Operation(
            summary = "Delete a tape specification",
            description = "Deletes the TapeSpec with the given id.",
            security = @SecurityRequirement(name = "bearerAuth", scopes = {"ADMIN"})
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TapeSpec deleted."),
            @ApiResponse(
                    responseCode = "404",
                    description = "TapeSpec not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTapeSpec(@PathVariable Long id) {
        tapeSpecService.deleteTapeSpec(id);
    }
}
