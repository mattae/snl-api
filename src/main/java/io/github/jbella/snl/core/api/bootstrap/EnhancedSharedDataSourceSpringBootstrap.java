package io.github.jbella.snl.core.api.bootstrap;

import io.github.jbella.snl.core.api.services.*;
import org.apache.commons.lang3.ArrayUtils;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SharedDataSourceSpringBootstrap;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityMetadataSourceAdvisor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import org.zalando.problem.spring.web.autoconfigure.security.ProblemSecurityBeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnhancedSharedDataSourceSpringBootstrap extends SharedDataSourceSpringBootstrap {
    private final SpringBootPlugin plugin;

    public EnhancedSharedDataSourceSpringBootstrap(SpringBootPlugin plugin, Class<?>... primarySources) {
        super(plugin, primarySources);
        this.plugin = plugin;
    }

    @Override
    protected String[] getExcludeConfigurations() {
        return ArrayUtils.addAll(super.getExcludeConfigurations(),
                "com.blazebit.persistence.spring.data.webmvc.impl.BlazePersistenceWebConfiguration",
                "org.springframework.boot.autoconfigure.netty.NettyAutoConfiguration");
    }

    @Override
    public ConfigurableApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext =
                (AnnotationConfigApplicationContext) super.createApplicationContext();
        importBeanFromMainContext(applicationContext, RouterFunctionMapping.class);
        importBeanFromMainContext(applicationContext, HttpSecurity.class);
        importBeanFromMainContext(applicationContext, JpaTransactionManager.class);
        importBeanFromMainContext(applicationContext, PluginManager.class);
        importBeanFromMainContext(applicationContext, AuthenticationManagerBuilder.class);
        importBeanFromMainContext(applicationContext, MethodSecurityMetadataSourceAdvisor.class);
        importBeanFromMainContext(applicationContext, SecurityProblemSupport.class);
        importBeanFromMainContext(applicationContext, ProblemSecurityBeanPostProcessor.class);
        importBeanFromMainContext(applicationContext, "problemSecurityAdvice");
        importBeanFromMainContext(applicationContext, ConfigurationService.class);
        importBeanFromMainContext(applicationContext, MailService.class);
        importBeanFromMainContext(applicationContext, IndividualService.class);
        importBeanFromMainContext(applicationContext, OrganisationService.class);
        importBeanFromMainContext(applicationContext, PluginService.class);
        importBeanFromMainContext(applicationContext, TranslationService.class);
        importBeanFromMainContext(applicationContext, ValueSetService.class);
        getGraphqlControllers(plugin.getMainApplicationContext())
                .forEach(controller -> importBeanFromMainContext(applicationContext, controller.getClass()));

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
}
