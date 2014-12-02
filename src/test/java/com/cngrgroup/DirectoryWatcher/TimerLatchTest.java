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

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
@Category(com.cngrgroup.DirectoryWatcher.HeadLessTest.class)
public class TimerLatchTest {

    /**
     * Method: purgeObserver()
     */
    @Test
    public void testNullConstructor() throws Exception {
        TimerLatch timerLatch = new TimerLatch(1L, null);
        timerLatch.trigger();
    }

    @Test
    public void testNonNullConstructor() {
        MyTestTrigger myTestTrigger = new MyTestTrigger();
        TimerLatch timerLatch = new TimerLatch(2L, new Observer[]{myTestTrigger});
        timerLatch.trigger();
        try {
            Thread.sleep(1 * 1000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        timerLatch.trigger();
        try {
            Thread.sleep(4 * 1000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("This should have fired.", myTestTrigger.isState());
    }

    private class MyTestTrigger implements Observer {
        boolean state = false;

        private MyTestTrigger() {
        }

        public boolean isState() {
            return state;
        }

        @Override
        public void update(Observable o, Object arg) {
            state = true;
        }
    }
}
