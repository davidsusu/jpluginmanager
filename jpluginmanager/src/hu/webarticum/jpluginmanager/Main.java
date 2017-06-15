package hu.webarticum.jpluginmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hu.webarticum.jpluginmanager.core.CompositePluginLoader;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.PluginLoader;
import hu.webarticum.jpluginmanager.core.PluginManager;
import hu.webarticum.jpluginmanager.core.SimplePluginLoader;
import hu.webarticum.jpluginmanager.loader.jar.DefaultJarPluginLoader;
import hu.webarticum.jpluginmanager.loader.js.DefaultJsPluginLoader;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.print("Plugin dir (see resources/sample for test plugins): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        PluginLoader pluginLoader = new CompositePluginLoader(
            new SimplePluginLoader(SampleInnerPlugin.class),
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

    public static class SampleInnerPlugin implements Plugin {

        @Override
        public String getName() {
            return "hu.webarticum.jpluginmanager.SampleInnerPlugin";
        }

        @Override
        public String getVersion() {
            return "0.1.1";
        }

        @Override
        public String getLabel() {
            return "Sample Inner Plugin";
        }

        @Override
        public boolean validate() {
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> getExtensions(Class<T> type) {
            List<T> extensions = new ArrayList<T>();
            if (type == HelloExtensionInterface.class) {
                extensions.add((T)new HelloExtensionInterface() {
                    
                    @Override
                    public void hello() {
                        System.out.println("Hello, Inner!");
                    }
                    
                });
            }
            return extensions;
        }
        
    }

}
