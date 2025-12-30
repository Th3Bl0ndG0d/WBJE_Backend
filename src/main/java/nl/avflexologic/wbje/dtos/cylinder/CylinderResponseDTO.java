package nl.avflexologic.wbje.dtos.cylinder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response model representing a cylinder within a job.")
public record CylinderResponseDTO(

        @Schema(description = "Database identifier of the cylinder.", example = "1")
        Long id,

        @Schema(description = "Cylinder sequence number within the job.", example = "1")
        Integer cylinderNr,

        @Schema(description = "Cylinder color label.", example = "Magenta")
        String color,

        @Schema(description = "Cylinder-specific info field.", example = "Mounting side: left")
        String cylinderInfo,

        @Schema(description = "Identifier of the parent job.", example = "100")
        Long jobId,

        @Schema(description = "Identifier of the TapeSpec used by this cylinder.", example = "10")
        Long tapeSpecId

) { }
