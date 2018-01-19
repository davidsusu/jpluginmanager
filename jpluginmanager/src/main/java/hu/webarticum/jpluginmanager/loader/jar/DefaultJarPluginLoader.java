package hu.webarticum.jpluginmanager.loader.jar;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginContainer;
import hu.webarticum.jpluginmanager.core.PluginLoader;


public class DefaultJarPluginLoader implements PluginLoader {
    
    private final File pluginDirectory;
    
    public DefaultJarPluginLoader(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PluginContainer> loadAll() {
        List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
        File[] files = pluginDirectory.listFiles(new JarFileFilter());
        if (files != null) {
            for (File file: files) {
                String pluginClassName = null;
                try (JarFile jarFile = new JarFile(file)) {
                    pluginClassName = jarFile.getManifest().getMainAttributes().getValue("Plugin-Class");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (pluginClassName == null) {
                    continue;
                }
                
                ClassLoader pluginClassLoader;
                try {
                    pluginClassLoader = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    continue;
                }
                
                Class<Plugin> pluginClass;
                try {
                    pluginClass = (Class<Plugin>)Class.forName(pluginClassName, true, pluginClassLoader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
                
                Plugin plugin;
                try {
                    plugin = pluginClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                
                PluginContainer pluginContainer = new PluginContainer(plugin);
                
                pluginContainers.add(pluginContainer);
            }
        }
        return pluginContainers;
    }
    
    private class JarFileFilter implements FilenameFilter {
    
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jar");
        }
        
    }

}

