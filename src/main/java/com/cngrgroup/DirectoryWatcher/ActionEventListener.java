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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * ActionEventListener
 *
 * @author Philip A Senger
 * @version 1.7
 * @see java.util.Observable
 * @see java.awt.event.ActionListener
 * @since 12/2/14
 */
public class ActionEventListener extends Observable implements ActionListener {
    private MenuItem menuItemStart;
    private MenuItem menuItemPause;
    private TrayIcon trayIcon;
    private Image imageStart;
    private Image imagePause;

    public ActionEventListener(MenuItem menuItemStart, MenuItem menuItemPause, TrayIcon trayIcon, Image imageStart, Image imagePause) {
        super();
        this.menuItemStart = menuItemStart;
        this.menuItemPause = menuItemPause;
        this.trayIcon = trayIcon;
        this.imageStart = imageStart;
        this.imagePause = imagePause;
        this.menuItemStart.setEnabled(true);
        this.menuItemPause.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Watcher.USER_INITIATED_START.equalsIgnoreCase(e.getActionCommand())) {
            toggleMenuSelection();
            toggleTrayIcon();
            setChanged();
        } else if (Watcher.USER_INITIATED_PAUSE.equalsIgnoreCase(e.getActionCommand())) {
            toggleMenuSelection();
            toggleTrayIcon();
            setChanged();
        } else if (Watcher.USER_INITIATED_EDIT.equalsIgnoreCase(e.getActionCommand())) {
            setChanged();
        } else if (Watcher.USER_INITIATED_EXIT.equalsIgnoreCase(e.getActionCommand())) {
            System.exit(0);
        } else {
            throw new IllegalStateException("Unknown Action Event caught:" + e.getActionCommand());
        }
        notifyObservers(e.getActionCommand());
    }

    private void toggleMenuSelection() {
        this.menuItemStart.setEnabled(!menuItemStart.isEnabled());
        this.menuItemPause.setEnabled(!menuItemPause.isEnabled());
    }

    private void toggleTrayIcon() {
        if (menuItemStart.isEnabled()) {
            trayIcon.setImage(imagePause);
        } else {
            trayIcon.setImage(imageStart);
        }
    }
}
