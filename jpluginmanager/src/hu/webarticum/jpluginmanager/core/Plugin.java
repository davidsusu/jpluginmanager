package hu.webarticum.jpluginmanager.core;

import java.util.List;

public interface Plugin {
    
    public String getName();
    
    public Version getVersion();
    
    public Dependency getDependency();
    
    public String getLabel();
    
    public boolean start();
    
    public void stop();
    
    public boolean isActive();
    
    public <T> List<T> getExtensions(Class<T> type);
    
}
