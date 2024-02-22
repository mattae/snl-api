package io.github.jbella.snl.core.api.bootstrap;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.impl.AbstractEntityViewConfigurationSource;
import com.blazebit.persistence.integration.view.spring.impl.SpringTransactionSupport;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.ConfigurationProperties;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import io.github.jbella.snl.core.api.config.AuditViewListenersConfiguration;
import io.github.jbella.snl.core.api.domain.CoreDomain;
import jakarta.persistence.EntityManagerFactory;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.laxture.sbp.spring.boot.PluginPersistenceManagedTypes;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import java.util.*;

class BlazePersistenceConfigurer implements IPluginConfigurer {
    private final List<Class<?>> modelPackages;

    BlazePersistenceConfigurer(List<Class<?>> modelPackages) {
        this.modelPackages = modelPackages;
    }

    @Override
    public void onBootstrap(SpringBootstrap bootstrap, GenericApplicationContext pluginApplicationContext) {
        EntityViewConfiguration configuration = getEntityViewConfiguration(bootstrap, pluginApplicationContext);


        pluginApplicationContext.registerBean("entityViewManager", EntityViewManager.class, () ->
                createEntityViewManager(createCriteriaBuilderFactory(bootstrap), configuration));

        pluginApplicationContext.registerBean("criteriaBuilderFactory", CriteriaBuilderFactory.class,
                () -> createCriteriaBuilderFactory(bootstrap));
    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        PluginPersistenceManagedTypes persistenceManagedTypes = (PluginPersistenceManagedTypes)
                plugin.getMainApplicationContext().getBean("persistenceManagedTypes");
        persistenceManagedTypes.unregisterPackage(plugin.getApplicationContext());
    }

    private CriteriaBuilderFactory createCriteriaBuilderFactory(SpringBootstrap bootstrap) {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(bootstrap.getMainApplicationContext().getBean(EntityManagerFactory.class));
    }

    private EntityViewConfiguration getEntityViewConfiguration(SpringBootstrap bootstrap, ApplicationContext applicationContext) {
        EntityViewConfiguration configuration = EntityViews.createDefaultConfiguration();
        configuration.setTransactionSupport(new SpringTransactionSupport(
                bootstrap.getMainApplicationContext().getBean(PlatformTransactionManager.class)));
        var configurationSource = getEntityViewConfigurationSource(bootstrap, applicationContext);

        Set<Class<?>> entityViewClasses = new HashSet<>();
        Set<Class<?>> entityViewListenerClasses = new HashSet<>();
        for (BeanDefinition candidate : configurationSource.getCandidates(applicationContext)) {
            try {
                Class<?> clazz = ClassUtils.forName(Objects.requireNonNull(candidate.getBeanClassName()),
                        applicationContext.getClassLoader());
                if (clazz.isAnnotationPresent(EntityView.class)) {
                    entityViewClasses.add(clazz);
                } else {
                    entityViewListenerClasses.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        for (Class<?> entityViewClass : entityViewClasses) {
            configuration.addEntityView(entityViewClass);
        }
        for (Class<?> entityViewListenerClass : entityViewListenerClasses) {
            configuration.addEntityViewListener(entityViewListenerClass);
        }

        return configuration;
    }

    private AbstractEntityViewConfigurationSource getEntityViewConfigurationSource(SpringBootstrap bootstrap, ApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        return new AbstractEntityViewConfigurationSource(env) {

            @Override
            public Iterable<String> getBasePackages() {
                List<String> scanPackages = new ArrayList<>(modelPackages.stream()
                        .map(Class::getPackageName).toList());
                PluginPersistenceManagedTypes persistenceManagedTypes = (PluginPersistenceManagedTypes)
                        bootstrap.getMainApplicationContext().getBean("persistenceManagedTypes");
                scanPackages.addAll(persistenceManagedTypes.getManagedPackages());
                scanPackages.addAll(persistenceManagedTypes.getManagedClassNames());
                scanPackages.add(CoreDomain.class.getPackageName());

                return scanPackages;
            }

            @Override
            protected Iterable<TypeFilter> getExcludeFilters() {
                return Collections.emptyList();
            }

            @Override
            protected Iterable<TypeFilter> getIncludeFilters() {
                return Collections.emptyList();
            }
        };
    }

    private EntityViewManager createEntityViewManager(
            CriteriaBuilderFactory cbf, EntityViewConfiguration entityViewConfiguration) {
        entityViewConfiguration.setProperty(ConfigurationProperties.UPDATER_STRICT_CASCADING_CHECK, "false");
        entityViewConfiguration.setProperty(ConfigurationProperties.UPDATER_FLUSH_MODE, "partial");
        entityViewConfiguration.setTypeTestValue(UUID.class, UUID.randomUUID());
        entityViewConfiguration.addEntityViewListener(AuditViewListenersConfiguration.class);
        return entityViewConfiguration.createEntityViewManager(cbf);
    }
}