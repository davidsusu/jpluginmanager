package hu.webarticum.jpluginmanager.core;


public class PluginContainer {
    
    public enum Status {NONE, LOADED, RESOLVED, ACTIVE};
    
    private final ClassLoader classLoader;
    
    private final Plugin plugin;
    
    private Status status = Status.ACTIVE; // TODO
    
    public PluginContainer(ClassLoader classLoader, Plugin plugin) {
        this.classLoader = classLoader;
        this.plugin = plugin;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public boolean isActive() {
        return status == Status.ACTIVE;
    }
    
}
