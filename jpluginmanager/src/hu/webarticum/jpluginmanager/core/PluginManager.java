package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.jpluginmanager.core.PluginContainer.Status;

public class PluginManager {
    
    private final PluginLoader pluginLoader;
    
    private boolean loaded = false;
    
    private final List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
    
    public PluginManager(PluginLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
    }
    
    public void load() {
        load(false);
    }

    public void load(boolean force) {
        if (!loaded || force) {
            pluginContainers.clear();
            pluginContainers.addAll(pluginLoader.loadAll());
        }
    }
    
    public List<PluginContainer> getPluginContainers() {
        load();
        return new ArrayList<PluginContainer>(pluginContainers);
    }

    public List<PluginContainer> getPluginContainers(PluginContainer.Status status) {
        load();
        List<PluginContainer> filteredPluginContainers = new ArrayList<PluginContainer>();
        for (PluginContainer pluginContainer: pluginContainers) {
            if (pluginContainer.getStatus() == status) {
                filteredPluginContainers.add(pluginContainer);
            }
        }
        return filteredPluginContainers;
    }

    public List<PluginContainer> getActivePluginContainers() {
        return getPluginContainers(Status.ACTIVE);
    }
    
    public <T> List<T> getExtensions(Class<T> type) {
        List<T> extensions = new ArrayList<T>();
        for (PluginContainer pluginContainer: getActivePluginContainers()) {
            extensions.addAll(pluginContainer.getPlugin().getExtensions(type));
        }
        return extensions;
    }
    
}
