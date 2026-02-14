package nl.avflexologic.wbje.dtos.tape;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.util.Objects;

/**
 * Request payload for creating or updating a TapeSpec.
 * The TapeSpec is treated as reference data for Cylinder configuration.
 */
@Schema(description = "Request payload for creating or updating a tape specification.")
public record TapeSpecRequestDTO(

        @Schema(
                description = "Name of the tape specification.",
                example = "Stickyback A",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "tapeName is required.")
        @Size(max = 255, message = "tapeName must be at most 255 characters.")
        String tapeName,

        @Schema(
                description = "Type or category of the tape.",
                example = "Foam"
        )
        @Size(max = 255, message = "tapeType must be at most 255 characters.")
        String tapeType,

        @Schema(
                description = "Thickness of the tape in micrometers.",
                example = "380",
                minimum = "0"
        )
        @Min(0)
        Integer thickness,

        @Schema(
                description = "Optional additional information about the tape specification.",
                example = "High compressibility, suitable for fine detail printing."
        )
        @Size(max = 255, message = "info must be at most 255 characters.")
        String info

) {
    public TapeSpecRequestDTO {
//        Objects.requireNonNull(tapeName, "tapeName is required.");
    }
}
