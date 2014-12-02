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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The worker actually executes the command.
 *
 * @author Philip A Senger
 * @version 1.7
 * @see java.lang.Runnable
 * @since 12/2/14
 */
public class Worker implements Runnable {

    private String[] commands;
    private String workingDir;

    /**
     * Passing a Settings object here, would be problematic. It would be impossible to
     * handle state changes. Therefore, we only accept values at a given construction time.
     *
     * @param aCommand    the command to execute on the OS.
     * @param aWorkingDir the working directory to execute these commands from.
     */
    public Worker(String aCommand, String aWorkingDir) {
        super();
        this.commands = aCommand.split(" ");
        this.workingDir = aWorkingDir;
    }

    @Override
    public void run() {
//        System.out.println("Start");
//        PrintWriter writer = null;
        try {
            WorkerControlState.getInstance().setState(WorkerControlState.State.RUNNING);
//            writer = new PrintWriter("c:\\Watcher.log", "UTF-8");
            List<String> strings = new ArrayList<>(Arrays.asList(commands));
            ProcessBuilder processBuilder = new ProcessBuilder(strings);
            processBuilder.directory(new File(workingDir));
            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
//            while ((line = br.readLine()) != null) {
//                writer.println(line);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            WorkerControlState.getInstance().setState(WorkerControlState.State.WAITING);
//            System.out.println("Done");
        }
    }
}
