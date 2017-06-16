package hu.webarticum.jpluginmanager.core;

import java.util.Collection;


public class UnresolvableDependency implements Dependency {

    @Override
    public boolean validate(Collection<Plugin> resolvedPlugins) {
        return false;
    }

}
