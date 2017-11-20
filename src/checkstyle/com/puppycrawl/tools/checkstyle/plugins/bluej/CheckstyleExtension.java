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

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import bluej.extensions.BlueJ;
import bluej.extensions.Extension;
import bluej.extensions.event.ApplicationEvent;
import bluej.extensions.event.ApplicationListener;
import bluej.extensions.event.CompileEvent;
import bluej.extensions.event.CompileListener;
import bluej.extensions.event.PackageEvent;
import bluej.extensions.event.PackageListener;

/**
 * BlueJ extension for Checkstyle.
 * @author Rick Giles
 * @author CS4250 Students (MSU Denver)
 * @version 1.11
 */
public class CheckstyleExtension extends Extension
{
    /** true if the log factory has been initialized */
    private boolean mInitialized = false;

    /** Factory for creating org.apache.commons.logging.Log instances */
    private LogFactory mLogFactory;

    /** interval between audit checks (milliseconds) */
    private static final int AUDIT_CHECK_INTERVAL = 2000;

    /** singleton */
    private static CheckstyleExtension sInstance;

    /** Periodically checks for file set changes. */
    private Timer mTimer;

    /** BlueJ tools menu item for Checkstyle */
    private ExtensionMenu mMenu;

    /** display audit results */
    private AuditFrame mFrame = null;

    /** files being compiled */
    private Set<File> mCompilingFiles = new HashSet<File>();

    /** extension name */
    private static final String NAME = "Checkstyle";

    /** extension description */
    private static final String DESCRIPTION =
        "Checks that Java source code adheres to a coding standard.";

    /**  extension version */
    private static final String VERSION = "5.4.1";

    /** extension URL */
    private static final String URL =
        "http://github.com/MetroCS/checkstyle4bluej/";

    /** @see bluej.extensions.event.PackageListener */
    private class CheckstylePackageListener implements PackageListener
    {
        /** @see bluej.extensions.event.PackageListener */
        public void packageOpened(PackageEvent aEvent)
        {
            // refreshView();
        }

        /** @see bluej.extensions.event.PackageListener */
        public void packageClosing(PackageEvent aEvent)
        {
            // refreshView();
        }
    }

    /** @see bluej.extensions.event.CompileListener */
    private class CheckstyleCompileListener implements CompileListener
    {
        /** @see bluej.extensions.event.CompileListener */
        public void compileStarted(CompileEvent aEvent)
        {
            recordCompileStart(aEvent.getFiles());
        }

