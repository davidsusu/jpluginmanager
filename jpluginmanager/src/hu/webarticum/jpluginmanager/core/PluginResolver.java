package hu.webarticum.jpluginmanager.core;


public class PluginResolver {
    
    public void resolveAll(PluginManager pluginManager) {
        
        // TODO
        
        for (PluginContainer pluginContainer: pluginManager.getPluginContainers()) {
            pluginContainer.setResolved(true);
        }
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
