////////////////////////////////////////////////////////////////////////////////
// BlueJ Checkstyle extension:
//    Checks Java source code for adherence to a set of rules.
// Copyright (C) 2003-2004  Rick Giles
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.plugins.bluej;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bluej.extensions.PreferenceGenerator;

/**
 * Manages Checkstyle extension panel in BlueJ preferences.
 * @author Rick Giles
 * @version $Id: Preferences.java,v 1.5 2007/08/19 03:13:52 stedwar2 Exp $
 */
public class Preferences implements PreferenceGenerator
{
    /** configuration file name when Preferences opens */
    private String mBeforeConfigFileName;

    /** properties file name when Preferences opens */
    private String mBeforePropsFileName;

    /** extension panel */
    private JPanel mPanel;

    /** contains name of Checkstyle configuration file */
    private JTextField mConfigFileTextField;

    /** selection of configuration file */
    private JButton mConfigFileButton;

    /** contains name of Checkstyle properties file */
    private JTextField mPropsFileTextField;

    /** selection of properties file */
    private JButton mPropsFileButton;

    /** number of panel grid rows */
    private static final int ROWS = 2;

    /** width of text fields */
    private static final int FIELD_WIDTH = 40;

    /** extension key for configuration file property */
    public static final String CONFIG_FILE_KEY = "checkstyle.ConfigFile";

    /** extension key for properties file property */
    public static final String PROPS_FILE_KEY = "checkstyle.PropsFile";

    /**
     * Action listener for user click on Select button.
     * @author Rick Giles
     * @version 13-May-2003
     */
    private class ButtonListener implements ActionListener
    {
        /** @see java.awt.event.ActionListener */
        public void actionPerformed(ActionEvent aEvent)
        {
            final JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            final File selectedFile = chooser.getSelectedFile();
            final String fileName = selectedFile.toString();
            if (aEvent.getSource() == mConfigFileButton) {
                mConfigFileTextField.setText(fileName);
            }
            else {
                mPropsFileTextField.setText(fileName);
            }
        }
    }


    /**
     * Creates a <code>Preferences</code> object that manages
     * the Checkstyle extension panel of the BlueJ Preferences dialog.
     * Panel allows user to select a Checkstyle configuration and
     * properties files.
     */
    public Preferences()
    {
        mPanel = new JPanel();
        //mPanel.setLayout(new GridLayout(ROWS, COLS));

        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(ROWS, 1));
        labelPanel.add(new JLabel("Configuration File"));
        labelPanel.add(new JLabel("Properties File"));
        mPanel.add(labelPanel);

        final JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(ROWS, 1));
        mConfigFileTextField = new JTextField(FIELD_WIDTH);
        fieldPanel.add(mConfigFileTextField);
        mPropsFileTextField = new JTextField(FIELD_WIDTH);
        fieldPanel.add(mPropsFileTextField);
        mPanel.add(fieldPanel);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(ROWS, 1));
        mConfigFileButton = new JButton("Select");
        mConfigFileButton.addActionListener(new ButtonListener());
        buttonPanel.add(mConfigFileButton);
        mPropsFileButton = new JButton("Select");
        mPropsFileButton.addActionListener(new ButtonListener());
        buttonPanel.add(mPropsFileButton);
        mPanel.add(buttonPanel);

        // Load the default value
        loadValues();
    }

    /** @see bluej.extensions.PreferenceGenerator#saveValues() */
    public void saveValues()
    {
        // save the preference values in the BlueJ properties file
        final BlueJManager manager =
            BlueJManager.getInstance();
        final String afterConfigFileName = mConfigFileTextField.getText();
        manager.saveConfigFileName(afterConfigFileName);
        final String afterPropsFileName = mPropsFileTextField.getText();
        manager.savePropsFileName(afterPropsFileName);

        // changes?
        if (!(mBeforeConfigFileName.equals(afterConfigFileName))
            || !(mBeforePropsFileName.equals(afterPropsFileName)))
        {
            CheckstyleExtension.getInstance().refreshView();
        }
    }

    /** @see bluej.extensions.PreferenceGenerator#loadValues() */
    public void loadValues()
    {
        final BlueJManager manager =
            BlueJManager.getInstance();

        mBeforeConfigFileName = manager.getConfigFileName();
        mBeforePropsFileName = manager.getPropsFileName();
        mConfigFileTextField.setText(mBeforeConfigFileName);
        mPropsFileTextField.setText(mBeforePropsFileName);
    }

    /** @see bluej.extensions.PreferenceGenerator#getPanel() */
    public JPanel getPanel()
    {
        return mPanel;
    }
}