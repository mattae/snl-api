package io.github.jbella.snl.core.api.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jbella.snl.core.api.services.*;
import io.github.jbella.snl.core.api.services.errors.ExceptionTranslator;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.ArrayUtils;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import javax.sql.DataSource;

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
        importBeanFromMainContext(applicationContext, PluginManager.class);
        importBeanFromMainContext(applicationContext, AuthenticationManagerBuilder.class);
        importBeanFromMainContext(applicationContext, MethodInterceptor.class);
        importBeanFromMainContext(applicationContext, ConfigurationService.class);
        importBeanFromMainContext(applicationContext, MailService.class);
        importBeanFromMainContext(applicationContext, IndividualService.class);
        importBeanFromMainContext(applicationContext, OrganisationService.class);
        importBeanFromMainContext(applicationContext, TranslationService.class);
        importBeanFromMainContext(applicationContext, ValueSetService.class);
        importBeanFromMainContext(applicationContext, ObjectMapper.class);
        importBeanFromMainContext(applicationContext, "xaDataSourceWrapper");
        importBeanFromMainContext(applicationContext, "transactionManager");
        importBeanFromMainContext(applicationContext, ExceptionTranslator.class);
        getGraphqlControllers(plugin.getMainApplicationContext())
                .forEach(controller -> importBeanFromMainContext(applicationContext, controller.getClass()));

        return applicationContext;
    }
}
