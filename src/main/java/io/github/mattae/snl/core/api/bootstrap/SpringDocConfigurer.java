package io.github.mattae.snl.core.api.bootstrap;

import io.github.mattae.snl.boot.controller.RegistrableMultipleOpenApiWebMvcResource;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.laxture.sbp.util.BeanUtil;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Map;

class SpringDocConfigurer implements IPluginConfigurer {

    @Override
    public String[] excludeConfigurations() {
        return new String[]{
                "org.springdoc.core.configuration.SpringDocConfiguration",
                "org.springdoc.core.properties.SpringDocConfigProperties",
                "org.springdoc.core.configuration.SpringDocJavadocConfiguration",
                "org.springdoc.core.configuration.SpringDocGroovyConfiguration",
                "org.springdoc.core.configuration.SpringDocSecurityConfiguration",
                "org.springdoc.core.configuration.SpringDocFunctionCatalogConfiguration",
                "org.springdoc.core.configuration.SpringDocHateoasConfiguration",
                "org.springdoc.core.configuration.SpringDocPageableConfiguration",
                "org.springdoc.core.configuration.SpringDocSortConfiguration",
                "org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration",
                "org.springdoc.core.configuration.SpringDocDataRestConfiguration",
                "org.springdoc.core.configuration.SpringDocKotlinConfiguration",
                "org.springdoc.core.configuration.SpringDocKotlinxConfiguration",
                "org.springdoc.core.configuration.SpringDocJacksonKotlinModuleConfiguration",
                "org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration",
                "org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration",
                "org.springdoc.webmvc.ui.SwaggerConfig",
                "org.springdoc.core.properties.SwaggerUiConfigProperties",
                "org.springdoc.core.properties.SwaggerUiOAuthProperties",
                "org.springdoc.core.configuration.SpringDocUIConfiguration"
        };
    }

    @Override
    public void onStart(SpringBootPlugin plugin) {
        if (plugin.getMainApplicationContext().getBeanNamesForType(RegistrableMultipleOpenApiWebMvcResource.class).length > 0) {
            RegistrableMultipleOpenApiWebMvcResource mvcResource =
                    plugin.getMainApplicationContext().getBean(RegistrableMultipleOpenApiWebMvcResource.class);
            try {
                GroupedOpenApi groupedOpenApi = plugin.getApplicationContext().getBean(GroupedOpenApi.class);
                mvcResource.registerPlugin(plugin, groupedOpenApi);
            } catch (Exception e) {

            }
        }
        refreshCacheIfNeeded(plugin.getMainApplicationContext());
    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        if (plugin.getMainApplicationContext().getBeanNamesForType(RegistrableMultipleOpenApiWebMvcResource.class).length > 0) {
            RegistrableMultipleOpenApiWebMvcResource mvcResource =
                    plugin.getMainApplicationContext().getBean(RegistrableMultipleOpenApiWebMvcResource.class);
            try {
                GroupedOpenApi groupedOpenApi = plugin.getApplicationContext().getBean(GroupedOpenApi.class);
                mvcResource.unregisterPlugin(groupedOpenApi.getGroup());
            } catch (Exception e) {
            }
        }
        refreshCacheIfNeeded(plugin.getMainApplicationContext());
    }

    public void refreshCacheIfNeeded(GenericApplicationContext applicationContext) {
        SpringDocConfigProperties springDocConfigProperties =
                applicationContext.getBean(SpringDocConfigProperties.class);
        if (!springDocConfigProperties.isCacheDisabled()) {
            OpenAPIService openApiService =
                    applicationContext.getBean(OpenAPIService.class);
            BeanUtil.<Map<?, ?>>getFieldValue(openApiService, "cachedOpenAPI").clear();
        }
    }
}
