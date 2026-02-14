package nl.avflexologic.wbje.dtos.note;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response model representing a note attached to a job.")
public record NoteResponseDTO(

        @Schema(description = "Database identifier of the note.", example = "5")
        Long id,

        @Schema(
                description = "Textual content of the note.",
                example = "Repeat run, verify plate wear on station 2."
        )
        String content

) { }
