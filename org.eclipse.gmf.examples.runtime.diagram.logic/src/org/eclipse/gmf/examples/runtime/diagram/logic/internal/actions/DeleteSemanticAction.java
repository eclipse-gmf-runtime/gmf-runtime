/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;

/**
 * This action exists to demonstrate specifically the functionality of notation
 *  semantic procedures when changes happen to the logic semantic elements. More
 *  specifically, we would like for the notation view to be destroyed whenever
 *  a LED semantic element is destroyed.
 *  
 *  @see org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicMetamodelSupportProvider.LogicMetamodelSupport
 *  @see org.eclipse.gmf.runtime.notation.providers.internal.semproc.NotationSemProc
 */
public class DeleteSemanticAction
	extends PresentationAction
	implements LogicActionIds {

	public DeleteSemanticAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	public void init() {
		super.init();
		setText("Delete Semantic Element"); //$NON-NLS-1$
	}

	protected Request createTargetRequest() {
		return new Request("deleteSemanticElement"); //$NON-NLS-1$
	}
	
	protected void doRun(IProgressMonitor progressMonitor) {
		super.doRun(progressMonitor);
		for (Iterator i = getStructuredSelection().iterator(); i.hasNext();) {
			final GraphicalEditPart ep = (GraphicalEditPart)i.next();
			OperationUtil.runInUndoInterval(new Runnable() {
				public void run() {
					try {
						OperationUtil.runAsWrite(new MRunnable() {
							public Object run() {
								EObjectUtil.destroy(ep.getPrimaryView().getElement());
								return null;
							}
						});
					} catch (MSLActionAbandonedException e) {
						e.printStackTrace();
					}
				}
			});
			
		}
	}

	protected boolean isSelectionListener() {
		return true;
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}
