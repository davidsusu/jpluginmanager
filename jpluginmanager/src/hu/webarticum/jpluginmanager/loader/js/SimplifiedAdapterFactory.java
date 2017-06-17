package hu.webarticum.jpluginmanager.loader.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import hu.webarticum.jpluginmanager.core.Dependency;
import hu.webarticum.jpluginmanager.core.NoDependency;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.Version;

public class SimplifiedAdapterFactory implements AdapterFactory {
    
    @Override
    public Plugin createAdapter(ScriptEngine scriptEngine, Object adaptedObject, Map<String, String> metaData) {
        if (!(adaptedObject instanceof SimplifiedAdaptedInterface)) {
            throw new IllegalArgumentException("Wrong adapted object");
        }
        
        SimplifiedAdaptedInterface adapted = (SimplifiedAdaptedInterface)adaptedObject;
        return new SimplifiedAdapter(scriptEngine, adapted, metaData);
    }
    
    @Override
    public Class<?> getAdaptedInterface() {
        return SimplifiedAdaptedInterface.class;
    }
    
    public class SimplifiedAdapter implements Plugin {
        
        private final ScriptEngine scriptEngine;
        private final SimplifiedAdaptedInterface adapted;
        private final Map<String, String> metaData;
        
        private boolean active = false;
        
        public SimplifiedAdapter(ScriptEngine scriptEngine, SimplifiedAdaptedInterface adapted, Map<String, String> metaData) {
            this.scriptEngine = scriptEngine;
            this.adapted = adapted;
            this.metaData = new HashMap<String, String>(metaData);
        }

        @Override
        public String getName() {
            return metaData.get("PluginName");
        }

        @Override
        public Version getVersion() {
            return new Version(metaData.get("PluginVersion"));
        }

        @Override
        public Dependency getDependency() {
            return new NoDependency();
        }

        @Override
        public String getLabel() {
            return metaData.get("PluginLabel");
        }

        @Override
        public boolean start() {
            this.active = true;
            return true;
        }

        @Override
        public void stop() {
            active = false;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public <T> List<T> getExtensions(Class<T> type) {
            List<T> extensions = new ArrayList<T>();
            
            String explicitExtensionInterfaceName = metaData.get("ExtensionInterface");
            if (explicitExtensionInterfaceName != null && !explicitExtensionInterfaceName.isEmpty()) {
                if (!type.getName().equals(explicitExtensionInterfaceName)) {
                    return extensions;
                }
            }
            
            List<Object> rawExtensions = adapted.getExtensions();
            for (Object rawExtension: rawExtensions) {
                T extension = null;
                try {
                    extension = ((Invocable)scriptEngine).getInterface(rawExtension, type);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (extension != null) {
                    extensions.add(extension);
                }
            }
            
            return extensions;
        }
        
    }
    
    public interface SimplifiedAdaptedInterface {

        public List<Object> getExtensions();
        
    }
    
}
