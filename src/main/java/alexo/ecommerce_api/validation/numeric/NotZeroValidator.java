package alexo.ecommerce_api.validation.numeric;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NotZeroValidator implements ConstraintValidator<NotZero, Number> {

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