package hu.webarticum.jpluginmanager.core;

import java.util.Collection;

public interface Dependency {
    
    public boolean validate(Collection<Plugin> resolvedPlugins);
    
}
