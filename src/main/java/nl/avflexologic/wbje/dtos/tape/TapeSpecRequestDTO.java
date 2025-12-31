package nl.avflexologic.wbje.dtos.tape;

import java.util.Objects;

/**
 * Request payload for creating or updating a TapeSpec.
 * The TapeSpec is treated as reference data for Cylinder configuration.
 */
public record TapeSpecRequestDTO(
        String tapeName,
        String tapeType,
        Integer thickness,
        String info
) {
    public TapeSpecRequestDTO {
        Objects.requireNonNull(tapeName, "tapeName is required.");
    }
}
