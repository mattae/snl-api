package io.github.mattae.snl.test.core;

import com.blazebit.persistence.integration.jackson.EntityViewIdValueAccessor;
import com.blazebit.persistence.spring.data.webmvc.impl.json.EntityViewAwareMappingJackson2HttpMessageConverter;
import com.blazebit.persistence.spring.data.webmvc.impl.json.EntityViewIdValueHolder;
import com.blazebit.persistence.view.EntityViewManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.scalars.ExtendedScalars;
import io.github.mattae.snl.core.api.services.errors.ExceptionTranslator;
import org.laxture.sbp.mock.MockSpringBootPluginManager;
import org.laxture.sbp.mock.MockSpringExtensionFactory;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.pf4j.PluginRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
public class TestConfiguration {
    private final EntityViewManager evm;
    private final ApplicationContext applicationContext;
    private final ConversionService conversionService;

    public TestConfiguration(EntityViewManager evm, ApplicationContext applicationContext,
                             @Lazy @Qualifier("mvcConversionService") ConversionService conversionService) {
        this.evm = evm;
        this.applicationContext = applicationContext;
        this.conversionService = conversionService;
    }

    @Bean
    public ObjectMapper entityViewAwareObjectMapper() {
        var objectMapper = new EntityViewAwareMappingJackson2HttpMessageConverter(evm,
                blazeWebmvcIdAttributeAccessor(), new ObjectMapper()).getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public PluginManager getPluginManager() {
        return new MockSpringBootPluginManager(applicationContext) {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new MockSpringExtensionFactory(applicationContext) {
                    @Override
                    public <T> T create(Class<T> extensionClass) {
                        try {
                            return applicationContext.getBean(extensionClass);
                        } catch (Exception e) {
                            throw new PluginRuntimeException(e);
                        }
                    }
                };
            }
        };
    }

    private EntityViewIdValueAccessor blazeWebmvcIdAttributeAccessor() {
        return new EntityViewIdValueHolder(conversionService);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> {
            wiringBuilder.scalar(ExtendedScalars.LocalTime);
            wiringBuilder.scalar(ExtendedScalars.GraphQLChar);
            wiringBuilder.scalar(ExtendedScalars.CountryCode);
            wiringBuilder.scalar(ExtendedScalars.Currency);
            wiringBuilder.scalar(ExtendedScalars.Date);
            wiringBuilder.scalar(ExtendedScalars.GraphQLBigDecimal);
            wiringBuilder.scalar(ExtendedScalars.GraphQLBigInteger);
            wiringBuilder.scalar(ExtendedScalars.GraphQLByte);
            wiringBuilder.scalar(ExtendedScalars.GraphQLLong);
            wiringBuilder.scalar(ExtendedScalars.GraphQLShort);
            wiringBuilder.scalar(ExtendedScalars.Locale);
            wiringBuilder.scalar(ExtendedScalars.NegativeFloat);
            wiringBuilder.scalar(ExtendedScalars.NegativeInt);
            wiringBuilder.scalar(ExtendedScalars.NonNegativeFloat);
            wiringBuilder.scalar(ExtendedScalars.NonNegativeInt);
            wiringBuilder.scalar(ExtendedScalars.NonPositiveFloat);
            wiringBuilder.scalar(ExtendedScalars.NonPositiveInt);
            wiringBuilder.scalar(ExtendedScalars.Object);
            wiringBuilder.scalar(ExtendedScalars.PositiveFloat);
            wiringBuilder.scalar(ExtendedScalars.PositiveInt);
            wiringBuilder.scalar(ExtendedScalars.Time);
            wiringBuilder.scalar(ExtendedScalars.Url);
            wiringBuilder.scalar(ExtendedScalars.UUID);
        };
    }

    @ControllerAdvice
    class ControllerExceptionHandler extends ExceptionTranslator {

    }
}
