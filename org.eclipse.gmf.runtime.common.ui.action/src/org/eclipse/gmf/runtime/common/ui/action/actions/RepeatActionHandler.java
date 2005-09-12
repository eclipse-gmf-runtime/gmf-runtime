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

package org.eclipse.gmf.runtime.common.ui.action.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.action.ActionManagerChangeEvent;
import org.eclipse.gmf.runtime.common.ui.action.IActionManagerChangeListener;
import org.eclipse.gmf.runtime.common.ui.action.internal.actions.CommandManagerActionHandler;

/**
 * Represents an action handler that could be registered with a retargetable
 * (global) repeat action to repeat the last command that was run. This class
 * implements the <code>IActionManagerChangeListener</code> interface; hence its
 * instances will automatically refresh themselves when an action manager change
 * notification is received. Repeat action handlers would update the label
 * for a Repeat menu item based on the label for the action that was last run.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.ui.action.IActionManagerChangeListener
 */
public class RepeatActionHandler
	extends CommandManagerActionHandler
	implements IActionManagerChangeListener {

	/**
	 * Constructs a new repeat action handler for the specified workbench part.
	 * 
	 * @param workbenchPart The workbench part to which this repeat action
	 *                       handler applies.
	 */
	public RepeatActionHandler(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#setWorkbenchPart(org.eclipse.ui.IWorkbenchPart)
	 */
	protected void setWorkbenchPart(IWorkbenchPart workbenchPart) {
		if (getWorkbenchPart() != null){
			getActionManager().removeActionManagerChangeListener(this);
		}
		super.setWorkbenchPart(workbenchPart);
		if (getWorkbenchPart() != null){
			getActionManager().addActionManagerChangeListener(this);
		}
	}

	/**
	 * Handles an event indicating that an action manager has changed.
	 * 
	 * @param event The action manager change event to be handled.
	 * 
	 **/
	public void actionManagerChanged(ActionManagerChangeEvent event) {
		refresh();
	}

	/**
	 * Runs this repeat action handler, passing the triggering SWT event.
	 * 
	 * @param event The SWT event which triggered this action being run.
	 * 
	 */
	public final void runWithEvent(Event event) {
		run();
	}

	/**
	 * Refreshes the label and enabled state of this repeat action handler.
	 */
	public void refresh() {
		setEnabled(getActionManager().canRepeat());
		setText(getActionManager().getRepeatLabel());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		doRun(new NullProgressMonitor());
	}

	/**
	 * Performs the actual work when this repeat action handler is run by asking
	 * the action manager to repeat the last action that was run.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		getActionManager().repeat();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

}
