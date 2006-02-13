/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A command to add a view to a another view at a given index
 * 
 * @author melaasar
 */
public class AddCommand extends AbstractTransactionalCommand {

	private IAdaptable parent;
	private IAdaptable child;
	private int index;

	/**
     * Creates a new <code>AddCommand</code>
     * 
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param parent
     *            The parent view adapter
     * @param child
     *            The child view adapter
     */
	public AddCommand(TransactionalEditingDomain editingDomain, IAdaptable parent, IAdaptable child) {
		this(editingDomain, parent, child, ViewUtil.APPEND);
	}
	
	/**
	 * Creates a new <code>AddCommand</code>
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param parent The parent view adapter
	 * @param child The child view adapter
	 * @param index the child insertion index
	 */
	public AddCommand(TransactionalEditingDomain editingDomain, IAdaptable parent, IAdaptable child, int index) {
		super(editingDomain,
            DiagramCoreMessages.AddCommand_Label, null);
        assert null != parent : "Null parent in AddCommand";//$NON-NLS-1$
		assert null != child : "Null child in AddCommand";//$NON-NLS-1$		
		this.parent = parent;
		this.child = child;
		this.index = index;
	}
    
    public List getAffectedFiles() {
        View view = (View) parent.getAdapter(View.class);
        
        if (view != null) {
            List result = new ArrayList();
            IFile file = EObjectUtil.getWorkspaceFile(view);
            
            if (file != null) {
                result.add(file);
            }
            return result;
        }
        
        return super.getAffectedFiles();
    }

	/** 
	 * executes the command; which will get the child and the containaer from
	 * the <code>IAdaptable<code> and then insert the child at the given index
	 * in the containers child list.
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
            IAdaptable info)
        throws ExecutionException {
        
		View childView = (View) child.getAdapter(View.class);
		View parentView = (View) parent.getAdapter(View.class);
		if (index==ViewUtil.APPEND)
			parentView.insertChild(childView);
		else
			parentView.insertChildAt(childView, index);
		return CommandResult.newOKCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public String getLabel() {
		return DiagramCoreMessages.AddCommand_Label;
	}

}
