package alexo.ecommerce_api.configuration.error;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.errors")
public record ErrorResponseProperties(boolean includeDetails) {
}
