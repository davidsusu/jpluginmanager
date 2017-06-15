package hu.webarticum.jpluginmanager.loader.js;

import java.io.File;
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

import hu.webarticum.jpluginmanager.core.AbstractDirectoryPluginLoader;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginContainer;

public class DefaultJsPluginLoader extends AbstractDirectoryPluginLoader {

    public DefaultJsPluginLoader(File pluginDirectory) {
        super(pluginDirectory, "js");
    }

    @Override
    protected PluginContainer loadPluginContainer(File file, String pluginName) {
        ClassLoader classLoader = getClass().getClassLoader(); // XXX
        PluginInvocationHandler pluginInvocationHandler = new PluginInvocationHandler(file, pluginName);
        if (!pluginInvocationHandler.load()) {
            return null;
        }
        Plugin plugin = (Plugin)Proxy.newProxyInstance(classLoader, new Class[] {Plugin.class}, pluginInvocationHandler);
        return new PluginContainer(classLoader, plugin);
    }
    
    private class PluginInvocationHandler implements InvocationHandler {
        
        final File file;
        
        final String pluginName;
        
        ScriptEngine scriptEngine;
        
        Plugin scriptPluginObject;
        
        PluginInvocationHandler(File file, String pluginName) {
            this.file = file;
            this.pluginName = pluginName;
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