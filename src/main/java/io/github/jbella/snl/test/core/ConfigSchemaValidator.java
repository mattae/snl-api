package io.github.jbella.snl.test.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Set;

@Slf4j
public class ConfigSchemaValidator {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper(new YAMLFactory());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @SneakyThrows
    public static boolean isValid(String config, Boolean... validateResources) {
        URI schemaFile = ConfigSchemaValidator.class.getClassLoader().getResource("config-schema.json").toURI();
        JsonSchemaFactory factory = JsonSchemaFactory.builder(JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V201909)).objectMapper(MAPPER).build();

        Set<ValidationMessage> invalidMessages = factory.getSchema(schemaFile)
                .validate(MAPPER.readTree(config));
        if (!invalidMessages.isEmpty()) {
            log.debug("Schema validation failed:\n {}", config);
            invalidMessages.forEach(m -> System.out.printf("...%s", m.getMessage()));
        }

        return invalidMessages.isEmpty();
    }
}
