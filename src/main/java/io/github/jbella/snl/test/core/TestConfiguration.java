package io.github.jbella.snl.test.core;

import com.blazebit.persistence.integration.jackson.EntityViewIdValueAccessor;
import com.blazebit.persistence.spring.data.webmvc.impl.json.EntityViewAwareMappingJackson2HttpMessageConverter;
import com.blazebit.persistence.spring.data.webmvc.impl.json.EntityViewIdValueHolder;
import com.blazebit.persistence.view.EntityViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.laxture.sbp.mock.MockSpringBootPluginManager;
import org.laxture.sbp.mock.MockSpringExtensionFactory;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.pf4j.PluginRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TestConfiguration {
    private final TransactionManager transactionManager;
    private final EntityViewManager evm;
    private final ApplicationContext applicationContext;
    private final ConversionService conversionService;

    public TestConfiguration(TransactionManager transactionManager, EntityViewManager evm, ApplicationContext applicationContext,
                             @Lazy @Qualifier("mvcConversionService") ConversionService conversionService) {
        this.transactionManager = transactionManager;
        this.evm = evm;
        this.applicationContext = applicationContext;
        this.conversionService = conversionService;
    }


    @Bean
    public TransactionTemplate getTransactionTemplate() {
        return new TransactionTemplate((PlatformTransactionManager) transactionManager);
    }

    @Bean
    public ObjectMapper entityViewAwareObjectMapper() {
        return new EntityViewAwareMappingJackson2HttpMessageConverter(evm,
                blazeWebmvcIdAttributeAccessor()).getObjectMapper();
    }

    @Bean
    public PluginManager getPluginManager() {
        return new MockSpringBootPluginManager(applicationContext) {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new MockSpringExtensionFactory(applicationContext) {
                    @Override
                    public <T> T create(Class<T> extensionClass) {
                        try {
                            return applicationContext.getBean(extensionClass);
                        } catch (Exception e) {
                            throw new PluginRuntimeException(e);
                        }
                    }
                };
            }
        };
    }

    private EntityViewIdValueAccessor blazeWebmvcIdAttributeAccessor() {
        return new EntityViewIdValueHolder(conversionService);
    }
}
