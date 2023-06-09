package io.github.jbella.snl.test.core;

import io.github.jbella.snl.core.api.extensions.OrderedExtension;
import io.github.jbella.snl.core.api.services.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.pf4j.PluginManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtensionServiceImpl implements ExtensionService {
    private final PluginManager pluginManager;

    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        return pluginManager.getExtensions(type);
    }

    @Override
    public <T> List<T> getExtensions(Class<T> type, String pluginId) {
        return pluginManager.getExtensions(type, pluginId);
    }

    @Override
    public <T extends OrderedExtension> T getPriorityExtension(Class<T> type) {
        return pluginManager.getExtensions(type)
            .stream()
            .min((e1, e2) -> Integer.compare(e2.getOrder(), e1.getOrder()))
            .orElse(null);
    }

    @Override
    public <T extends OrderedExtension> T getPriorityExtension(Class<T> type, String pluginId) {
        return pluginManager.getExtensions(type, pluginId)
            .stream()
            .min((e1, e2) -> Integer.compare(e2.getOrder(), e1.getOrder()))
            .orElse(null);
    }
}