package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CompositeDependency implements Dependency {
    
    final List<Dependency> dependencies;
    
    public CompositeDependency(Dependency... dependencies) {
        this(Arrays.asList(dependencies));
    }
    
    public CompositeDependency(Collection<Dependency> dependencies) {
        this.dependencies = new ArrayList<Dependency>(dependencies);
    }
    
    @Override
    public boolean validate(Collection<Plugin> resolvedPlugins) {
        for (Dependency dependency: dependencies) {
            if (!dependency.validate(resolvedPlugins)) {
                return false;
            }
        }
        return true;
    }
    
}
