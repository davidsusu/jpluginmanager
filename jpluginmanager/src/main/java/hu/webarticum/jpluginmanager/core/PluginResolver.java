package hu.webarticum.jpluginmanager.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PluginResolver {
    
    public void resolveAll(PluginManager pluginManager) {
        Set<PluginContainer> unresolvedPluginContainers = new HashSet<PluginContainer>(pluginManager.getPluginContainers());
        Set<Plugin> resolvedPlugins = new HashSet<Plugin>();
        
        boolean changed;
        do {
            changed = false;
            Iterator<PluginContainer> unresolvedPluginContainerIterator = unresolvedPluginContainers.iterator();
            while (unresolvedPluginContainerIterator.hasNext()) {
                PluginContainer pluginContainer = unresolvedPluginContainerIterator.next();
                Plugin plugin = pluginContainer.getPlugin();
                if (plugin.getDependency().validate(resolvedPlugins)) {
                    unresolvedPluginContainerIterator.remove();
                    resolvedPlugins.add(plugin);
                    pluginContainer.setResolved(true);
                    changed = true;
                }
            }
        } while(changed);
    }

    public void startAll(PluginManager pluginManager) {
        resolveAll(pluginManager);
        for (PluginContainer pluginContainer: pluginManager.getPluginContainers()) {
            if (pluginContainer.isResolved()) {
                pluginContainer.getPlugin().start();
            }
        }
    }
    
}
