package hu.webarticum.jpluginmanager.core;

import java.util.List;

public interface ExtensionSupplier {
    
    public <T> List<T> getExtensions(Class<T> type);
    
}
