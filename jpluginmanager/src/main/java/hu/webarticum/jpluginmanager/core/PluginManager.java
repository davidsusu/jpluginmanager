package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.List;

public class PluginManager implements ExtensionSupplier {
    
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
            loaded = true;
        }
    }
    
    public List<PluginContainer> getPluginContainers() {
        load();
        return new ArrayList<PluginContainer>(pluginContainers);
    }

    public List<PluginContainer> getActivePluginContainers() {
        load();
        List<PluginContainer> filteredPluginContainers = new ArrayList<PluginContainer>();
        for (PluginContainer pluginContainer: pluginContainers) {
            if (pluginContainer.getPlugin().isActive()) {
                filteredPluginContainers.add(pluginContainer);
            }
        }
        return filteredPluginContainers;
    }
    
    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        List<T> extensions = new ArrayList<T>();
        for (PluginContainer pluginContainer: getActivePluginContainers()) {
            extensions.addAll(pluginContainer.getPlugin().getExtensions(type));
        }
        return extensions;
    }
    
}
