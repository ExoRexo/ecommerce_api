package alexo.ecommerce_api.exception.authentication;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationException extends AuthenticationException {
    public TokenAuthenticationException(@Nullable String msg) {
        super(msg);
    }

    public TokenAuthenticationException(@Nullable String msg, Throwable cause) {
        super(msg, cause);
    }
}
