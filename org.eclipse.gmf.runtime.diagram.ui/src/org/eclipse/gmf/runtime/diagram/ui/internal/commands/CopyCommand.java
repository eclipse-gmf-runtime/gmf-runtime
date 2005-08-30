/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import com.ibm.xtools.notation.View;
/**
 * Copy Command used to copy the list of <code>IView</code> to the system
 * clipboard
 * 
 * @author Vishy Ramaswamy
 * @canBeSeenBy %level1
 */
public class CopyCommand extends ClipboardCommand {
    /**
     * The list of <code>View</code> used for the copy operation
     */
    private final List source;

    /**
     * Constructor for CopyCommand.
     * @param viewContext
     * @param source
     */
    public CopyCommand(
        View viewContext,
        List source) {
        this(null, viewContext, source);
    }

    /**
     * Constructor for CopyCommand.
     * @param label
     * @param viewContext
     * @param source
     */
    public CopyCommand(
        String label,
        View viewContext,
        List source) {
        super(label, viewContext);

        Assert.isNotNull(source);
        this.source = source;
    }

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
        /* Copy all the views */
        copyToClipboard(getSource());
        return newOKCommandResult();
    }

    /**
     * Returns the source.
     * @return List
     */
    public List getSource() {
        return source;
    }
}
