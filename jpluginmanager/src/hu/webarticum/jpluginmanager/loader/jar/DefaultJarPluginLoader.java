package hu.webarticum.jpluginmanager.loader.jar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import hu.webarticum.jpluginmanager.core.AbstractDirectoryPluginLoader;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginContainer;


public class DefaultJarPluginLoader extends AbstractDirectoryPluginLoader {
    
    public DefaultJarPluginLoader(File pluginDirectory) {
        super(pluginDirectory, "jar");
    }
    
    @Override
    protected PluginContainer loadPluginContainer(File file, String pluginClassName) {
        PluginContainer pluginContainer = null;
        try {
            ClassLoader pluginClassLoader = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
            @SuppressWarnings("unchecked")
            Class<Plugin> pluginClass = (Class<Plugin>)Class.forName(pluginClassName, true, pluginClassLoader);
            Plugin plugin = pluginClass.newInstance();
            pluginContainer = new PluginContainer(pluginClassLoader, plugin);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return pluginContainer;
    }
    
}
