package alexo.ecommerce_api.exception.persistance;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super();
    }

    public static UserNotFoundException fromUserId(Long id) {
        return new UserNotFoundException("user with id["+id+"] was not found");
    }

    public UserNotFoundException(Exception cause) {
        super(cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
