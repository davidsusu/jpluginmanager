package hu.webarticum.jpluginmanager.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtensionBinder implements ExtensionSupplier {

    private final Map<Object, Set<Object>> connectionMap = new HashMap<Object, Set<Object>>();
    
    @Override
    public <T> List<T> getExtensions(Class<T> type) {
        List<T> extensions = new ArrayList<T>();
        if (connectionMap.containsKey(type)) {
            for (Object classObject: connectionMap.get(type)) {
                @SuppressWarnings("unchecked")
                Class<T> _class = (Class<T>)classObject;
                try {
                    extensions.add(_class.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return extensions;
    }

    public <T> void bind(Class<T> type, Class<? extends T> implementation) {
        Set<Object> connectedImplementations;
        if (connectionMap.containsKey(type)) {
            connectedImplementations = connectionMap.get(type);
        } else {
            connectedImplementations = new LinkedHashSet<Object>();
            connectionMap.put(type, connectedImplementations);
        }
        connectedImplementations.add(implementation);
    }

    public <T> void unbind(Class<T> type, Class<? extends T> implementation) {
        if (connectionMap.containsKey(type)) {
            Set<Object> connectedImplementations = connectionMap.get(type);
            if (connectedImplementations.remove(implementation)) {
                if (connectedImplementations.isEmpty()) {
                    connectionMap.remove(type);
                }
            }
        }
    }
    
}
