package hu.webarticum.jpluginmanager.core;


public class PluginContainer {
    
    private final Plugin plugin;
    
    private boolean resolved = false;
    
    public PluginContainer(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }

    public boolean isResolved() {
        return resolved;
    }
    
    public void setResolved(boolean newValue) {
        if (newValue == resolved) {
            return;
        }
        
        if (!newValue && plugin.isActive()) {
            plugin.stop();
        }
        
        resolved = newValue;
    }
    
}
