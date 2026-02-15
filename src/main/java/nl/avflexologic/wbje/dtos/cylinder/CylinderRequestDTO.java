package nl.avflexologic.wbje.dtos.cylinder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Schema(description = "Request payload for creating or updating a cylinder within a job.")
public record CylinderRequestDTO(

        @Schema(
                description = "Cylinder sequence number within the job. Must be unique per job.",
                example = "1",
                minimum = "1"
        )
        @Min(1)
        Integer cylinderNr,

        @Schema(
                description = "Cylinder color label.",
                example = "Cyan"
        )
        String color,

        @Schema(
                description = "Optional cylinder-specific information.",
                example = "Mounting side: left"
        )
        String cylinderInfo,

        @NotNull
        @Schema(
                description = "Identifier of the TapeSpec used by this cylinder.",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long tapeSpecId

) {

    /**
     * Enforces mandatory request fields.
     */
    public CylinderRequestDTO {

    }
}
