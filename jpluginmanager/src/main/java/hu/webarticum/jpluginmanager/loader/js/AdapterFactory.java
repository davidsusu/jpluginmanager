package hu.webarticum.jpluginmanager.loader.js;

import java.util.Map;

import javax.script.ScriptEngine;

import hu.webarticum.jpluginmanager.core.Plugin;

public interface AdapterFactory {
    
    public Plugin createAdapter(ScriptEngine scriptEngine, Object adaptedObject, Map<String, String> metaData);
    
    public Class<?> getAdaptedInterface();
    
}
