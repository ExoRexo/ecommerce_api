package alexo.ecommerce_api.configuration.security;

import alexo.ecommerce_api.dto.http.response.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


/**
 * Returns unified JSON response for forbidden requests (HTTP 403).
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * Writes unified error envelope when authenticated user has no permission.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param accessDeniedException access denied exception
     * @throws IOException when response write fails
     */
    @Override
    public void handle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(
                response.getOutputStream(),
                ApiResponseDTO.failure(List.of(resolveMessage(accessDeniedException)))
        );
    }

    /**
     * Resolves safe message to return to client.
     *
     * @param exception source exception
     * @return exception message or default access denied message
     */
    private String resolveMessage(Exception exception) {
        if (exception.getMessage() == null || exception.getMessage().isBlank()) {
            return "Access is denied";
        }
        return exception.getMessage();
    }
}