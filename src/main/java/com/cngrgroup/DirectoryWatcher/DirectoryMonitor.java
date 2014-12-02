/*
 * The MIT License (MIT)
 *
 * Copyright (c) today.year. Philip A Senger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.cngrgroup.DirectoryWatcher;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * <p>DirectoryMonitor responsible for monitoring a directory</p>
 *
 * @author Philip A Senger
 * @version 1.7
 * @see java.lang.Runnable
 * @since 12/2/14
 */
public class DirectoryMonitor implements Runnable {

    private WatchService watcher;
    private Map<WatchKey, Path> keys;
    private TimerLatch timerLatch;
    private boolean recursive;
    private UserPreferences userPreferences = null;
    private Observer observer = null;

    /**
     * @param userPreferences - We will attach an observer to the Settings object, so we are aware of state change, because this is a long run
     * @param timerLatch      the Latch Timer responsible for running the worker.
     */
    public DirectoryMonitor(UserPreferences userPreferences, TimerLatch timerLatch) {
        super();
        try {
            this.userPreferences = userPreferences;
            this.timerLatch = timerLatch;
            this.watcher = FileSystems.getDefault().newWatchService();
            this.observer = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    UserPreferences userPreferences = (UserPreferences) o;
                    resetSettings(userPreferences.getMonitorDir(), userPreferences.isRecursive());
                }
            };
            this.userPreferences.addObserver(this.observer);
            resetSettings(this.userPreferences.getMonitorDir(), this.userPreferences.isRecursive());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    private void resetSettings(String monitorDir, boolean recursive) {
        this.recursive = recursive;
        try {
            this.keys = new HashMap<>();
            Path path = Paths.get(monitorDir);
            if (recursive) {
                registerAll(path);
            } else {
                register(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Register the given directory with the WatchService
     *
     * @param dir path to register with
     * @throws IOException
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     *
     * @param start path to start on.
     * @throws IOException
     */
    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    private void processEvents() {
        for (; ; ) {
            WatchKey key;
            try {
                key = watcher.take(); // wait for a key
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("Watch Key not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // This event indicate that events may have been lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                timerLatch.trigger();

                // if directory is created, and watching recursively, then register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException ignored) {
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            processEvents();
        } finally {
            this.userPreferences.deleteObserver(this.observer);
            this.timerLatch.deleteObservers();
        }
    }
}
