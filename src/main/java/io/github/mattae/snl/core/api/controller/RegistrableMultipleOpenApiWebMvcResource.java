package io.github.mattae.snl.core.api.controller;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.util.BeanUtil;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.filters.GlobalOpenApiMethodFilter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RegistrableMultipleOpenApiWebMvcResource extends MultipleOpenApiWebMvcResource {

    private final Map<String, List<Class<?>>> controllerMap = Collections.synchronizedMap(new HashMap<>());

    private final ApplicationContext applicationContext;

    public RegistrableMultipleOpenApiWebMvcResource(ApplicationContext applicationContext,
                                                    List<GroupedOpenApi> groupedOpenApis,
                                                    ObjectFactory<OpenAPIService> defaultOpenAPIBuilder,
                                                    AbstractRequestService requestBuilder,
                                                    GenericResponseService responseBuilder,
                                                    OperationService operationParser,
                                                    SpringDocConfigProperties springDocConfigProperties,
                                                    SpringDocProviders springDocProviders,
                                                    SpringDocCustomizers springDocCustomizers) {
        super(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder, responseBuilder,
                operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
        this.applicationContext = applicationContext;
    }

    //*************************************************************************
    // Reflection Getter/Setter
    //*************************************************************************

    List<GroupedOpenApi> getGroupedOpenApis() {
        return BeanUtil.getFieldValue(this, "groupedOpenApis");
    }

    void setGroupedOpenApiResources(Map<String, OpenApiResource> groupedOpenApiResources) {
        BeanUtil.setFieldValue(this, "groupedOpenApiResources", groupedOpenApiResources);
    }

    SpringDocConfigProperties getSpringDocConfigProperties() {
        return BeanUtil.getFieldValue(this, "springDocConfigProperties");
    }

    //*************************************************************************
    // Override
    //*************************************************************************

    public void afterPropertiesSet() {
        Map<String, GlobalOpenApiCustomizer> globalOpenApiCustomizerMap = applicationContext.getBeansOfType(GlobalOpenApiCustomizer.class);
        Map<String, GlobalOperationCustomizer> globalOperationCustomizerMap = applicationContext.getBeansOfType(GlobalOperationCustomizer.class);
        Map<String, GlobalOpenApiMethodFilter> globalOpenApiMethodFilterMap = applicationContext.getBeansOfType(GlobalOpenApiMethodFilter.class);

        this.getGroupedOpenApis().forEach(groupedOpenApi -> groupedOpenApi
                .addAllOpenApiCustomizer(globalOpenApiCustomizerMap.values())
                .addAllOperationCustomizer(globalOperationCustomizerMap.values())
                .addAllOpenApiMethodFilter(globalOpenApiMethodFilterMap.values())
        );

        setGroupedOpenApiResources(getGroupedOpenApis().stream()
                .collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
                        {
                            SpringDocConfigProperties.GroupConfig groupConfig = new SpringDocConfigProperties.GroupConfig(
                                    item.getGroup(), item.getPathsToMatch(), item.getPackagesToScan(),
                                    item.getPackagesToExclude(), item.getPathsToExclude(),
                                    item.getProducesToMatch(), item.getConsumesToMatch(),
                                    item.getHeadersToMatch(), item.getDisplayName());
                            getSpringDocConfigProperties().addGroupConfig(groupConfig);
                            return buildWebMvcOpenApiResourceHack(item);
                        }
                )));
    }

    private OpenApiResource buildWebMvcOpenApiResourceHack(GroupedOpenApi item) {
        OpenApiResource res = BeanUtil.callMethod(this, "buildWebMvcOpenApiResource", item);
        if (controllerMap.containsKey(item.getGroup())) {
            controllerMap.get(item.getGroup()).forEach(AbstractOpenApiResource::addRestControllers);
        }
        return res;
    }

    public void registerPlugin(SpringBootPlugin plugin, GroupedOpenApi groupedOpenApi) {
        getGroupedOpenApis().add(groupedOpenApi);
        this.controllerMap.put(groupedOpenApi.getGroup(), Stream.concat(Stream.concat( //Stream.concat(
                                plugin.getApplicationContext().getBeansOfType(Controller.class).values().stream(),
                                plugin.getApplicationContext().getBeansWithAnnotation(RestController.class).values().stream()),
                        plugin.getMainApplicationContext().getBeansOfType(org.springframework.web.servlet.function.RouterFunction.class).values().stream())
                .map(Object::getClass)
                .filter(controllerClass -> controllerClass.getClassLoader() == plugin.getWrapper().getPluginClassLoader())
                .collect(Collectors.toList()));
        this.afterPropertiesSet();
    }

    public void unregisterPlugin(String group) {
        getGroupedOpenApis().removeIf(item -> item.getGroup().equals(group));
        this.controllerMap.remove(group);
        this.afterPropertiesSet();
    }

}