        /**
         * Records the start of compilation of a set of files.
         * @param aFiles the set of files being compiled.
         */
        private void recordCompileStart(File[] aFiles)
        {
            for (int i = 0; i < aFiles.length; i++) {
                mCompilingFiles.add(aFiles[i]);
            }
            updateTimer();
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileError(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }

        /**
         * Records the end of compilation of a set of files.
         * @param aFiles the set of files ending compilation.
         */
        private void recordCompileEnd(File[] aFiles)
        {
            for (int i = 0; i < aFiles.length; i++) {
                mCompilingFiles.remove(aFiles[i]);
            }
            updateTimer();
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileWarning(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileSucceeded(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
            if (mCompilingFiles.isEmpty()) {
                refreshView();
            }
        }

        /** @see bluej.extensions.event.CompileListener */
        public void compileFailed(CompileEvent aEvent)
        {
            recordCompileEnd(aEvent.getFiles());
        }
    }

    /** @see bluej.extensions.event.ApplicationListener */
    private class CheckstyleApplicationListener implements ApplicationListener
    {

        /**
         * Initializes the audit window.
         * @see bluej.extensions.event.ApplicationListener
         */
        public void blueJReady(ApplicationEvent aEvent)
        {
            buildAuditFrame();
            refreshView();
        }
    }

   /**
     * Returns the single CheckstyleExtension instance.
     * @return  the single CheckstyleExtension instance.
     */
    public static CheckstyleExtension getInstance()
    {
        return sInstance;
    }

    /**
     * Constructs a <code>CheckstyleExtension</code>.
     */
    public CheckstyleExtension()
    {
        // establish singleton extension
        sInstance = this;

        final ActionListener listener = new FilesChangeListener();
        mTimer = new Timer(AUDIT_CHECK_INTERVAL, listener);

        try {
            mLogFactory = LogFactory.getFactory();
            mInitialized = true;
        }
        catch (LogConfigurationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Starts or stops timer.
     */
    private void updateTimer()
    {
        if (mCompilingFiles.isEmpty() && mFrame.isShowing()) {
            mTimer.start();
        }
        else {
            mTimer.stop();
        }
    }

    /** @see bluej.extensions.Extension#isCompatible() */
    public boolean isCompatible()
    {
        return true;
    }

    /** @see bluej.extensions.Extension#startup(bluej.extensions.BlueJ) */
    public void startup(BlueJ aBlueJ)
    {
        // establish singleton manager for the BlueJ application proxy
        BlueJManager.getInstance().setBlueJ(aBlueJ);

        // register listeners
        aBlueJ.addApplicationListener(new CheckstyleApplicationListener());
        aBlueJ.addPackageListener(new CheckstylePackageListener());
        aBlueJ.addCompileListener(new CheckstyleCompileListener());

        // install menu item
        mMenu = new ExtensionMenu();
        aBlueJ.setMenuGenerator(mMenu);

        // install preferences handler
        Preferences myPreferences = new Preferences();
        aBlueJ.setPreferenceGenerator(myPreferences);

        mTimer.start();
    }

    /**
     * Saves audit window information.
     * @see bluej.extensions.Extension#terminate()
     */
    public void terminate()
    {
        BlueJManager.getInstance().saveAuditFrame(mFrame);
        mCompilingFiles.clear();
        mTimer.stop();
    }

    /**
     * Refreshes the audit view. If there is an error, report it.
     */
    public void refreshView()
    {
        if (mFrame.isShowing()) {
            final BlueJChecker checker = new BlueJChecker();
            final Auditor auditor;
            try {
                auditor = checker.processAllFiles();
            }
            catch (CheckstyleException ex) {
                error(ex);
                return;
            }
            viewAudit(auditor);
        }
    }

    /**
     * Creates and installs an audit frame
     */
    private synchronized void buildAuditFrame()
    {
        /** @see java.awt.event.WindowAdapter */
        final class AuditFrameListener extends WindowAdapter
        {
            /** @see java.awt.event.WindowListener */
            public void windowOpened(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowClosed(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowIconified(WindowEvent aEvent)
            {
                updateTimer();
            }

            /** @see java.awt.event.WindowListener */
            public void windowDeiconified(WindowEvent aEvent)
            {
                updateTimer();
            }
        }

        if (mFrame == null) {
            mFrame = new AuditFrame();
            mFrame.addWindowListener(new AuditFrameListener());
            BlueJManager.getInstance().initAuditFrame(mFrame);
            mFrame.pack();
        }
    }

    /**
     * Shows the audit frame.
     */
    public void showAuditFrame()
    {
        buildAuditFrame();
        mFrame.setVisible(true);
        refreshView();
    }

    /**
     * Updates view of audit results.
     * @param aAuditor the auditor with audit results
     */
    public synchronized void viewAudit(final Auditor aAuditor)
    {
        // execute on the application's event-dispatch thread
        final Runnable update = new Runnable()
        {
            public void run()
            {
                if (mFrame != null) {
                    mFrame.setAuditor(aAuditor);
                }
            }
        };
        SwingUtilities.invokeLater(update);
    }

    /** @see bluej.extensions.Extension#getName() */
    public String getName()
    {
        return NAME;
    }

    /** @see bluej.extensions.Extension#getDescription() */
    public String getDescription()
    {
        return DESCRIPTION;
    }

    /** @see bluej.extensions.Extension#getVersion() */
    public String getVersion()
    {
        return VERSION;
    }

    /** @see bluej.extensions.Extension#getURL() */
    public URL getURL()
    {
        URL result = null;
        try {
            result = new URL(URL);
        }
        catch (MalformedURLException e) {
            error(e);
        }
        return result;
    }
    /**
     * Reports an error message.
     * @param aMessage the message to report.
     */
    public void error(String aMessage)
    {
        Frame frame = BlueJManager.getInstance().getCurrentFrame();
        JOptionPane.showMessageDialog(frame, aMessage);
        if (mInitialized) {
            final Log log = mLogFactory.getInstance(CheckstyleExtension.class);
            log.info(aMessage);
        }
        else {
            System.out.println(aMessage);
        }
    }

    /**
     * Reports an exception.
     * @param aException the exception to report.
     */
    public void error(Exception aException)
    {
        aException.printStackTrace();
        error("" + aException);
    }
}
