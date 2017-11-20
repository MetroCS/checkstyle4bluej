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
import java.util.Iterator;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

/**
 * Describe class Auditor
 * @author Rick Giles
 * @author CS4250 Students (MSU Denver)
 * @version 1.5
 */
public class Auditor implements AuditListener
{
    /** List of FileAuditors, one for each audited file */
    private List<FileAuditor> mFileAuditors = null;


    /** FileAuditor for currently processed file */
    private FileAuditor mCurrentFileAuditor;


    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void auditStarted(AuditEvent aEvt)
    {
        mFileAuditors = new ArrayList<FileAuditor>();
    }

    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void auditFinished(AuditEvent aEvt)
    {
        // nothing to do
    }

    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void fileStarted(AuditEvent aEvt)
    {
        // use one in list?
        Iterator it = mFileAuditors.iterator();
        while (it.hasNext()) {
            final FileAuditor auditor = (FileAuditor) it.next();
            if (auditor.toString().equals(aEvt.getFileName())){
                mCurrentFileAuditor = auditor;
                return;
            }
        }
        mCurrentFileAuditor = new FileAuditor(aEvt);
        mFileAuditors.add(mCurrentFileAuditor);
    }

    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void fileFinished(AuditEvent aEvt)
    {
        // nothing to do
    }

    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void addError(AuditEvent aEvt)
    {
        mCurrentFileAuditor.addError(aEvt);
    }

    /** @see com.puppycrawl.tools.checkstyle.api.AuditListener */
    public void addException(AuditEvent aEvt, Throwable aThrowable)
    {
        mCurrentFileAuditor.addException(aEvt, aThrowable);
    }


    /**
     * Returns the list of file auditors for this auditor.
     * @return the list of file auditors for this auditor.
     */
    public List<FileAuditor> getFileAuditors()
    {
        return mFileAuditors;
    }

}
