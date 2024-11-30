package io.github.mattae.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.PluginEntityManagerFactoryBeanRegister;
import org.laxture.sbp.spring.boot.PluginPersistenceManagedTypes;
import org.laxture.sbp.spring.boot.SbpJpaConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

class SnlJpaConfigurer extends SbpJpaConfigurer {

    public SnlJpaConfigurer(String[] modelPackages) {
        super(modelPackages);
    }

    @Override
    public void onBootstrap(SpringBootstrap bootstrap, GenericApplicationContext pluginApplicationContext) {
        Thread.currentThread().setContextClassLoader(pluginApplicationContext.getClassLoader());
        super.onBootstrap(bootstrap, pluginApplicationContext);
    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        PluginPersistenceManagedTypes persistenceManagedTypes = (PluginPersistenceManagedTypes)
                plugin.getMainApplicationContext().getBean("persistenceManagedTypes");
        persistenceManagedTypes.unregisterPackage(plugin.getApplicationContext());
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                plugin.getMainApplicationContext().getBean(LocalContainerEntityManagerFactoryBean.class);
        PluginEntityManagerFactoryBeanRegister.unregisterClassloader(entityManagerFactory, plugin.getWrapper().getPluginClassLoader());
        entityManagerFactory.setManagedTypes(persistenceManagedTypes);

    }
}
