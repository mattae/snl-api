package io.github.mattae.snl.core.api.bootstrap;

import org.apache.commons.lang3.ArrayUtils;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.laxture.sbp.spring.boot.configurer.SbpDataSourceConfigurer;
import org.pf4j.PluginWrapper;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Set;


public abstract class DataSourceSpringBootPlugin extends SpringBootPlugin {
    public DataSourceSpringBootPlugin(PluginWrapper wrapper, IPluginConfigurer... pluginConfigurers) {
        super(wrapper, ArrayUtils.addAll(pluginConfigurers, new SnlWebConfigurer(),
                new SbpDataSourceConfigurer(), new SpringDocConfigurer(), new MethodAuthorizationAccessDeniedConfigurer()));
    }

    public Set<String> getExcludeConfigurations() {
        Set<String> configurations = super.getExcludeConfigurations();
        configurations.add("com.blazebit.persistence.spring.data.webmvc.impl.BlazePersistenceWebConfiguration");
        return configurations;
    }

    @Override
    public void onPluginBootstrap(SpringBootstrap bootstrap, GenericApplicationContext pluginApplicationContext) {
        super.onPluginBootstrap(bootstrap, pluginApplicationContext);
    }
}
