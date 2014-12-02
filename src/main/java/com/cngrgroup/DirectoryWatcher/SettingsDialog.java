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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField workingDir;
    private JTextField monitorDir;
    private JCheckBox recursive;
    private JSlider sensitivity;
    private JTextPane command;
    private JLabel sensitivityValue;
    private JButton workDirectorySelector;
    private JButton monitorDirectorySelector;
    private UserPreferences originalUserPreferences;

    public SettingsDialog() {
        super();
        pack();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        sensitivity.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setSensitivityLabel(((JSlider) e.getSource()).getValue());
            }
        });
        workDirectorySelector.addActionListener(new DirectorySelector(workingDir));
        monitorDirectorySelector.addActionListener(new DirectorySelector(monitorDir));
    }

    private void onOK() {
        transposeDialogChanges(originalUserPreferences);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void setInitialDialogValues(UserPreferences data) {
        if (data == null) {
            throw new IllegalArgumentException("User Preferences can not be null.");
        } else {
            originalUserPreferences = data;
            workingDir.setText(data.getWorkingDir());
            monitorDir.setText(data.getMonitorDir());
            recursive.setSelected(data.isRecursive());
            command.setText(data.getCommand());
            sensitivity.setValue(data.getSensitivity());
            setSensitivityLabel(data.getSensitivity());
        }
    }

    private void setSensitivityLabel(int value) {
        sensitivityValue.setText(String.valueOf(value) + " Seconds");
    }

    public void transposeDialogChanges(UserPreferences userPreferences) {
        userPreferences.setWorkingDir(workingDir.getText());
        userPreferences.setMonitorDir(monitorDir.getText());
        userPreferences.setRecursive(recursive.isSelected());
        userPreferences.setCommand(command.getText());
        userPreferences.setSensitivity(sensitivity.getValue());
        userPreferences.notifyObservers();
    }

    private class DirectorySelector implements ActionListener {
        private JTextField directoryPlaceHolder;

        public DirectorySelector(JTextField directoryPlaceHolder) {
            this.directoryPlaceHolder = directoryPlaceHolder;
        }

        public void selectDirectory(String originalValue) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File(originalValue));
            chooser.setDialogTitle("Choice a directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                this.directoryPlaceHolder.setText(chooser.getSelectedFile().toString());
            } else {
                this.directoryPlaceHolder.setText(originalValue);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectDirectory(this.directoryPlaceHolder.getText());
        }
    }


}
