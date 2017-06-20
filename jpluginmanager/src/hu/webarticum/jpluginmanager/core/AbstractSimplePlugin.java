package hu.webarticum.jpluginmanager.core;

import java.util.List;

public abstract class AbstractSimplePlugin implements Plugin {
    
    private boolean active = false;
    
    protected ExtensionBinder extensionBinder = new ExtensionBinder();
    
    @Override
    public Dependency getDependency() {
        // TODO Auto-generated method stub
        return new NoDependency();
    }

    @Override
    public boolean start() {
        active = true;
        return true;
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        return extensionBinder.getExtensions(type);
    }

}
