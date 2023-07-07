package io.github.jbella.snl.core.api.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jbella.snl.core.api.services.*;
import io.github.jbella.snl.core.api.services.errors.ExceptionTranslator;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.aopalliance.intercept.MethodInterceptor;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginManager;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springdoc.webmvc.ui.SwaggerConfigResource;
import org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EnhancedSpringBootstrap extends SpringBootstrap {
    private final SpringBootPlugin plugin;

    public EnhancedSpringBootstrap(SpringBootPlugin plugin, Class<?>... primarySources) {
        super(plugin, primarySources);
        this.plugin = plugin;
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
        importBeanFromMainContext(applicationContext, PersonService.class);
        importBeanFromMainContext(applicationContext, OrganisationService.class);
        importBeanFromMainContext(applicationContext, TranslationService.class);
        importBeanFromMainContext(applicationContext, ValueSetService.class);
        importBeanFromMainContext(applicationContext, DataSource.class);
        importBeanFromMainContext(applicationContext, ObjectMapper.class);
        importBeanFromMainContext(applicationContext, ExceptionTranslator.class);
        importBeanFromMainContext(applicationContext, OpenApiWebMvcResource.class);
        importBeanFromMainContext(applicationContext, SwaggerWelcomeWebMvc.class);
        importBeanFromMainContext(applicationContext, SwaggerConfigResource.class);
        importBeanFromMainContext(applicationContext, ExtensionService.class);
        importBeanFromMainContext(applicationContext, PreferenceService.class);
        importBeanFromMainContext(applicationContext, ConversionService.class);
        importBeanFromMainContext(applicationContext, TransactionManager.class);
        importBeanFromMainContext(applicationContext, TransactionHandler.class);
        getGraphqlControllers(plugin.getMainApplicationContext())
                .forEach(controller -> importBeanFromMainContext(applicationContext, controller.getClass()));

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(LiquibaseConfiguration.class);
        applicationContext.registerBeanDefinition("liquibaseConfiguration", beanDefinition);

        return applicationContext;
    }

    protected static Set<Object> getGraphqlControllers(ApplicationContext applicationContext) {
        Set<Object> beans = new LinkedHashSet<>();
        beans.addAll(applicationContext.getBeansWithAnnotation(Controller.class)
                .values().stream().toList());
        beans.addAll(applicationContext.getBeansWithAnnotation(RestController.class)
                .values().stream().toList());
        beans = beans.stream().filter(bean -> {
            Set<Method> sets = MethodIntrospector.selectMethods(ClassUtils.getUserClass(bean),
                    (ReflectionUtils.MethodFilter) method -> {
                        Set<Annotation> annotations = AnnotatedElementUtils.findAllMergedAnnotations(
                                method, new LinkedHashSet<>(Arrays.asList(BatchMapping.class, SchemaMapping.class)));
                        return !annotations.isEmpty();
                    });
            return !sets.isEmpty();
        }).collect(Collectors.toSet());
        return beans;
    }

    @ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
    static class LiquibaseConfiguration implements InitializingBean {
        private final LiquibaseProperties properties;
        private final DataSource dataSource;
        private final ApplicationContext applicationContext;

        LiquibaseConfiguration(Optional<LiquibaseProperties> properties, DataSource dataSource, ApplicationContext applicationContext) {
            this.properties = properties.orElse(null);
            this.dataSource = dataSource;
            this.applicationContext = applicationContext;
        }

        public void initializeLiquibase() throws LiquibaseException {
            if (properties != null) {
                SpringLiquibase liquibase = new SpringLiquibase();
                liquibase.setDataSource(dataSource);
                liquibase.setChangeLog(properties.getChangeLog());
                liquibase.setClearCheckSums(properties.isClearChecksums());
                liquibase.setContexts(properties.getContexts());
                liquibase.setDefaultSchema(properties.getDefaultSchema());
                liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
                liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
                liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
                liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
                liquibase.setDropFirst(properties.isDropFirst());
                liquibase.setShouldRun(properties.isEnabled());
                liquibase.setLabelFilter(properties.getLabelFilter());
                liquibase.setChangeLogParameters(properties.getParameters());
                liquibase.setRollbackFile(properties.getRollbackFile());
                liquibase.setTestRollbackOnUpdate(properties.isTestRollbackOnUpdate());
                liquibase.setTag(properties.getTag());
                liquibase.setResourceLoader(applicationContext);

                liquibase.afterPropertiesSet();
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            initializeLiquibase();
        }
    }
}
