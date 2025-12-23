package nl.avflexologic.wbje.dtos.error;

import java.time.LocalDateTime;

public class ApiErrorDTO {
    public LocalDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;
}
