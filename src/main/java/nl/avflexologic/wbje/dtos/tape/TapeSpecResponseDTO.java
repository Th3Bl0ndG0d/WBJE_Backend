package nl.avflexologic.wbje.dtos.tape;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for returning TapeSpec data over HTTP.
 */
@Schema(description = "Response payload representing a persisted tape specification.")
public record TapeSpecResponseDTO(

        @Schema(
                description = "Database identifier of the tape specification.",
                example = "12"
        )
        Long id,

        @Schema(
                description = "Name of the tape specification.",
                example = "Stickyback A"
        )
        String tapeName,

        @Schema(
                description = "Type or category of the tape.",
                example = "Foam"
        )
        String tapeType,

        @Schema(
                description = "Thickness of the tape in micrometers.",
                example = "380"
        )
        Integer thickness,

        @Schema(
                description = "Optional additional information about the tape specification.",
                example = "High compressibility, suitable for fine detail printing."
        )
        String info

) { }
