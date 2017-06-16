package hu.webarticum.jpluginmanager.loader.js;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
        ClassLoader classLoader = getClass().getClassLoader(); // XXX
        PluginInvocationHandler pluginInvocationHandler = new PluginInvocationHandler(file);
        if (!pluginInvocationHandler.load()) {
            return null;
        }
        Plugin plugin = (Plugin)Proxy.newProxyInstance(classLoader, new Class[] {Plugin.class}, pluginInvocationHandler);
        return new PluginContainer(plugin);
    }
    
    private class PluginInvocationHandler implements InvocationHandler {
        
        final File file;
        
        ScriptEngine scriptEngine;
        
        Plugin scriptPluginObject;
        
        PluginInvocationHandler(File file) {
            this.file = file;
        }
        
        boolean load() {
            String scriptContent;
            
            try {
                scriptContent = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
            ScriptEngine scriptEngine = getScriptEngine();
            if (scriptEngine == null) {
                return false;
            }
            
            try {
                Object scriptObject = scriptEngine.eval(scriptContent);
                scriptPluginObject = ((Invocable)scriptEngine).getInterface(scriptObject, Plugin.class);
            } catch (ScriptException e) {
                e.printStackTrace();
                return false;
            }
            
            this.scriptEngine = scriptEngine;
            
            return true;
        }
        
        private ScriptEngine getScriptEngine() {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            return scriptEngineManager.getEngineByMimeType("text/javascript");
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (scriptPluginObject == null) {
                return null;
            }
            
            Object result = method.invoke(scriptPluginObject, args);
            
            if (method.getName().equals("getExtensions")) {
                Class<?> extensionClass = (Class<?>)args[0];
                List<?> extensions = (List<?>)result;
                List<Object> resultExtensions = new ArrayList<Object>();
                for (Object extension: extensions) {
                    resultExtensions.add(
                        ((Invocable)scriptEngine).getInterface(extension, extensionClass)
                    );
                }
                result = resultExtensions;
            }
            
            return result;
        }
        
    }
    
}
