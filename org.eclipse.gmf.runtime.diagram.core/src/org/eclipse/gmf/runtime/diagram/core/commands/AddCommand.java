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

package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A command to add a view to a another view at a given index
 * 
 * @author melaasar
 */
public class AddCommand extends AbstractModelCommand{

	private IAdaptable parent;
	private IAdaptable child;
	private int index;

	/**
	 * Creates a new <code>AddCommand</code>
	 * @param parent The parent view adapter
	 * @param child The child view adapter
	 */
	public AddCommand(IAdaptable parent, IAdaptable child) {
		this(parent, child, ViewUtil.APPEND);
	}
	
	/**
	 * Creates a new <code>AddCommand</code>
	 * @param parent The parent view adapter
	 * @param child The child view adapter
	 * @param index the child insertion index
	 */
	public AddCommand(IAdaptable parent, IAdaptable child, int index) {
		super(DiagramCoreMessages.AddCommand_Label, null);
		assert null != parent : "Null parent in AddCommand";//$NON-NLS-1$
		assert null != child : "Null child in AddCommand";//$NON-NLS-1$		
		this.parent = parent;
		this.child = child;
		this.index = index;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		View view = (View) parent.getAdapter(View.class);
		if (view != null)
			return getWorkspaceFilesFor(view);
		return super.getAffectedObjects();
	}

	/** 
	 * executes the command; which will get the child and the containaer from
	 * the <code>IAdaptable<code> and then insert the child at the given index
	 * in the containers child list.
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		View childView = (View) child.getAdapter(View.class);
		View parentView = (View) parent.getAdapter(View.class);
		if (index==ViewUtil.APPEND)
			parentView.insertChild(childView);
		else
			parentView.insertChildAt(childView, index);
		return newOKCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public String getLabel() {
		return DiagramCoreMessages.AddCommand_Label;
	}

}
