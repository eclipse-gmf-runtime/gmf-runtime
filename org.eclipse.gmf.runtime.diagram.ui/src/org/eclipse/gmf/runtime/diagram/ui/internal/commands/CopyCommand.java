/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.notation.View;
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
