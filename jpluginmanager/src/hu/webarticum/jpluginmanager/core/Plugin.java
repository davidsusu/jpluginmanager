package hu.webarticum.jpluginmanager.core;


public interface Plugin extends ExtensionSupplier {
    
    public String getName();
    
    public Version getVersion();
    
    public Dependency getDependency();
    
    public String getLabel();
    
    public boolean start();
    
    public void stop();
    
    public boolean isActive();
    
}
