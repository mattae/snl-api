package io.github.mattae.snl.core.api.bootstrap;

import io.github.mattae.snl.core.api.domain.CoreDomain;
import org.apache.commons.lang3.ArrayUtils;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.pf4j.PluginWrapper;

import java.util.List;
import java.util.Set;

public abstract class JpaSpringBootPlugin extends SpringBootPlugin {

    public JpaSpringBootPlugin(PluginWrapper wrapper, List<Class<?>> modelPackages, IPluginConfigurer... pluginConfigurers) {
        super(wrapper, ArrayUtils.addAll(pluginConfigurers, new SnlWebConfigurer(),
                new SnlJpaConfigurer(ArrayUtils.addAll(modelPackages.stream()
                                .map(Class::getPackageName).toArray(String[]::new),
                        CoreDomain.class.getPackageName())), new BlazePersistenceConfigurer(modelPackages)));
    }

    public Set<String> getExcludeConfigurations() {
        Set<String> configurations = super.getExcludeConfigurations();
        configurations.add("com.blazebit.persistence.spring.data.webmvc.impl.BlazePersistenceWebConfiguration");
        return configurations;
    }
}
