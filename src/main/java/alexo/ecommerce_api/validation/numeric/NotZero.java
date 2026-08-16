package alexo.ecommerce_api.validation.numeric;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * passed number must be null, greater or less than zero
 */
@Documented
@Constraint(validatedBy = NotZero.NotZeroValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotZero {

    String message() default "must not be zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NotZeroValidator implements ConstraintValidator<NotZero, Number> {

        @Override
        public boolean isValid(Number value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            return switch (value) {
                case BigDecimal v -> v.signum() != 0;
                case BigInteger v -> v.signum() != 0;

                case Byte v -> v != 0;
                case Short v -> v != 0;
                case Integer v -> v != 0;
                case Long v -> v != 0L;

                case Float v -> v != 0.0f;
                case Double v -> v != 0.0d;

                default -> throw new IllegalArgumentException(
                        "Unsupported Number type: " + value.getClass().getName()
                );
            };
        }
    }
}