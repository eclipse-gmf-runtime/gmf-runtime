/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.View;

/**
 * A command to add a view to a parent view at a given index
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
		super(Messages.getString("AddCommand.Label"), null); //$NON-NLS-1$
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
		return Messages.getString("AddCommand.Label"); //$NON-NLS-1$
	}

}
