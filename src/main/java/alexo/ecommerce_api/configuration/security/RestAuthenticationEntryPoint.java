package alexo.ecommerce_api.configuration.security;

import alexo.ecommerce_api.configuration.error.ErrorResponseProperties;
import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Returns unified JSON response for unauthorized requests (HTTP 401).
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final ErrorResponseProperties errorResponseProperties;

    /**
     * Writes unified error envelope for authentication failures.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param authException authentication exception
     * @throws IOException when response write fails
     */
    @Override
    public void commence(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(
                response.getOutputStream(),
                ApiResponseDTO.failure(List.of(resolveMessage(authException)))
        );
    }

    /**
     * Resolves safe message to return to client.
     *
     * @param exception source exception
     * @return exception message or default authentication message
     */
    private String resolveMessage(Exception exception) {
        if (!errorResponseProperties.includeDetails()
                || exception.getMessage() == null
                || exception.getMessage().isBlank()) {
            return "Authentication is required";
        }
        return exception.getMessage();
    }
}