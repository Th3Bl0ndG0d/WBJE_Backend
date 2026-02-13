//package nl.avflexologic.wbje.exceptions;
//
//import io.swagger.v3.oas.annotations.Hidden;
//import jakarta.servlet.http.HttpServletRequest;
//import nl.avflexologic.wbje.dtos.error.ApiErrorDTO;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
//    @ExceptionHandler(EntityInUseException.class)
//    public ResponseEntity<ApiErrorDTO> handleEntityInUse(
//            EntityInUseException ex,
//            HttpServletRequest request) {
//        return new ResponseEntity<>(
//                buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()),
//                HttpStatus.CONFLICT
//        );
//    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorDTO> handleUnexpectedException(
//            Exception ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        ApiErrorDTO body = buildError(
//                status,
//                "An unexpected internal error occurred.",
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(body, status);
//    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolation(
//            DataIntegrityViolationException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.CONFLICT;
//
//        ApiErrorDTO body = buildError(
//                status,
//                "Database constraint violation.",
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(body, status);
//    }
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<ApiErrorDTO> handleTypeMismatch(
//            MethodArgumentTypeMismatchException ex,
//            HttpServletRequest request
//    ) {
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//
//        ApiErrorDTO body = buildError(
//                status,
//                "Invalid parameter type for: " + ex.getName(),
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(body, status);
//    }
//
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleJobNotFound(
            JobNotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorDTO body = buildError(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<ApiErrorDTO> handleEntityInUse(
            EntityInUseException ex,
            HttpServletRequest request) {
        return new ResponseEntity<>(
                buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    // 🔹 Verplaatst boven de generieke Exception handler
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorDTO body = buildError(
                status,
                "Database constraint violation.",
                request.getRequestURI()
        );

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorDTO body = buildError(
                status,
                "Invalid parameter type for: " + ex.getName(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(body, status);
    }

    //Altijd als laatste laten staan
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorDTO body = buildError(
                status,
                "An unexpected internal error occurred.",
                request.getRequestURI()
        );

        return new ResponseEntity<>(body, status);
    }

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
