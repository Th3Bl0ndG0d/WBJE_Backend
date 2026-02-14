package nl.avflexologic.wbje.dtos.note;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload representing a note attached to a job.")
public record NoteRequestDTO(

        @Schema(
                description = "Textual content of the note.",
                example = "Repeat run, verify plate wear on station 2."
        )
        @Size(max = 100000, message = "Note content must not exceed 100000 characters.")
        String content

) { }
