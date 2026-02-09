//package nl.avflexologic.wbje.exceptions;
//
//import io.swagger.v3.oas.annotations.Hidden;
//import jakarta.servlet.http.HttpServletRequest;
//import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Centralized exception handling for all REST controllers.
// * This component translates technical exceptions into a stable API error contract.
// */
//@RestControllerAdvice
//@Hidden
//public class GlobalExceptionHandler {
//
//    /**
//     * Handles Bean Validation failures as HTTP 400 with field-level error details.
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorDTO> handleValidationException(
//            MethodArgumentNotValidException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        Map<String, String> fieldErrors = new HashMap<>();
//
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
//            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//
//        ApiErrorDTO body = buildError(
//                status,
//                "Validation failed for one or more fields.",
//                request.getRequestURI()
//        );
//        body.fieldErrors = fieldErrors;
//
//        return new ResponseEntity<>(body, status);
//    }
//
//    /**
//     * Maps IllegalArgumentException to HTTP 400.
//     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ApiErrorDTO> handleIllegalArgument(
//            IllegalArgumentException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
//        return new ResponseEntity<>(body, status);
//    }
//
//    /**
//     * Handles generic resource-not-found cases as HTTP 404.
//     */
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiErrorDTO> handleResourceNotFound(
//            ResourceNotFoundException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.NOT_FOUND;
//        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
//        return new ResponseEntity<>(body, status);
//    }
//
//    /**
//     * Handles job-specific not-found cases as HTTP 404.
//     */
//    @ExceptionHandler(JobNotFoundException.class)
//    public ResponseEntity<ApiErrorDTO> handleJobNotFound(
//            JobNotFoundException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.NOT_FOUND;
//        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
//        return new ResponseEntity<>(body, status);
//    }
//
//    /**
//     * Creates a basic ApiErrorDTO instance for the given HTTP status and message.
//     */
//    private ApiErrorDTO buildError(HttpStatus status, String message, String path) {
//        ApiErrorDTO dto = new ApiErrorDTO();
//        dto.timestamp = LocalDateTime.now();
//        dto.status = status.value();
//        dto.error = status.getReasonPhrase();
//        dto.message = message;
//        dto.path = path;
//        return dto;
//    }
//}
package nl.avflexologic.wbje.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
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
 * Centralized exception handling for all REST controllers.
 * This component translates technical exceptions into a stable API error contract.
 */
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    /**
     * Handles Bean Validation failures as HTTP 400 with field-level error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiErrorDTO body = buildError(
                status,
                "Validation failed for one or more fields.",
                request.getRequestURI()
        );
        body.fieldErrors = fieldErrors;

        return new ResponseEntity<>(body, status);
    }

    /**
     * Maps IllegalArgumentException to HTTP 400.
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
     * Handles generic resource-not-found cases as HTTP 404.
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

    /**
     * Handles job-specific not-found cases as HTTP 404.
     */
    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleJobNotFound(
            JobNotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    /**
     * Creates a basic ApiErrorDTO instance for the given HTTP status and message.
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
}
