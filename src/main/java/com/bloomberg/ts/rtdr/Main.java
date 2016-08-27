package com.bloomberg.ts.rtdr;

/**
 * Created by cksharma on 8/23/16.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Properties;

public class Main {

    WatchService watcher;

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
    }

    private void init() {
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("project.properties").getFile());
            InputStream input = new FileInputStream(file);

            properties.load(input);

            String directory = properties.getProperty("DIRECTORY_TO_LISTEN");

            Path pathDir = Paths.get(directory);

            watcher = pathDir.getFileSystem().newWatchService();

            pathDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE );

            watchDirectory();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void watchDirectory() {
        try {
            boolean flag;
            do {
                WatchKey watckKey = watcher.take();
                synchronized (this) {
                    List<WatchEvent<?>> events = watckKey.pollEvents();
                    for (WatchEvent event : events) {
                        String newPath = event.context().toString();
                        if (newPath.endsWith("___")) continue;
                        processDirectoryChange(event.context().toString());
                    }
                    flag = watckKey.reset();
                }
            } while (flag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void processDirectoryChange(String fileName) {
        System.out.println("File changed " + fileName);
    }
}
