package io.github.mattae.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class PluginWebMvcEndpointHandlerMapping extends WebMvcEndpointHandlerMapping {
    public PluginWebMvcEndpointHandlerMapping(EndpointMapping endpointMapping, Collection<ExposableWebEndpoint> endpoints,
                                              EndpointMediaTypes endpointMediaTypes, CorsConfiguration corsConfiguration,
                                              EndpointLinksResolver linksResolver, boolean shouldRegisterLinksMapping) {
        super(endpointMapping, endpoints, endpointMediaTypes, corsConfiguration, linksResolver, shouldRegisterLinksMapping);
    }

    public void registerEndpoints(SpringBootPlugin springBootPlugin) {
        unregisterEndpoints(springBootPlugin);

        getWebEndpoints(springBootPlugin.getApplicationContext()).forEach((endpoint) -> {
            for (WebOperation operation : endpoint.getOperations()) {
                WebOperationRequestPredicate predicate = operation.getRequestPredicate();
                String path = predicate.getPath();
                String matchAllRemainingPathSegmentsVariable = predicate.getMatchAllRemainingPathSegmentsVariable();
                if (matchAllRemainingPathSegmentsVariable != null) {
                    path = path.replace("{*" + matchAllRemainingPathSegmentsVariable + "}", "**");
                }

                registerMapping(endpoint, predicate, operation, path);
            }
        });
    }

    public void unregisterEndpoints(SpringBootPlugin springBootPlugin) {

        getWebEndpoints(springBootPlugin.getApplicationContext()).forEach(endpoint -> {
            for (WebOperation operation : endpoint.getOperations()) {
                WebOperationRequestPredicate predicate = operation.getRequestPredicate();
                String path = predicate.getPath();

                Method initMethod;
                initMethod = ReflectionUtils.findMethod(getClass(), "createRequestMappingInfo",
                        WebOperationRequestPredicate.class, String.class);
                assert initMethod != null;
                ReflectionUtils.makeAccessible(initMethod);
                try {
                    RequestMappingInfo mappingInfo = (RequestMappingInfo) initMethod.invoke(this, predicate, path);
                    getHandlerMethods().forEach((mapping, handlerMethod) -> {
                        if (mappingInfo.equals(mapping)) {
                            unregisterMapping(mapping);
                        }
                    });
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        getEndpointObjects(springBootPlugin.getApplicationContext())
                .forEach(springBootPlugin::unregisterBeanFromMainContext);
    }

    private Collection<ExposableWebEndpoint> getWebEndpoints(ApplicationContext applicationContext) {
        return applicationContext.getBean(WebEndpointDiscoverer.class).getEndpoints();
    }

    private Set<Object> getEndpointObjects(ApplicationContext applicationContext) {
        return new LinkedHashSet<>(applicationContext.getBeansWithAnnotation(WebEndpoint.class)
                .values().stream().toList());
    }
}
