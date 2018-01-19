package hu.webarticum.jpluginmanager.core;

import java.util.Collection;

public class PluginVersionDependency implements Dependency {
    
    private final String pluginName;
    private final String versionMatcher;
    
    public PluginVersionDependency(String pluginName, String versionMatcher) {
        this.pluginName = pluginName;
        this.versionMatcher = versionMatcher;
    }
    
    @Override
    public boolean validate(Collection<Plugin> resolvedPlugins) {
        for (Plugin plugin: resolvedPlugins) {
            if (plugin.getName().equals(pluginName)) {
                if (plugin.getVersion().matches(versionMatcher)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
