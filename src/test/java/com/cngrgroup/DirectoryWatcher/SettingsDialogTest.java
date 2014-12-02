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

import java.util.Observable;
import java.util.Observer;

/**
 * SettingsDialog Tester.
 *
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
public class SettingsDialogTest {
    /**
     * Setting the state to null should not fire the update.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStateNull() throws Exception {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setInitialDialogValues(null);
    }

    /**
     * Setting the state valid values.
     */
    @Test
    public void testSetStateNotNull() throws Exception {
        UserPreferences userPreferences = new UserPreferences("", "", "", true, 1);
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setInitialDialogValues(userPreferences);
    }

    /**
     * Setting the state valid values.
     */
    @Test
    public void testTransposeDialogChangesWithNoChange() throws Exception {
        UserPreferences A = new UserPreferences("Before", "Before", "Before", true, 1);
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setInitialDialogValues(A);
        A.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Assert.assertFalse("The observer should not have fired there was no change in the data", true);
            }
        });
        settingsDialog.transposeDialogChanges(A);
    }

    /**
     * Setting the state valid values.
     */
    @Test
    public void testTransposeDialogChanges() throws Exception {
        UserPreferences A = new UserPreferences("Before", "Before", "Before", true, 1);
        UserPreferences B = new UserPreferences("After", "After", "After", false, 2);

        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setInitialDialogValues(A);
        A.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Assert.assertTrue("The observer should have fired there was a change in the data", true);
            }
        });
        settingsDialog.transposeDialogChanges(B);
    }


}
