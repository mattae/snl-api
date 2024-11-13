package io.github.mattae.snl.core.api.services;

public interface PluginStorageProvider {
    byte[] getData(String pluginId);

    void saveData(String pluginId, byte[] data);

    void deleteData(String pluginId);
}
