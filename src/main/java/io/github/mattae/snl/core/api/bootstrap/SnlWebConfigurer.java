package io.github.mattae.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.internal.PluginRequestMappingAdapter;
import org.laxture.sbp.spring.boot.configurer.SbpWebConfigurer;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;

class SnlWebConfigurer extends SbpWebConfigurer {
    @Override
    public void onStart(SpringBootPlugin plugin) {
        try {
            getMainRequestMapping(plugin).registerControllers(plugin);
            getMainRequestMapping(plugin).registerRouterFunction(plugin);
            PluginWebMvcEndpointHandlerMapping endpointMapper = (PluginWebMvcEndpointHandlerMapping) plugin
                    .getMainApplicationContext().getBean(WebMvcEndpointHandlerMapping.class);
            endpointMapper.registerEndpoints(plugin);
        } catch (Exception e) {
            onStop(plugin);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        PluginWebMvcEndpointHandlerMapping endpointMapper = (PluginWebMvcEndpointHandlerMapping) plugin
                .getMainApplicationContext().getBean(WebMvcEndpointHandlerMapping.class);
        endpointMapper.unregisterEndpoints(plugin);
        super.onStop(plugin);
    }

    private PluginRequestMappingAdapter getMainRequestMapping(SpringBootPlugin plugin) {
        return (PluginRequestMappingAdapter)
                plugin.getMainApplicationContext().getBean("requestMappingHandlerMapping");
    }

}
