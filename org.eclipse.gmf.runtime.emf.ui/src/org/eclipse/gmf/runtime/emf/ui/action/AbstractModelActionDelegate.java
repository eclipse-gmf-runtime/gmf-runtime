/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui.action;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;

/**
 * The abstract parent of all concrete action delegates that execute model
 * commands. Concrete subclasses must provide a definition of the
 * <code>doRun()</code> method to gather any required input and execute a
 * model command.
 * 
 * @author khussey
 */
public abstract class AbstractModelActionDelegate
	extends AbstractActionDelegate {

	/**
	 * Constructs a new model action delegate.
	 */
	protected AbstractModelActionDelegate() {
		super();
	}

	/**
	 * Runs this model action delegate as a read action.
	 * 
	 * @see IRepeatableAction#run(IProgressMonitor)
	 */
	public final void run(final IProgressMonitor progressMonitor) {
		try {
			OperationUtil.runAsRead(new Runnable() {

				public void run() {
					AbstractModelActionDelegate.super.run(progressMonitor);
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
