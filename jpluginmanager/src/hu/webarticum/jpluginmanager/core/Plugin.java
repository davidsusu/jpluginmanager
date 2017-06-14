package hu.webarticum.jpluginmanager.core;

import java.util.List;

public interface Plugin {
    
    public String getName();
    
    public String getVersion();
    
    public String getLabel();
    
    public boolean validate();
    
    public <T> List<T> getExtensions(Class<T> type);
    
}
