package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.Plugin;

import java.util.Optional;

public interface PluginService {
    Optional<Plugin> findByName(String name);
}
