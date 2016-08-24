package com.bloomberg.ts.rtdr;

/**
 * Created by cksharma on 8/23/16.
 */

import java.nio.file.*;
import java.util.List;

public class Main {

    Path myDir;
    WatchService watcher;

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
    }

    private void init() {
        try {
            myDir = Paths.get("./data");
            watcher = myDir.getFileSystem().newWatchService();
            myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

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
                List<WatchEvent<?>> events = watckKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        processCreation(event.context().toString());
                    }
                }
                flag = watckKey.reset();
            } while (flag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void processCreation(String createdContext) {
        //TODO Business implementation
    }
}
