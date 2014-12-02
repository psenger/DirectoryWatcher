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

package com.cngrgroup.DirectoryWatcher.prefs;

import com.cngrgroup.DirectoryWatcher.UserPreferences;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * PreferenceStore Tester.
 *
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
@Category(com.cngrgroup.DirectoryWatcher.HeadLessTest.class)
public class PreferenceStoreTest {

    private static String previousValue;

    @Before
    public void before() throws Exception {
        previousValue = PreferenceStore.overrideFileName("c:\\TestFile.txt");
    }

    @After
    public void after() throws Exception {
        PreferenceStore.overrideFileName(previousValue);
    }

    /**
     * Method: store(UserPreferences userPreferences)
     */
    @Test
    public void testStoreNull() throws Exception {
        PreferenceStore.store(null);
    }

    /**
     * Method: store(UserPreferences userPreferences)
     */
    @Test
    public void testStoreNotNull() throws Exception {
        PreferenceStore.store(new UserPreferences("Monkey", "NoWhere", "Nothing", true, 1));
    }

    /**
     * Method: retrieve()
     */
    @Test
    public void testRetrieve() throws Exception {
        UserPreferences retrieve = PreferenceStore.retrieve();
        Assert.assertNotNull(retrieve);
        Assert.assertEquals("Monkey", retrieve.getCommand());
        Assert.assertEquals("NoWhere", retrieve.getWorkingDir());
        Assert.assertEquals("Nothing", retrieve.getMonitorDir());
        Assert.assertEquals(true, retrieve.isRecursive());
        Assert.assertEquals(1, retrieve.getSensitivity());
    }


} 
