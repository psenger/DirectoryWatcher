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

import java.io.Serializable;
import java.util.Observable;

/**
 * <p>The Object holding the user's preferences, such as the command to execute, the working directory,
 * directory to monitor, the recursive flag, and the sensitivity.</p>
 *
 * @author Philip A Senger
 * @version 1.7
 * @see Observable
 * @since 12/2/14
 */
@SuppressWarnings("PointlessBooleanExpression")
public class UserPreferences extends Observable implements Serializable {

    private static final long serialVersionUID = 2938637513460398105L;

    private String command = "";
    private String workingDir = "";
    private String monitorDir = "";
    private boolean recursive = false;
    private int sensitivity = 0;

    public UserPreferences(String command, String workingDir, String monitorDir, boolean recursive, int sensitivity) {
        super();
        this.command = (command == null) ? "" : command;
        this.workingDir = (workingDir == null) ? "" : workingDir;
        this.monitorDir = (monitorDir == null) ? "" : monitorDir;
        this.recursive = recursive;
        this.sensitivity = sensitivity;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        if (this.command.equals(command) == false) {
            this.command = command;
            setChanged();
        }
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        if (this.workingDir.equals(workingDir) == false) {
            this.workingDir = workingDir;
            setChanged();
        }
    }

    public String getMonitorDir() {
        return monitorDir;
    }

    public void setMonitorDir(String monitorDir) {
        if (this.monitorDir.equals(monitorDir) == false) {
            this.monitorDir = monitorDir;
            setChanged();
        }
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        if (this.recursive != recursive) {
            this.recursive = recursive;
            setChanged();
        }
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        if (this.sensitivity != sensitivity) {
            this.sensitivity = sensitivity;
            setChanged();
        }
    }

}
