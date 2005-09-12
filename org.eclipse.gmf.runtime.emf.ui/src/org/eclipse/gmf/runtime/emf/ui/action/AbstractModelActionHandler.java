/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;

/**
 * The abstract parent of all concrete action handlers that execute model
 * commands. Concrete subclasses must provide a definition of the
 * <code>doRun()</code> method to gather any required input and execute a model
 * command.
 * 
 * @author khussey
 */
public abstract class AbstractModelActionHandler
	extends AbstractActionHandler {

	/**
	 * Constructs a new model action handler for the specified workbench part.
	 * 
	 * @param workbenchPart The workbench part to which this model action
	 *                       handler applies.
	 */
	protected AbstractModelActionHandler(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/**
	 * Constructs a new model action handler for the specified workbench page.
	 * 
	 * @param workbenchPage The workbench page to which this model action
	 *                       handler applies.
	 */
	protected AbstractModelActionHandler(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * Runs this model action handler as a read action.
	 * 
	 * @see IRepeatableAction#run(IProgressMonitor)
	 */
	public final void run(final IProgressMonitor progressMonitor) {
		try {
			OperationUtil.runAsRead(new Runnable() {

				public void run() {
					AbstractModelActionHandler.super.run(progressMonitor);
				}
			});
		} catch (MSLActionAbandonedException e) {
			// This is not expected to happen.
			Trace.trace(MslUIPlugin.getDefault(),
				MslUIDebugOptions.MODEL_OPERATIONS,
				"MSLActionAbandonedException"); //$NON-NLS-1$
		}
	}

}
