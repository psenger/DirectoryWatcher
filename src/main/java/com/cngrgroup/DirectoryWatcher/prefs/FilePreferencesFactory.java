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

import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * FilePreferencesFactory implementation of PreferencesFactory to store user-defined preferences in a file as defined
 * by system property <tt>java.util.prefs.PreferencesFactory</tt> The file defaults to [user.home]/.fileprefs, but may
 * be overridden with the system property <tt> com.cngrgroup.DirectoryWatcher.prefs.FilePreferencesFactory.SYSTEM_PROPERTY_FILE</tt>
 *
 * @author Philip A Senger
 * @version 1.7
 * @see java.util.prefs.PreferencesFactory
 * @since 12/2/14
 */
public class FilePreferencesFactory implements PreferencesFactory {
    public static final String SYSTEM_PROPERTY_FILE = UserPreferences.class.getCanonicalName();
    private static final Logger log = Logger.getLogger(FilePreferencesFactory.class.getName());
    private static File preferencesFile;
    private Preferences rootPreferences;

    public static File getPreferencesFile() {
        if (preferencesFile == null) {
            String prefsFile = System.getProperty(SYSTEM_PROPERTY_FILE);
            if (prefsFile == null || prefsFile.length() == 0) {
                prefsFile = System.getProperty("user.home") + File.separator + ".fileprefs";
            }
            preferencesFile = new File(prefsFile).getAbsoluteFile();
            log.finer("Preferences file is " + preferencesFile);
        }
        return preferencesFile;
    }

    public Preferences systemRoot() {
        return userRoot();
    }

    public Preferences userRoot() {
        if (rootPreferences == null) {
            log.finer("Instantiating root preferences");

            rootPreferences = new FilePreferences(null, "");
        }
        return rootPreferences;
    }

}