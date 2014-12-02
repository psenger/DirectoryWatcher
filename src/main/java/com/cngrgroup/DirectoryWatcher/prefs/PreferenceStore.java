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
import com.cngrgroup.DirectoryWatcher.Watcher;

import java.util.Observable;
import java.util.Observer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The PreferenceStore
 *
 * @author Philip A Senger
 * @version 1.7
 * @since 12/2/14
 */
public class PreferenceStore {

    private static String WATCHER_PREFERENCE_TXT = "watcher-preference.txt";

    protected static String overrideFileName(String fileName) {
        String retString = WATCHER_PREFERENCE_TXT;
        WATCHER_PREFERENCE_TXT = fileName;
        return retString;
    }

    public static void store(UserPreferences userPreferences) throws BackingStoreException {
        if (userPreferences != null) {
            System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
            System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, WATCHER_PREFERENCE_TXT);
            Preferences p = Preferences.userNodeForPackage(UserPreferences.class);
            p.put("command", String.valueOf(userPreferences.getCommand()));
            p.put("workingDir", String.valueOf(userPreferences.getWorkingDir()));
            p.put("monitorDir", String.valueOf(userPreferences.getMonitorDir()));
            p.put("recursive", String.valueOf(userPreferences.isRecursive()));
            p.put("sensitivity", String.valueOf(userPreferences.getSensitivity()));
            p.flush();
        }
    }

    public static UserPreferences retrieve() {
        System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
        System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, WATCHER_PREFERENCE_TXT);
        Preferences p = Preferences.userNodeForPackage(Watcher.class);
        @SuppressWarnings("SimplifiableConditionalExpression")
        UserPreferences userPreferences = new UserPreferences(
                (p == null) ? null : p.get("command", null),
                (p == null) ? null : p.get("workingDir", null),
                (p == null) ? null : p.get("monitorDir", null),
                (p == null) ? false : p.getBoolean("recursive", false),
                (p == null) ? 0 : p.getInt("sensitivity", 0)
        );
        userPreferences.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                try {
                    PreferenceStore.store((UserPreferences) o);
                } catch (BackingStoreException e) {
                    e.printStackTrace();
                }
            }
        });
        return userPreferences;
    }
}
