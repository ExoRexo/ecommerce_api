package alexo.ecommerce_api.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configures Jackson mapper used by API serialization.
 */
@Configuration
public class JacksonConfig {

    /**
     * Registers Java Time module for {@code Instant} and other temporal types.
     *
     * @return Jackson module instance
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    /**
     * Provides primary object mapper for the application.
     *
     * @return configured object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper(JavaTimeModule javaTimeModule) {
        return new ObjectMapper().registerModule(javaTimeModule).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}