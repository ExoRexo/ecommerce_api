package alexo.ecommerce_api.dto.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Standard API envelope used by all JSON endpoints.
 *
 * @param <T> successful response payload type
 * @param payload successful response payload, can be primitive, object, or list
 * @param errors list of error messages, empty for success responses
 * @param date response creation timestamp in UTC
 */
public record ApiResponseDTO<T>(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        T payload,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<String> errors,

        @JsonInclude(JsonInclude.Include.ALWAYS)
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Instant date
) {

    public ApiResponseDTO {
        date = date == null ? Instant.now() : date;
    }

    /**
     * Creates successful API envelope.
     *
     * @param payload successful response payload
     * @return envelope with empty error list
     */
    public static <T> ApiResponseDTO<T> success(T payload) {
        return new ApiResponseDTO<>(payload, List.of(), null);
    }

    public static ApiResponseDTO<Void> success() {
        return success(null);
    }

    public static ApiResponseDTO<Void> failure(List<String> errors) {
        return new ApiResponseDTO<>(null, errors, null);
    }
}