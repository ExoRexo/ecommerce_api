package alexo.ecommerce_api.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

/**
 * Standard API envelope used by all JSON endpoints.
 *
 * @param payload successful response payload, can be primitive, object, or list
 * @param errors list of error messages, empty for success responses
 * @param date response creation timestamp in UTC
 */
public record ApiResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL) Object payload,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) Object errors,
        @JsonInclude(JsonInclude.Include.ALWAYS) Instant date
) {

    public ApiResponse {
        errors = errors == null ? List.of() : errors;
        date = date == null ? Instant.now() : date;
    }

    /**
     * Creates successful API envelope.
     *
     * @param payload successful response payload
     * @return envelope with empty error list
     */
    public static ApiResponse success(Object payload) {
        return new ApiResponse(payload, List.of(), null);
    }

    public static ApiResponse failure(Object errors) {
        return new ApiResponse(null, errors, null);
    }

    /**
     * Exposes short class name for envelope typing.
     *
     * @return short runtime type name
     */
    @JsonProperty("$type")
    public String type() {
        return getClass().getSimpleName();
    }
}