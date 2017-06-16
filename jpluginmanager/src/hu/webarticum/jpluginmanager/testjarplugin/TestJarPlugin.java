package hu.webarticum.jpluginmanager.testjarplugin;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.jpluginmanager.Main.HelloExtensionInterface;
import hu.webarticum.jpluginmanager.core.Dependency;
import hu.webarticum.jpluginmanager.core.NoDependency;
import hu.webarticum.jpluginmanager.core.Plugin;
import hu.webarticum.jpluginmanager.core.Version;


public class TestJarPlugin implements Plugin {
    
    private boolean active = false;
    
    @Override
    public String getName() {
        return "test-jar-plugin";
    }

    @Override
    public Version getVersion() {
        return new Version("0.1.0");
    }

    @Override
    public Dependency getDependency() {
        return new NoDependency();
    }

    @Override
    public String getLabel() {
        return "Test Jar Plugin";
    }

    @Override
    public boolean start() {
        active = true;
        return false;
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        List<T> extensions = new ArrayList<T>();
        if (type == HelloExtensionInterface.class) {
            extensions.add((T)new TestHelloExtension());
        }
        return extensions;
    }
    
    private class TestHelloExtension implements HelloExtensionInterface {

        @Override
        public void hello() {
            System.out.println("Hello, JAR!");
        }
        
    }

}
