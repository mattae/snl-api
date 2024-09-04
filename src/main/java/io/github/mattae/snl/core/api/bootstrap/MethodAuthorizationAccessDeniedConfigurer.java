package io.github.mattae.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.IPluginConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.laxture.sbp.util.BeanUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodAuthorizationAccessDeniedConfigurer implements IPluginConfigurer {
    private final Set<String> beanNames = new HashSet<>();

    @Override
    public void afterBootstrap(SpringBootstrap bootstrap, GenericApplicationContext pluginApplicationContext) {

        getMethodAuthorizationDeniedHandlers(pluginApplicationContext).forEach((beanName, bean) -> {
            var mainApplicationContext = bootstrap.getMainApplicationContext();
            importBeanDefinition(pluginApplicationContext, mainApplicationContext, bean.getClass().getName());
            mainApplicationContext.getBeanFactory().registerSingleton(bean.getClass().getName(), bean);
            mainApplicationContext.getBeanFactory().autowireBean(bean);
            beanNames.add(bean.getClass().getName());
        });
    }

    @Override
    public void onStop(SpringBootPlugin plugin) {
        beanNames.forEach(beanName -> {
            var mainApplicationContext = plugin.getMainApplicationContext();
            mainApplicationContext.removeBeanDefinition(beanName);
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
