package hu.webarticum.jpluginmanager.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public abstract  class AbstractDirectoryPluginLoader implements PluginLoader {
        
        private final File pluginDirectory;
        
        private final String fileExtension;
        
        public AbstractDirectoryPluginLoader(File pluginDirectory, String fileExtension) {
            this.pluginDirectory = pluginDirectory;
            this.fileExtension = fileExtension;
        }
        
        @Override
        public List<PluginContainer> loadAll() {
            List<PluginContainer> pluginContainers = new ArrayList<PluginContainer>();
            File[] files = pluginDirectory.listFiles(new FileExtensionFilter());
            if (files != null) {
                for (File file: files) {
                    String fileName = file.getName();
                    String pluginClassName = getClassNameByFileName(fileName);
                    PluginContainer pluginContainer = loadPluginContainer(file, pluginClassName);
                    if (pluginContainer != null) {
                        pluginContainers.add(pluginContainer);
                    }
                }
            }
            return pluginContainers;
        }
        
        private String getClassNameByFileName(String fileName) {
            String result = fileName.substring(0, fileName.length() - 4);
            result = result.replaceAll("@.*$", "");
            return result;
        }

        protected abstract PluginContainer loadPluginContainer(File file, String pluginClassName);
        
        private class FileExtensionFilter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("." + fileExtension);
            }
            
        }

    }
