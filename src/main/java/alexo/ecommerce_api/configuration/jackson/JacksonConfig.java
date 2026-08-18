package alexo.ecommerce_api.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Jackson mapper used by API serialization.
 */
@Configuration
public class JacksonConfig {

    /**
     * Override default Spring Doc Object mapper provider and add the JsonNullableModule - otherwise the OpenAPI JSON
     * Spec interprets the JsonNullable wrong
     *
     * @param springDocConfigProperties given config properties in application yaml
     * @return provider for spring doc generation
     */
    @Bean
    @ConditionalOnBean(SpringDocConfigProperties.class)
    public ObjectMapperProvider springdocObjectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {
        ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider(springDocConfigProperties);
        objectMapperProvider.jsonMapper().registerModule(jsonNullableModule());
        return objectMapperProvider;
    }

    @Bean
    public ObjectMapper objectMapper(JsonNullableModule jsonNullableModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(jsonNullableModule);
        return objectMapper;
    }

    /**
     * Register JsonNullableModule for openapi generation in springdoc (above) and for the default object mapper
     * used to serialize/deserialize JSON Payloads via web requests - otherwise the application is not able to mmap
     * JsonNullable<Type> fields
     *
     * @return json nullable module
     */
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }
}