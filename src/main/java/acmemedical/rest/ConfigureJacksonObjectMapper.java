/********************************************************************************************************
 * File:  ConfigureJacksonObjectMapper.java
 * Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Mike Norman
 * 
 * Note:  Students do NOT need to change anything in this class.
 */
package acmemedical.rest;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class ConfigureJacksonObjectMapper implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper objectMapper;

    public ConfigureJacksonObjectMapper() {
        this.objectMapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    //Configure JDK 8's new DateTime objects to use proper ISO-8601 time format
    protected ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // Lenient parsing of JSON - if a field has a typo, don't fall to pieces
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // Handle circular references
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            // Handle polymorphic types more carefully
            .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        
        return mapper;
    }
}