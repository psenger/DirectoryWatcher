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

import com.cngrgroup.DirectoryWatcher.prefs.PreferenceStore;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.BackingStoreException;

/**
 * Main Class
 *
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
public class Watcher {

    public static final String USER_INITIATED_START = "Start";
    private static MenuItem menuItemStart = new MenuItem(USER_INITIATED_START);
    public static final String USER_INITIATED_PAUSE = "Pause";
    private static MenuItem menuItemPause = new MenuItem(USER_INITIATED_PAUSE);
    public static final String USER_INITIATED_EDIT = "Edit";
    private static MenuItem menuItemEdit = new MenuItem(USER_INITIATED_EDIT);
    public static final String USER_INITIATED_EXIT = "Exit";
    private static MenuItem menuItemExit = new MenuItem(USER_INITIATED_EXIT);
    private static UserPreferences userPreferences = null;
    private static Image imageStart = null;
    private static Image imagePause = null;

    private static Thread directoryMonitor = null;
    private static Thread workerThread = null;

    /**
     * Yes, this has a main.
     *
     * @param args from the command prompt.
     */
    public static void main(String[] args) throws IOException, BackingStoreException {
        imageStart = Toolkit.getDefaultToolkit().getImage(getImageUrl("start16x16II.png"));
        imagePause = Toolkit.getDefaultToolkit().getImage(getImageUrl("pause16x16II.png"));

        userPreferences = PreferenceStore.retrieve();

        if (!startTrayIcon()) {
            System.err.println("Error: Your operating system does not support a system tray.");
            System.exit(1);
        }
    }

    private static URL getImageUrl(String fileName) {
        ClassLoader cl = Watcher.class.getClassLoader();
        return cl.getResource("META-INF/" + fileName);
    }

    private static boolean startTrayIcon() {
        if (SystemTray.isSupported()) {

            PopupMenu popup = new PopupMenu();

            TrayIcon trayIcon = new TrayIcon(imagePause, "Watcher", popup);

            WorkerControlState.getInstance().setState(WorkerControlState.State.WAITING);
            SystemTray tray = SystemTray.getSystemTray();

            ActionEventListener actionEventListener = new ActionEventListener(menuItemStart, menuItemPause, trayIcon, imageStart, imagePause);

            actionEventListener.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object command) {
                    if (Watcher.USER_INITIATED_START.equals(command)) {
                        WorkerControlState.State state = WorkerControlState.getInstance().getState();
                        if (WorkerControlState.State.PAUSED.equals(state) || WorkerControlState.State.WAITING.equals(state)) {
                            if (not(directoryMonitor == null) && directoryMonitor.isAlive() && not(directoryMonitor.isInterrupted())) {
                                directoryMonitor.interrupt();
                            }
                            directoryMonitor = null; // old school way of telling the JVM to do a GC on the former object... needs to be investigated if it works still.
                            directoryMonitor = new Thread(new DirectoryMonitor(userPreferences, new TimerLatch(
                                    (long) userPreferences.getSensitivity(),
                                    new Observer[]{
                                            new Observer() {
                                                @Override
                                                public void update(Observable o, Object arg) {
                                                    workerThread = new Thread(new Worker(userPreferences.getCommand(), userPreferences.getWorkingDir()));
                                                    workerThread.start();
                                                }
                                            }
                                    })));
                            directoryMonitor.start();
                        }
                    }
                }
            });

            actionEventListener.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object command) {
                    if (Watcher.USER_INITIATED_PAUSE.equals(command)) {
                        if (not(directoryMonitor == null) && directoryMonitor.isAlive() && not(directoryMonitor.isInterrupted())) {
                            directoryMonitor.interrupt();
                        }
                        directoryMonitor = null;
                        WorkerControlState.getInstance().setState(WorkerControlState.State.PAUSED);
                        // pause the latch timer.
                    }
                }
            });

            actionEventListener.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object command) {
                    if (Watcher.USER_INITIATED_EDIT.equals(command)) {
                        SettingsDialog dialog = new SettingsDialog();
                        dialog.setInitialDialogValues(userPreferences);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }
            });

            menuItemStart.addActionListener(actionEventListener);
            menuItemPause.addActionListener(actionEventListener);
            menuItemEdit.addActionListener(actionEventListener);
            menuItemExit.addActionListener(actionEventListener);

            popup.add(menuItemStart);
            popup.add(menuItemPause);
            popup.addSeparator();
            popup.add(menuItemEdit);
            popup.addSeparator();
            popup.add(menuItemExit);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean not(boolean aQuestion) {
        return !aQuestion;
    }

}
