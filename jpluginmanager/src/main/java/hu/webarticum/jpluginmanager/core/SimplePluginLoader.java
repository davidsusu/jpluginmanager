package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimplePluginLoader implements PluginLoader {
    
    private final List<Class<? extends Plugin>> pluginClasses;
    
    @SafeVarargs
    public SimplePluginLoader(Class<? extends Plugin>... pluginClasses) {
        this(Arrays.asList(pluginClasses));
    }

    public SimplePluginLoader(Collection<Class<? extends Plugin>> pluginClasses) {
        this.pluginClasses = new ArrayList<Class<? extends Plugin>>(pluginClasses);
    }
    
    @Override
    public List<PluginContainer> loadAll() {
        List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
        for (Class<? extends Plugin> pluginClass: pluginClasses) {
            try {
                Plugin plugin = pluginClass.newInstance();
                pluginContainers.add(new PluginContainer(plugin));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return pluginContainers;
    }

}
