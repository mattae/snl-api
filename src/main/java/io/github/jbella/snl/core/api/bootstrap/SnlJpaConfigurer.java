/*
 * Copyright (C) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jbella.snl.core.api.bootstrap;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.PluginEntityManagerFactoryBeanRegister;
import org.laxture.sbp.spring.boot.PluginPersistenceManagedTypes;
import org.laxture.sbp.spring.boot.SbpJpaConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class SnlJpaConfigurer extends SbpJpaConfigurer {

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
