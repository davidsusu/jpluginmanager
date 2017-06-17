package hu.webarticum.jpluginmanager.loader.js;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginContainer;
import hu.webarticum.jpluginmanager.core.PluginLoader;

public class DefaultJsPluginLoader implements PluginLoader {

    private final File pluginDirectory;
    
    private final List<AdapterFactory> adapterFactories;

    public DefaultJsPluginLoader(File pluginDirectory) {
        this(pluginDirectory, true, null);
    }

    public DefaultJsPluginLoader(File pluginDirectory, Collection<AdapterFactory> adapterFactories) {
        this(pluginDirectory, false, adapterFactories);
    }

    public DefaultJsPluginLoader(
        File pluginDirectory,
        boolean addBundledAdapterFactories,
        Collection<AdapterFactory> additionalAdapterFactories
    ) {
        this.pluginDirectory = pluginDirectory;
        this.adapterFactories = new ArrayList<AdapterFactory>();
        if (addBundledAdapterFactories) {
            this.adapterFactories.add(new StrictAdapterFactory());
            this.adapterFactories.add(new SimplifiedAdapterFactory());
        }
        if (additionalAdapterFactories != null) {
            for (AdapterFactory adapterFactory: additionalAdapterFactories) {
                this.adapterFactories.add(adapterFactory);
            }
        }
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
        
        Map<String, String> metaData = parseMetaData(file.getName(), scriptContent);

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
        
        for (AdapterFactory adapterFactory: adapterFactories) {
            Object adaptedObject;
            try {
                adaptedObject = ((Invocable)scriptEngine).getInterface(scriptObject, adapterFactory.getAdaptedInterface());
            } catch (Throwable e) {
                e.printStackTrace();
                continue;
            }
            if (adaptedObject != null) {
                Plugin plugin = adapterFactory.createAdapter(scriptEngine, adaptedObject, metaData);
                return new PluginContainer(plugin);
            }
        }
        
        return null;
    }
    
    private Map<String, String> parseMetaData(String filename, String scriptContent) {
        Map<String, String> metaData = createFallbackMetadata(filename);
        String trimmedScriptContent = scriptContent.trim();
        if (trimmedScriptContent.startsWith("/*")) {
            int firstCommentEndPos = trimmedScriptContent.indexOf("*/");
            if (firstCommentEndPos >= 2) {
                String firstComment = trimmedScriptContent.substring(0, firstCommentEndPos + 2);
                Pattern annotationPattern = Pattern.compile("(?m)(?:^[ \\t]+|/)\\*+[ \\t]*@(\\w+)[ \\t:][ \\t]*(.+)");
                Matcher annotationMatcher = annotationPattern.matcher(firstComment);
                while (annotationMatcher.find()) {
                    String metaKey = annotationMatcher.group(1);
                    String metaValue = annotationMatcher.group(2);
                    metaData.put(metaKey, metaValue);
                }
            }
        }
        return metaData;
    }
    
    private Map<String, String> createFallbackMetadata(String filename) {
        Map<String, String> fallbackMetaData = new HashMap<String, String>();
        int atPos = filename.indexOf('@');
        if (atPos >= 0) {
            String pluginName = filename.substring(0, atPos);
            fallbackMetaData.put("PluginName", pluginName);
            fallbackMetaData.put("PluginLabel", pluginName);
            fallbackMetaData.put("PluginVersion", filename.substring(atPos + 1));
        } else {
            fallbackMetaData.put("PluginName", filename);
            fallbackMetaData.put("PluginLabel", filename);
            fallbackMetaData.put("PluginVersion", "0.0.0");
        }
        return fallbackMetaData;
    }

    private ScriptEngine getScriptEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        return scriptEngineManager.getEngineByMimeType("text/javascript");
    }
    
}
