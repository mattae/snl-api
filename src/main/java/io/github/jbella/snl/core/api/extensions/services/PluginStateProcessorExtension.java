package io.github.jbella.snl.core.api.extensions.services;

import io.github.jbella.snl.core.api.config.yml.Permission;
import io.github.jbella.snl.core.api.config.yml.Role;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

import java.util.Set;

public interface PluginStateProcessorExtension extends ExtensionPoint {

    void processStateWith(PluginWrapper wrapper, PluginState state, Set<Role> roles, Set<Permission> permissions);
}
