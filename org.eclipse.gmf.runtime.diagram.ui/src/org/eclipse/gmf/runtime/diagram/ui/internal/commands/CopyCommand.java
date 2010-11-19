/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.notation.View;
/**
 * Copy Command used to copy the list of <code>IView</code> to the system
 * clipboard
 * 
 * @author Vishy Ramaswamy
 */
public class CopyCommand extends ClipboardCommand {
    /**
     * The list of <code>View</code> used for the copy operation
     */
    private final List source;

    /**
     * Constructor for CopyCommand.
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param viewContext
     * @param source
     */
    public CopyCommand(TransactionalEditingDomain editingDomain, 
        View viewContext,
        List source) {
        this(editingDomain, null, viewContext, source);
    }

    /**
     * Constructor for CopyCommand.
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label
     * @param viewContext
     * @param source
     */
    public CopyCommand(TransactionalEditingDomain editingDomain, 
        String label,
        View viewContext,
        List source) {
        super(editingDomain, label, viewContext);

        Assert.isNotNull(source);
        this.source = source;
    }

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
        /* Copy all the views */
        copyToClipboard(getSource());
        return CommandResult.newOKCommandResult();
    }

    /**
     * Returns the source.
     * @return List
     */
    public List getSource() {
        return source;
    }
}
