package hu.webarticum.jpluginmanager.loader.js;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginContainer;
import hu.webarticum.jpluginmanager.core.PluginLoader;

public class DefaultJsPluginLoader implements PluginLoader {

    private final File pluginDirectory;
    
    public DefaultJsPluginLoader(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }
    
    @Override
    public List<PluginContainer> loadAll() {
        List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
        File[] files = pluginDirectory.listFiles(new JsFileFilter());
        if (files != null) {
            for (File file: files) {
                PluginContainer pluginContainer = loadPluginContainer(file);
                if (pluginContainer != null) {
                    pluginContainers.add(pluginContainer);
                }
            }
        }
        return pluginContainers;
    }
    
    private class JsFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".js");
        }
        
    }

    protected PluginContainer loadPluginContainer(File file) {
        String scriptContent;
        
        try {
            scriptContent = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        Map<String, String> metaData = parseMetaData(scriptContent);

        ScriptEngine scriptEngine = getScriptEngine();
        if (scriptEngine == null) {
            return null;
        }
        
        Object scriptObject;
        try {
            scriptObject = scriptEngine.eval(scriptContent);
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }
        
        for (AdapterFactory adapterFactory: getAdapterFactories()) {
            Object adaptedObject = ((Invocable)scriptEngine).getInterface(scriptObject, adapterFactory.getAdaptedInterface());
            if (adaptedObject != null) {
                Plugin plugin = adapterFactory.createAdapter(scriptEngine, adaptedObject, metaData);
                return new PluginContainer(plugin);
            }
        }
        
        return null;
    }
    
    private Map<String, String> parseMetaData(String scriptContent) {
        // TODO
        
        return new HashMap<String, String>();
    }

    private ScriptEngine getScriptEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        return scriptEngineManager.getEngineByMimeType("text/javascript");
    }
    
    private List<AdapterFactory> getAdapterFactories() {
        List<AdapterFactory> adapterFactories = new ArrayList<AdapterFactory>();
        
        adapterFactories.add(new DefaultAdapterFactory());
        
        return adapterFactories;
    }
    
}
