package io.github.mattae.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.laxture.sbp.util.BeanUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class MethodAuthorizationAccessDeniedConfigurer implements IPluginConfigurer {
    private final Set<String> beanNames = new HashSet<>();

    @Override
    public void afterBootstrap(SpringBootstrap bootstrap, GenericApplicationContext pluginApplicationContext) {
        SpringBootPlugin plugin = pluginApplicationContext.getBean(SpringBootPlugin.class);
        getMethodAuthorizationDeniedHandlers(pluginApplicationContext).entrySet()
                .forEach(entry -> {
                    var bean = entry.getValue();
                    var beanName = entry.getKey() + "." + plugin.getWrapper().getPluginId();
                    var mainApplicationContext = bootstrap.getMainApplicationContext();
                    importBeanDefinition(pluginApplicationContext, mainApplicationContext, entry.getClass().getName());
                    mainApplicationContext.getBeanFactory().registerSingleton(beanName, bean);
                    mainApplicationContext.getBeanFactory().autowireBean(bean);
                    beanNames.add(beanName);
                });
    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        beanNames.forEach(beanName -> {
            var mainApplicationContext = plugin.getMainApplicationContext();
            ((AbstractAutowireCapableBeanFactory) mainApplicationContext.getBeanFactory()).destroySingleton(beanName);
        });
    }

    private Map<String, MethodAuthorizationDeniedHandler> getMethodAuthorizationDeniedHandlers(ApplicationContext applicationContext) {
        return applicationContext.getBeansOfType(MethodAuthorizationDeniedHandler.class);
    }

    private void importBeanDefinition(GenericApplicationContext sourceApplicationContext,
                                      GenericApplicationContext applicationContext,
                                      String beanName) {
        try {
            RootBeanDefinition bd = (RootBeanDefinition)
                    sourceApplicationContext.getBeanFactory().getMergedBeanDefinition(beanName);
            RootBeanDefinition copiedBd = new RootBeanDefinition(bd);
            copiedBd.setLazyInit(false);
            copiedBd.setFactoryBeanName(null);
            BeanUtil.setFieldValue(copiedBd, "isFactoryBean", false);
            applicationContext.registerBeanDefinition(beanName, copiedBd);
        } catch (NoSuchBeanDefinitionException ignored) {
        }
    }
}
