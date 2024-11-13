package io.github.mattae.snl.core.api.services;

import io.github.mattae.snl.core.api.extensions.OrderedExtension;

import java.util.List;

public interface ExtensionService {
    <T> List<T> getExtensions(Class<T> type);

    <T> List<T> getExtensions(Class<T> type, String pluginId);

    <T extends OrderedExtension> T getPriorityExtension(Class<T> type);

    <T extends OrderedExtension> T getPriorityExtension(Class<T> type, String pluginId);
}
