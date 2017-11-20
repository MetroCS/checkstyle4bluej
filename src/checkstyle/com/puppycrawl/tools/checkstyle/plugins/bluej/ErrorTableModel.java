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

import javax.swing.table.AbstractTableModel;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

/**
 * Model for error table of AuditFrame.
 * @author Rick Giles
 * @version $Id: ErrorTableModel.java,v 1.5 2007/08/19 03:13:52 stedwar2 Exp $
 */
public class ErrorTableModel extends AbstractTableModel
{


    /** names for column headers */
    private static final String[] COLUMN_NAMES =
        {"Line", "Column", "Error"};


    /** events reported by the model */
    private AuditEvent[] mEvents = new AuditEvent[0];

    /**
     * Sets the events for the model.
     * @param aEvents events for the model.
     */
    public void setEvents(AuditEvent[] aEvents)
    {
        mEvents = aEvents;
        fireTableDataChanged();
    }


    /** @see javax.swing.table.TableModel#getRowCount() */
    public int getRowCount()
    {
        return mEvents.length;
    }

    /** @see javax.swing.table.TableModel#getColumnCount() */
    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    /** @see javax.swing.table.TableModel#getColumnName(int) */
    public String getColumnName(int aCol)
    {
        return COLUMN_NAMES[aCol];
    }


    /** @see javax.swing.table.TableModel#getValueAt(int, int) */
    public Object getValueAt(int aRow, int aCol)
    {
        switch (aCol) {
        case 0:
            return ("" + mEvents[aRow].getLine());
        case 1:
            return "" + mEvents[aRow].getColumn();
        case 2:
            return mEvents[aRow].getMessage();
        default:
            return null;
        }
    }

}
