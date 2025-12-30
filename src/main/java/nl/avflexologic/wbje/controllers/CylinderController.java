package nl.avflexologic.wbje.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.services.CylinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cylinders",
        description = "Operations related to cylinders within a job"
)
@RestController
@RequestMapping("/jobs/{jobId}/cylinders")
public class CylinderController {

    private final CylinderService cylinderService;

    public CylinderController(CylinderService cylinderService) {
        this.cylinderService = cylinderService;
    }

    @Operation(
            summary = "Create a cylinder for a job",
            description = "Creates a new cylinder within the given job. " +
                    "Cylinder numbers must be unique per job."
    )
    @PostMapping
    public ResponseEntity<CylinderResponseDTO> createCylinder(
            @PathVariable Long jobId,
            @Valid @RequestBody CylinderRequestDTO request
    ) {
        CylinderResponseDTO response =
                cylinderService.createCylinder(jobId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all cylinders for a job",
            description = "Returns all cylinders that belong to the specified job."
    )
    @GetMapping
    public ResponseEntity<List<CylinderResponseDTO>> getCylindersByJob(
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(
                cylinderService.getCylindersByJob(jobId)
        );
    }

    @Operation(
            summary = "Get a single cylinder",
            description = "Returns a specific cylinder by id, scoped to the given job."
    )
    @GetMapping("/{cylinderId}")
    public ResponseEntity<CylinderResponseDTO> getCylinderById(
            @PathVariable Long jobId,
            @PathVariable Long cylinderId
    ) {
        return ResponseEntity.ok(
                cylinderService.getCylinderById(jobId, cylinderId)
        );
    }
}
