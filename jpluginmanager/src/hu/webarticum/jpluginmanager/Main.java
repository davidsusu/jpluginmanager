package hu.webarticum.jpluginmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.webarticum.jpluginmanager.core.CompositePluginLoader;
import hu.webarticum.jpluginmanager.core.PluginLoader;
import hu.webarticum.jpluginmanager.core.PluginManager;
import hu.webarticum.jpluginmanager.loader.jar.DefaultJarPluginLoader;
import hu.webarticum.jpluginmanager.loader.js.DefaultJsPluginLoader;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.print("Plugin dir (see resources/sample for test plugins): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        PluginLoader pluginLoader = new CompositePluginLoader(
            new DefaultJarPluginLoader(new File(path)),
            new DefaultJsPluginLoader(new File(path))
        );
        PluginManager pluginManager = new PluginManager(pluginLoader);
        for (HelloExtensionInterface helloExtension: pluginManager.getExtensions(HelloExtensionInterface.class)) {
            helloExtension.hello();
        }
    }
    
    public static interface HelloExtensionInterface {
        
        public void hello();
        
    }
    
}
