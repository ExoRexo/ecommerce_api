package alexo.ecommerce_api.http.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler
{
    /**
     * @param exception exception
     * @return response to user
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors = new HashMap<>(exception.getFieldErrorCount());

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    /**
     * @param exception exception
     * @return response to user
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleValidation(RuntimeException exception)
    {

        Map<String, String> errors = new HashMap<>(1);

        errors.put("error", exception.getLocalizedMessage());

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}