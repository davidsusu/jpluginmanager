package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class CompositePluginLoader implements PluginLoader {
    
    private final List<PluginLoader> pluginLoaders;
    
    public CompositePluginLoader(PluginLoader... pluginLoaders) {
        this(Arrays.asList(pluginLoaders));
    }

    public CompositePluginLoader(Collection<PluginLoader> pluginLoaders) {
        this.pluginLoaders = new ArrayList<PluginLoader>(pluginLoaders);
    }
    
    @Override
    public List<PluginContainer> loadAll() {
        List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
        for (PluginLoader pluginLoader: pluginLoaders) {
            pluginContainers.addAll(pluginLoader.loadAll());
        }
        return pluginContainers;
    }

}
