package io.github.jbella.snl.core.api.extensions.security;

import io.github.jbella.snl.core.api.config.yml.Permission;
import io.github.jbella.snl.core.api.config.yml.Role;
import io.github.jbella.snl.core.api.domain.Plugin;
import io.github.jbella.snl.core.api.extensions.OrderedExtension;

import java.util.Set;

public interface RolePermissionProcessorExtension extends OrderedExtension {
    void process(Plugin plugin, Set<Role> roles, Set<Permission> permissions);

    void delete(Plugin plugin);
}
