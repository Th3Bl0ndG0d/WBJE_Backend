package nl.avflexologic.wbje.dtos.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error payload returned by the WBJE API.
 */
@Schema(description = "Standard error payload returned by the WBJE API.")
public class ApiErrorDTO {

    @Schema(
            description = "Timestamp at which the error was created.",
            example = "2025-01-10T14:32:11.123"
    )
    public LocalDateTime timestamp;

    @Schema(
            description = "HTTP status code that was returned.",
            example = "404"
    )
    public int status;

    @Schema(
            description = "Short reason phrase that corresponds to the HTTP status.",
            example = "Not Found"
    )
    public String error;

    @Schema(
            description = "Human-readable description of the error.",
            example = "Job not found for id: 42"
    )
    public String message;

    @Schema(
            description = "Request path where the error occurred.",
            example = "/jobs/42"
    )
    public String path;

    /**
     * Optional map that contains the field-level validation errors, keyed by field name.
     */
    @Schema(
            description = "Optional map containing field-level validation errors keyed by field name.",
            example = "{ \"jobNumber\": \"must not be blank\" }"
    )
    public Map<String, String> fieldErrors;
}
