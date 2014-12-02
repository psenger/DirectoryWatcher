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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Observable;
import java.util.Observer;

/**
 * ControlState Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Nov 14, 2014</pre>
 */
@Category(com.cngrgroup.DirectoryWatcher.HeadLessTest.class)
public class WorkerControlStateTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Setting the state to null should not fire the update.
     */
    @Test
    public void testSetStateNull() throws Exception {
        WorkerControlState cs = WorkerControlState.getInstance();
        cs.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Assert.fail("The observable should not have fired");
            }
        });
        cs.setState(null);
    }

    /**
     * Method: getState()
     */
    @Test
    public void testGetSetState() throws Exception {
        WorkerControlState cs = WorkerControlState.getInstance();
        cs.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Assert.assertEquals(WorkerControlState.State.RUNNING, arg);
            }
        });
        cs.setState(WorkerControlState.State.RUNNING);
    }

} 
