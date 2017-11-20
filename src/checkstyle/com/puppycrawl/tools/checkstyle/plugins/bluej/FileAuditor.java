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

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

/**
 * Holds audit events for one file.
 * @author Rick Giles
 * @version $Id: FileAuditor.java,v 1.4 2007/08/19 03:13:52 stedwar2 Exp $
 */
public class FileAuditor implements Comparable
{
    /** event that spawned this file auditor */
    private final AuditEvent mAuditEvent;


    /** events for the file of this file auditor */
    private final List<AuditEvent> mEvents = new ArrayList<AuditEvent>();

    /**
     * Constructs a <code>FileAuditor</code> for a given file.
     * @param aEvt audit event for the file.
     */
    public FileAuditor(AuditEvent aEvt)
    {
        mAuditEvent = aEvt;
    }

    /**
     * Notify that an audit error was discovered on a specific file.
     * @param aEvt the event details.
     */
    public void addError(AuditEvent aEvt)
    {
        mEvents.add(aEvt);
    }

    /**
     * Notify that an exception happened while performing audit.
     * @param aEvt the event details.
     * @param aThrowable details of the exception.
     */
    public void addException(
        AuditEvent aEvt,
        @SuppressWarnings("unused") Throwable aThrowable)
    {
        mEvents.add(aEvt);
    }

    /**
     * Return the audit events for this file auditor.
     * @return the audit events for this file auditor.
     */
    public List<AuditEvent> getEvents()
    {
        return mEvents;
    }

    /**
     * Return a string representation of this file auditor.
     * The returned string is the name of the file for the
     * auditor.
     * @return a string representation of this file auditor.
     */
    public String toString()
    {
        return mAuditEvent.getFileName();
    }

    /**
     * Returns the name of the class for this file auditor, without package
     * qualifiers.
     * @return the name of the class for this file auditor.
     */
    public String getBaseClassName()
    {
        String result = mAuditEvent.getFileName();
        int i = result.lastIndexOf("/");
        if (i == -1) {
            i = result.lastIndexOf("\\");
        }
        result = result.substring(i + 1);
        if (result.endsWith(".java")) {
            result = result.substring(0, result.lastIndexOf("."));
        }
        return result;
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(Object aObject)
    {
        final FileAuditor other = (FileAuditor) aObject;
        return getBaseClassName().compareTo(other.getBaseClassName());
    }
}
