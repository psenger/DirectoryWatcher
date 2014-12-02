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

import java.util.Observable;

/**
 * <h1>WorkerControlState:</h1>
 * <p>A Singleton that tracks the state of the worker.</p>
 *
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
public class WorkerControlState extends Observable {

    private static WorkerControlState ourInstance = new WorkerControlState();
    /**
     * The state.
     */
    private State state = State.WAITING;

    /**
     * Singleton constructor.
     */
    private WorkerControlState() {
        super();
        this.state = State.WAITING;
    }

    /**
     * Method to gain access to the singleton.
     *
     * @return the one and only WorkerControlState
     */
    public static WorkerControlState getInstance() {
        return ourInstance;
    }

    /**
     * Getter to the state of the worker, from the instance.
     *
     * @return the State of the worker.
     */
    public synchronized State getState() {
        return state;
    }

    /**
     * Setter for the state of the worker, from the instance.
     *
     * @param state The new State. null safe, but null does nothing.
     */
    public synchronized void setState(State state) {
        if (state != null) {
            if (!this.state.equals(state)) {
                this.state = state;
                setChanged();
                notifyObservers(state);
            }
        }
    }

    /**
     * All possible states the work can be in.
     */
    public enum State {
        PAUSED,
        WAITING,
        RUNNING
    }
}
