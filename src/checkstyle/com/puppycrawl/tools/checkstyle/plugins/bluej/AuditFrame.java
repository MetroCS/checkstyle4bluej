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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

/**
 * View for audited files and audit events for each file.
 * @author Rick Giles
 * @version $Id: AuditFrame.java,v 1.7 2007/08/19 03:13:52 stedwar2 Exp $
 */
public class AuditFrame extends JFrame
{
    /** Line column width */
    private static final int LINE_WIDTH = 50;

    /** Column column width */
    private static final int COL_WIDTH = 50;

    /** Error column width */
    private static final int ERROR_WIDTH = 300;

    /** table height */
    private static final int TABLE_HEIGHT = 300;

    /** model for view of file audit events */
    private final ErrorTableModel mModel = new ErrorTableModel();

    /** view for audited files */
    private final JList mFileList = new JList();

    /** default file list rendered when no files in list */
    private final DefaultListCellRenderer mDefaultListCellRenderer =
        new DefaultListCellRenderer();

    /** file list renderer when files present */
    private final FileCellRenderer mFileCellRenderer =
        new FileCellRenderer();

    /**
     * Responds to selection from the list of audited files.
     * @author Rick Giles
     * @version 14-May-2003
     */
    private class ListListener implements ListSelectionListener
    {
        /** @see javax.swing.event.ListSelectionListener */
        public void valueChanged(ListSelectionEvent aEvent)
        {
            if (aEvent.getValueIsAdjusting()) {
                return;
            }
            final JList theList = (JList) aEvent.getSource();
            if (!theList.isSelectionEmpty()) {
                final FileAuditor fileAuditor =
                    (FileAuditor) theList.getSelectedValue();
                final AuditEvent[] events =
                    new AuditEvent[fileAuditor.getEvents().size()];
                for (int i = fileAuditor.getEvents().size() - 1; i >= 0; i--) {
                   events[i] = (AuditEvent) fileAuditor.getEvents().get(i);
                }
                mModel.setEvents(events);
            }
        }
    }

    /**
     * Renders a cell for one file. Draws the name of the file. If the
     * file has audit errors, appends an asterisk after the name and
     * sets the cell background to yellow.
     * @author Rick Giles
     * @version 24-May-2003
     */
    private class FileCellRenderer extends JLabel implements ListCellRenderer
    {
        /** Constructs a <code>FileCellRenderer</code>*/
         public FileCellRenderer()
         {
             setOpaque(true);
         }

         /** @see javax.swing.ListCellRenderer#getListCellRendererComponent */
         public Component getListCellRendererComponent(
             JList aList,
             Object aValue,
             int aIndex,
             boolean aIsSelected,
             boolean aCellHasFocus)
         {
             final FileAuditor auditor = (FileAuditor) aValue;

             // text label
             String text = auditor.getBaseClassName();
             if (auditor.getEvents().size() > 0) {
                 text += "*";
             }
             setText(text);

             // default foreground
             setForeground(aList.getForeground());

             // if cell is selected, set background color to default cell
             // selection background color
            if (aIsSelected) {
              setBackground(aList.getSelectionBackground());
            }
            // otherwise, set cell background color to our custom color
            else {
                if (auditor.getEvents().size() == 0) {
                    setBackground(Color.WHITE);
                }
                else {
                    setBackground(Color.YELLOW);
                }
             }
             return this;
         }
     }

    /**
     * Constructs an audit frame with components to list files and
     * audit events for each file.
     */
    public AuditFrame()
    {
        setTitle("Checkstyle");

        // file list
        final JScrollPane listScrollPane = new JScrollPane(mFileList);
        mFileList.addListSelectionListener(new ListListener());
        mFileList.setCellRenderer(new FileCellRenderer());
        // audit events for one file
        final JTable table = new JTable(mModel);
        TableColumnModel columnMode = table.getColumnModel();
        columnMode.getColumn(0).setPreferredWidth(LINE_WIDTH);
        columnMode.getColumn(1).setPreferredWidth(COL_WIDTH);
        columnMode.getColumn(2).setPreferredWidth(ERROR_WIDTH);
        table.setPreferredScrollableViewportSize(new Dimension(
            LINE_WIDTH + COL_WIDTH + ERROR_WIDTH, TABLE_HEIGHT));
        final JScrollPane errorsScrollPane = new JScrollPane(table);

        final JSplitPane splitPane =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listScrollPane, errorsScrollPane);
        splitPane.setOneTouchExpandable(true);
        getContentPane().add(splitPane);
    }

    /**
     * Sets the auditor for this view.
     * @param aAuditor auditor for this view.
     */
    public synchronized void setAuditor(Auditor aAuditor)
    {
        boolean useSelection = false;

        final Object[] auditors = aAuditor.getFileAuditors().toArray();
        if (auditors.length == 0) {
            mFileList.setCellRenderer(mDefaultListCellRenderer);
            mFileList.setListData(new String[] {"(No files)"});
        }
        else {
            final Object oldSelection = mFileList.getSelectedValue();
            mFileList.setCellRenderer(mFileCellRenderer);
            Arrays.sort(auditors);
            mFileList.setListData(auditors);
            if (oldSelection != null) {
                final int index = Arrays.binarySearch(auditors, oldSelection);
                if (index >= 0) {
                    useSelection = true;
                    mFileList.setSelectedIndex(index);
                }
            }
        }
        if (!useSelection) {
            mModel.setEvents(new AuditEvent[] {});
        }
    }
}
