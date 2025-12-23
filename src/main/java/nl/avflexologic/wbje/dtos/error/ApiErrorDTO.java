package nl.avflexologic.wbje.dtos.error;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiErrorDTO {
    public LocalDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;
    /**
     * Optional map with field-level validation errors.
     */
    public Map<String, String> fieldErrors;
}
