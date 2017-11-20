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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Listener for timer.
 * @author Rick Giles
 * @version $Id: FilesChangeListener.java,v 1.5 2007/08/19 03:13:52 stedwar2 Exp $
 */
public class FilesChangeListener implements ActionListener
{
    /** most recent set of files */
    private Set<File> mFiles = new HashSet<File>();

    /** @see java.awt.event.ActionListener */
    public void actionPerformed(ActionEvent aEvent)
    {
        try
        {
//          files changed?
            final Set<File> openFiles = BlueJManager.getInstance().getFiles();
            if (!mFiles.equals(openFiles))
            {
                mFiles = openFiles;
                final BlueJChecker checker = new BlueJChecker();
                final Auditor auditor = checker.process(openFiles);
                CheckstyleExtension.getInstance().viewAudit(auditor);
            }
        }
        catch (Exception ex)
        {
            return;
        }
    }

}
