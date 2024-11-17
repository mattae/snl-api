package io.github.mattae.snl.boot;

import io.github.mattae.snl.core.api.controller.RegistrableMultipleOpenApiWebMvcResource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laxture.sbp.SpringBootPlugin;
import org.springdoc.core.conditions.MultipleOpenApiSupportCondition;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.List;

import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@Conditional(MultipleOpenApiSupportCondition.class)
@AutoConfigureBefore(MultipleOpenApiSupportConfiguration.class)
@ConditionalOnMissingBean(SpringBootPlugin.class)
@RequiredArgsConstructor
@Slf4j
public class SpringDocWebMvcAppAutoConfiguration {
    private final ApplicationContext applicationContext;

    @Bean
    @Primary
    @Lazy(false)
    @ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
    MultipleOpenApiWebMvcResource multipleOpenApiResource(
            ApplicationContext applicationContext,
            List<GroupedOpenApi> groupedOpenApis,
            ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
            GenericResponseService responseBuilder, OperationService operationParser,
            SpringDocConfigProperties springDocConfigProperties,
            SpringDocProviders springDocProviders,
            SpringDocCustomizers springDocCustomizers) {
        return new RegistrableMultipleOpenApiWebMvcResource(
            applicationContext,
            groupedOpenApis,
            defaultOpenAPIBuilder, requestBuilder,
            responseBuilder, operationParser,
            springDocConfigProperties,
            springDocProviders,
            springDocCustomizers);
    }

    @PostConstruct
    public void init() {
        log.info("SpringDocWebMvcAppAutoConfiguration created");
    }
}
