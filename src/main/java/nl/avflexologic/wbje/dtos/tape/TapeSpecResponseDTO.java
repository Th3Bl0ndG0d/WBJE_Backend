package nl.avflexologic.wbje.dtos.tape;

/**
 * Response DTO for returning TapeSpec data over HTTP.
 */
public record TapeSpecResponseDTO(
        Long id,
        String tapeName,
        String tapeType,
        Integer thickness,
        String info
) { }
