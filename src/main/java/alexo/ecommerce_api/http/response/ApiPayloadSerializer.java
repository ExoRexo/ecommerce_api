package alexo.ecommerce_api.http.response;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Converts arbitrary controller payload into JSON-friendly structure and
 * appends {@code $type} for object payloads.
 */
@Component
@RequiredArgsConstructor
public class ApiPayloadSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Serializes payload into primitives, lists, and maps.
     *
     * @param payload raw controller payload
     * @return serialized payload compatible with unified API envelope
     */
    public Object serialize(Object payload) {
        return toTypedValue(payload);
    }

    /**
     * Recursively converts value into plain Java types suitable for JSON output.
     *
     * @param value source value
     * @return converted value with nested typing rules applied
     */
    private Object toTypedValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof TemporalAccessor temporalAccessor) {
            return temporalAccessor.toString();
        }

        if (isScalar(value)) {
            return value;
        }

        if (value instanceof Iterable<?> iterable) {
            List<Object> result = new ArrayList<>();
            for (Object item : iterable) {
                result.add(toTypedValue(item));
            }
            return result;
        }

        if (value.getClass().isArray()) {
            List<Object> result = new ArrayList<>();
            int length = Array.getLength(value);
            for (int index = 0; index < length; index++) {
                result.add(toTypedValue(Array.get(value, index)));
            }
            return result;
        }

        if (value instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(String.valueOf(entry.getKey()), toTypedValue(entry.getValue()));
            }
            result.put("$type", shortType(value));
            return result;
        }

        Map<String, Object> result = new LinkedHashMap<>();

        for (BeanPropertyDefinition property : serializableProperties(value)) {
            AnnotatedMember accessor = property.getAccessor();
            if (accessor == null) {
                continue;
            }

            try {
                accessor.fixAccess(true);
                Object propertyValue = accessor.getValue(value);
                result.put(property.getName(), toTypedValue(propertyValue));
            } catch (Exception ignored) {
                // Skip inaccessible property.
            }
        }

        result.put("$type", shortType(value));

        return result;
    }

    /**
     * Reads serializable bean properties from runtime class.
     *
     * @param value object to introspect
     * @return iterable set of serializable property definitions
     */
    private Iterable<BeanPropertyDefinition> serializableProperties(Object value) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ClassUtils.getUserClass(value));
        BeanDescription beanDescription = objectMapper.getSerializationConfig().introspect(javaType);
        return beanDescription.findProperties();
    }

    /**
     * Determines whether value should be emitted as scalar JSON value.
     *
     * @param value value to classify
     * @return {@code true} for primitive-like values
     */
    private boolean isScalar(Object value) {
        Class<?> userClass = ClassUtils.getUserClass(value);
        return userClass.isPrimitive()
                || value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Enum<?>
                || value instanceof Date
                || value instanceof TemporalAccessor
                || value instanceof UUID;
    }

    /**
     * Returns short runtime class name for `$type` field.
     *
     * @param value source object
     * @return simple class name
     */
    private String shortType(Object value) {
        return ClassUtils.getUserClass(value).getSimpleName();
    }
}