package hu.webarticum.jpluginmanager.loader.js;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import hu.webarticum.jpluginmanager.core.AbstractDirectoryPluginLoader;
import hu.webarticum.jpluginmanager.core.PluginContainer;

public class DefaultJsPluginLoader extends AbstractDirectoryPluginLoader {

    public DefaultJsPluginLoader(File pluginDirectory) {
        super(pluginDirectory, "js");
    }

    @Override
    protected PluginContainer loadPluginContainer(File file, String pluginClassName) {
        
        // TODO
        
        try {
            System.out.println(
                "\n--------------------\n" +
                pluginClassName + ":\n" +
                new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset()) +
                "\n--------------------\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
