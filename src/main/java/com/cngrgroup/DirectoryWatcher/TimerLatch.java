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
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This will only notify the observers after the sensitivity ( in milliseconds ) has elapsed since the last tickle and
 * calling the tickle function will reset the timer.
 *
 * @author Philip A Senger
 * @version 1.7
 * @see java.util.Observable
 * @since 12/2/14
 */
public class TimerLatch extends Observable {

    private long lastTrigger = 0L;
    private long sensitivity = 0L;
    private Timer timer = null;

    public TimerLatch(long sensitivity, Observer[] observers) {
        super();
        this.sensitivity = sensitivity * 1000L;
        if (observers != null && observers.length != 0) {
            for (Observer observer : observers) {
                this.addObserver(observer);
            }
        }
    }

    public void trigger() {
        System.out.println("TimerLatch.trigger");
        lastTrigger = now();
        if (timer == null && this.countObservers() != 0) {
            scheduleTimerTask();
        }
    }

    private void scheduleTimerTask() {
        timer = new Timer();
        System.out.println("TimerLatch.scheduleTimerTask:sensitivity = " + sensitivity);
        timer.schedule(new MyTimerTask(this), sensitivity);
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private class MyTimerTask extends TimerTask {
        private TimerLatch timerLatch;

        public MyTimerTask(TimerLatch timerLatch) {
            this.timerLatch = timerLatch;
        }

        @Override
        public void run() {
            System.out.println("MyTimerTask.run:" + lastTrigger + ":" + (now() - sensitivity));
            if (lastTrigger <= (now() - sensitivity)) {
                timerLatch.setChanged();
                timerLatch.notifyObservers();
            } else {
                timerLatch.scheduleTimerTask();
            }
        }
    }
}
