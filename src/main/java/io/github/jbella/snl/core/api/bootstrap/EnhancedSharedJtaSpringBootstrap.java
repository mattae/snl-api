package io.github.jbella.snl.core.api.bootstrap;

import io.github.jbella.snl.core.api.services.*;
import org.apache.commons.lang3.ArrayUtils;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityMetadataSourceAdvisor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import org.zalando.problem.spring.web.autoconfigure.security.ProblemSecurityBeanPostProcessor;

import static io.github.jbella.snl.core.api.bootstrap.EnhancedSharedDataSourceSpringBootstrap.getGraphqlControllers;

public class EnhancedSharedJtaSpringBootstrap extends SpringBootstrap {
    private final SpringBootPlugin plugin;

    public EnhancedSharedJtaSpringBootstrap(SpringBootPlugin plugin, Class<?>... primarySources) {
        super(plugin, primarySources);
        this.plugin = plugin;
    }

    @Override
    protected String[] getExcludeConfigurations() {
        return ArrayUtils.addAll(super.getExcludeConfigurations(),
                "org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration",
                "com.blazebit.persistence.spring.data.webmvc.impl.BlazePersistenceWebConfiguration",
                "org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration");
    }

    @Override
    public ConfigurableApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext =
                (AnnotationConfigApplicationContext) super.createApplicationContext();
        importBeanFromMainContext(applicationContext, RouterFunctionMapping.class);
        importBeanFromMainContext(applicationContext, HttpSecurity.class);
        importBeanFromMainContext(applicationContext, AuthenticationManagerBuilder.class);
        importBeanFromMainContext(applicationContext, MethodSecurityMetadataSourceAdvisor.class);
        importBeanFromMainContext(applicationContext, SecurityProblemSupport.class);
        importBeanFromMainContext(applicationContext, ProblemSecurityBeanPostProcessor.class);
        importBeanFromMainContext(applicationContext, PluginManager.class);
        importBeanFromMainContext(applicationContext, ConfigurationService.class);
        importBeanFromMainContext(applicationContext, OrganisationService.class);
        importBeanFromMainContext(applicationContext, IndividualService.class);
        importBeanFromMainContext(applicationContext, MailService.class);
        importBeanFromMainContext(applicationContext, TranslationService.class);
        importBeanFromMainContext(applicationContext, ValueSetService.class);
        importBeanFromMainContext(applicationContext, "xaDataSourceWrapper");
        importBeanFromMainContext(applicationContext, "transactionManager");
        getGraphqlControllers(plugin.getMainApplicationContext())
                .forEach(controller -> importBeanFromMainContext(applicationContext, controller.getClass()));

        return applicationContext;
    }
}
