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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

/**
 * This class provides the functionality to check a set of files.
 * @author Rick Giles
 * @version $Id: BlueJChecker.java,v 1.9 2011/10/25 05:27:10 stedwar2 Exp $
 */
public class BlueJChecker
{
    /**
     * Constructs a <code>BlueJChecker</code>.
     */
    public BlueJChecker()
    {
        // Nothing to do
    }

    /**
     * Audits all files of the open BlueJ packages.
     * @return an Auditor with the audit results.
     * @throws CheckstyleException if there is an error.
     */
    public Auditor processAllFiles()
        throws CheckstyleException
    {
        Set<File> files;
        try {
            files = BlueJManager.getInstance().getFiles();
        }
        catch (Exception e) {
            throw new CheckstyleException(e.getMessage());
        }
        return process(files);
    }

    /**
     * Audits a set of files.
     * @param files the set of files to audit.
     * @return an Auditor with the audit results.
     * @throws CheckstyleException if there is an error.
     */
    public Auditor process(Set<File> files)
        throws CheckstyleException
    {
        final Properties props =
            new Properties(BlueJManager.getInstance().properties());
        try {
            InputStream propStream =
                BlueJManager.getInstance().getPropertyStream();
            if (propStream != null)
            {
                props.load(propStream);
            }
        }
        catch (IOException ex) {
            throw new CheckstyleException(ex.getMessage());
        }

        // create and configure a Checker
        final Checker c = new Checker();
        c.setModuleClassLoader(BlueJChecker.class.getClassLoader());
        final Configuration config =
            ConfigurationLoader.loadConfiguration(
                BlueJManager.getInstance().getConfigStream(),
                new PropertiesExpander(props),
                true);
        c.configure(config);

        final AuditListener auditor = new Auditor();
        c.addListener(auditor);

        // lock TreeWalker for call to static method parse
        synchronized (TreeWalker.class) {
            c.process(new ArrayList<File>(files));
        }
        c.destroy();
        return (Auditor) auditor;
    }
}


