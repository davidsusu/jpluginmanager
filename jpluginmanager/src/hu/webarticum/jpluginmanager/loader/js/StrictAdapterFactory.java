package hu.webarticum.jpluginmanager.loader.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import hu.webarticum.jpluginmanager.core.CompositeDependency;
import hu.webarticum.jpluginmanager.core.Dependency;
import hu.webarticum.jpluginmanager.core.NoDependency;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginVersionDependency;
import hu.webarticum.jpluginmanager.core.UnresolvableDependency;
import hu.webarticum.jpluginmanager.core.Version;

public class StrictAdapterFactory implements AdapterFactory {
    
    @Override
    public Plugin createAdapter(ScriptEngine scriptEngine, Object adaptedObject, Map<String, String> metaData) {
        if (!(adaptedObject instanceof StrictAdaptedInterface)) {
            throw new IllegalArgumentException("Wrong adapted object");
        }
        
        StrictAdaptedInterface adapted = (StrictAdaptedInterface)adaptedObject;
        return new StrictAdapter(scriptEngine, adapted, metaData);
    }
    
    @Override
    public Class<?> getAdaptedInterface() {
        return StrictAdaptedInterface.class;
    }
    
    public class StrictAdapter implements Plugin {

        private final ScriptEngine scriptEngine;
        private final StrictAdaptedInterface adapted;
        
        public StrictAdapter(ScriptEngine scriptEngine, StrictAdaptedInterface adapted, Map<String, String> metaData) {
            this.scriptEngine = scriptEngine;
            this.adapted = adapted;
        }
        
        @Override
        public String getName() {
            return adapted.getName();
        }
        
        @Override
        public Version getVersion() {
            return new Version(adapted.getVersion());
        }
        
        @Override
        public Dependency getDependency() {
            Object dependencyObject = adapted.getDependency();
            if (dependencyObject == null) {
                return new NoDependency();
            } else if (dependencyObject instanceof String) {
                String dependencyString = (String)dependencyObject;
                int atIndex = dependencyString.indexOf('@');
                String pluginName;
                String versionMatcher;
                if (atIndex > 0) {
                    pluginName = dependencyString.substring(0, atIndex);
                    versionMatcher = dependencyString.substring(atIndex + 1);
                } else {
                    pluginName = dependencyString;
                    versionMatcher = "*";
                }
                return new PluginVersionDependency(pluginName, versionMatcher);
            } else if (dependencyObject instanceof Map) {
                List<Dependency> subDependencies = new ArrayList<Dependency>();
                for (Map.Entry<?, ?> entry: ((Map<?, ?>)dependencyObject).entrySet()) {
                    String pluginName = entry.getKey().toString();
                    String versionMatcher = entry.getValue().toString();
                    subDependencies.add(new PluginVersionDependency(pluginName, versionMatcher));
                }
                return new CompositeDependency(subDependencies);
            } else {
                return new UnresolvableDependency();
            }
        }
        
        @Override
        public String getLabel() {
            return adapted.getLabel();
        }
        
        @Override
        public boolean start() {
            return adapted.start();
        }
        
        @Override
        public void stop() {
            adapted.stop();
        }
        
        @Override
        public boolean isActive() {
            return adapted.isActive();
        }
        
        @Override
        public <T> List<T> getExtensions(Class<T> type) {
            List<T> extensions = new ArrayList<T>();
            List<Object> rawExtensions = adapted.getExtensions(type);
            for (Object rawExtension: rawExtensions) {
                T extension = ((Invocable)scriptEngine).getInterface(rawExtension, type);
                if (extension != null) {
                    extensions.add(extension);
                }
            }
            return extensions;
        }
        
    }

    public interface StrictAdaptedInterface {
        
        public String getName();
        
        public String getVersion();
        
        public Object getDependency();
        
        public String getLabel();
        
        public boolean start();
        
        public void stop();
        
        public boolean isActive();
        
        public List<Object> getExtensions(Class<?> type);
        
    }

}
