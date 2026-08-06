package alexo.ecommerce_api.http.controller.advice;

import alexo.ecommerce_api.http.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Maps application exceptions to unified HTTP error responses.
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    /**
     * Handles bean validation errors from request body DTOs.
     *
     * @param exception validation exception
     * @return HTTP 400 with unified error envelope
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NotNull ApiResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return build(HttpStatus.BAD_REQUEST, errors);
    }

    /**
     * Handles constraint violations from parameter-level validation.
     *
     * @param exception validation exception
     * @return HTTP 400 with unified error envelope
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<@NotNull ApiResponse> handleConstraintViolation(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        return build(HttpStatus.BAD_REQUEST, errors);
    }

    /**
     * Handles malformed JSON payloads.
     *
     * @param exception parse exception
     * @return HTTP 400 with unified error envelope
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<@NotNull ApiResponse> handleInvalidBody(HttpMessageNotReadableException exception) {
        return build(HttpStatus.BAD_REQUEST, List.of(resolveMessage(exception, "Request body is invalid")));
    }

    /**
     * Handles business argument errors.
     *
     * @param exception argument exception
     * @return HTTP 400 with unified error envelope
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NotNull ApiResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return build(HttpStatus.BAD_REQUEST, List.of(resolveMessage(exception, "Request is invalid")));
    }

    /**
     * Handles not-found entity cases.
     *
     * @param exception not-found exception
     * @return HTTP 404 with unified error envelope
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<@NotNull ApiResponse> handleNotFound(EntityNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, List.of(resolveMessage(exception, "Resource not found")));
    }

    /**
     * Handles authentication failures raised in MVC layer.
     *
     * @param exception authentication exception
     * @return HTTP 401 with unified error envelope
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<@NotNull ApiResponse> handleAuthentication(AuthenticationException exception) {
        return build(HttpStatus.UNAUTHORIZED, List.of(resolveMessage(exception, "Authentication failed")));
    }

    /**
     * Handles access denial raised in MVC layer.
     *
     * @param exception access denied exception
     * @return HTTP 403 with unified error envelope
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<@NotNull ApiResponse> handleAccessDenied(AccessDeniedException exception) {
        return build(HttpStatus.FORBIDDEN, List.of(resolveMessage(exception, "Access is denied")));
    }

    /**
     * Fallback handler for any unprocessed exception.
     *
     * @param exception unexpected exception
     * @return HTTP 500 with unified error envelope
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NotNull ApiResponse> handleUnexpected(Exception exception) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, List.of(resolveMessage(exception, "Unexpected server error")));
    }

    /**
     * Creates response entity with unified error envelope.
     *
     * @param status HTTP status to return
     * @param errors error message list
     * @return response entity with unified payload
     */
    private ResponseEntity<@NotNull ApiResponse> build(HttpStatus status, List<String> errors) {
        return ResponseEntity.status(status).body(ApiResponse.failure(errors));
    }

    /**
     * Resolves final error message with fallback for blank exception text.
     *
     * @param exception source exception
     * @param fallback fallback message
     * @return resolved message for response
     */
    private String resolveMessage(Exception exception, String fallback) {
        if (exception.getMessage() == null || exception.getMessage().isBlank()) {
            return fallback;
        }
        return exception.getMessage();
    }
}