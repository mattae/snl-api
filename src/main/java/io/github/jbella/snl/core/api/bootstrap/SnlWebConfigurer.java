package io.github.jbella.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.internal.PluginRequestMappingAdapter;
import org.laxture.sbp.spring.boot.configurer.SbpWebConfigurer;

class SnlWebConfigurer extends SbpWebConfigurer {
    @Override
    public void onStart(SpringBootPlugin plugin) {
        try {
            getMainRequestMapping(plugin).registerControllers(plugin);
            getMainRequestMapping(plugin).registerRouterFunction(plugin);
        } catch (Exception e) {
            onStop(plugin);
            throw new RuntimeException(e);
        }

    }

    private PluginRequestMappingAdapter getMainRequestMapping(SpringBootPlugin plugin) {
        return (PluginRequestMappingAdapter)
                plugin.getMainApplicationContext().getBean("requestMappingHandlerMapping");
    }
}