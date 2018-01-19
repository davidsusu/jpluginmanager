package hu.webarticum.jpluginmanager.core;

import java.util.Collection;

public class NoDependency implements Dependency {
    
    @Override
    public boolean validate(Collection<Plugin> resolvedPlugins) {
        return true;
    }
    
}
