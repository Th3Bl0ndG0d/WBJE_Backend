package nl.avflexologic.wbje.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized HTTP exception handling for the WBJE REST API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Builds a standard API error DTO for the given HTTP status and message.
     */
    private ApiErrorDTO buildError(HttpStatus status, String message, String path) {
        ApiErrorDTO dto = new ApiErrorDTO();
        dto.timestamp = LocalDateTime.now();
        dto.status = status.value();
        dto.error = status.getReasonPhrase();
        dto.message = message;
        dto.path = path;
        return dto;
    }

    /**
     * Handles Bean Validation errors from @Valid annotated request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ApiErrorDTO body = buildError(status, "Validation failed for request.", request.getRequestURI());
        body.fieldErrors = fieldErrors;

        return new ResponseEntity<>(body, status);
    }

    /**
     * Handles invalid arguments raised in the service layer as HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    /**
     * Handles missing resources as HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
