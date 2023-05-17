package io.github.jbella.snl.core.api.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jbella.snl.core.api.services.*;
import io.github.jbella.snl.core.api.services.errors.ExceptionTranslator;
import org.aopalliance.intercept.MethodInterceptor;
import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginManager;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springdoc.webmvc.ui.SwaggerConfigResource;
import org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
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
