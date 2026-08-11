package alexo.ecommerce_api.http.controller.advice;

import alexo.ecommerce_api.dto.http.response.PageResponseDTO;
import alexo.ecommerce_api.http.response.ApiPayloadSerializer;
import alexo.ecommerce_api.http.response.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Globally wraps JSON responses into {@link ApiResponse} envelope.
 * Leaves OpenAPI and Swagger infrastructure endpoints untouched so generated docs remain valid.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ApiPayloadSerializer apiPayloadSerializer;
    private final ObjectMapper objectMapper;

    /**
     * Applies to all controller methods; actual filtering is done by media type.
     *
     * @param returnType controller return metadata
     * @param converterType selected message converter
     * @return always {@code true}
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Wraps response body into standard envelope for JSON responses.
     * Skips Swagger-related endpoints because they must return raw OpenAPI payloads.
     *
     * @param body raw controller body
     * @param returnType controller return metadata
     * @param selectedContentType selected content type
     * @param selectedConverterType selected converter
     * @param request HTTP request
     * @param response HTTP response
     * @return wrapped response body
     */
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        String path = request.getURI().getPath();

        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        if (selectedContentType != null && !selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return body;
        }

        if (body instanceof JsonNode jsonNode) {
            Object normalized = objectMapper.convertValue(jsonNode, Object.class);
            return ApiResponse.success(normalized);
        }

        if (body instanceof PageResponseDTO<?> page) {
            return ApiResponse.success(apiPayloadSerializer.serialize(page));
        }

        return ApiResponse.success(apiPayloadSerializer.serialize(body));
    }
}