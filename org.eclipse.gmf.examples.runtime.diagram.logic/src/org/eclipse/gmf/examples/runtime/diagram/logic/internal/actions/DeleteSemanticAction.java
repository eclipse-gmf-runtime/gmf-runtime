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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gef.Request;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramDebugOptions;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.ui.IWorkbenchPage;

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
	extends DiagramAction
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
		
        AbstractEMFOperation operation = new AbstractEMFOperation(
			getDiagramEditPart().getEditingDomain(), getLabel()) {

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {

				for (Iterator i = getStructuredSelection().iterator(); i
					.hasNext();) {
					IGraphicalEditPart ep = (IGraphicalEditPart) i.next();
					EMFCoreUtil.destroy(ep.getPrimaryView().getElement());

				}
				return new Status(IStatus.OK, LogicDiagramPlugin.getPluginId(),
					DiagramStatusCodes.OK, StringStatics.BLANK, null);
			};
		};

		try {
			getActionManager().getOperationHistory().execute(operation,
				new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			Trace.catching(LogicDiagramPlugin.getInstance(),
				LogicDiagramDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"doRun", e); //$NON-NLS-1$
			Log.error(LogicDiagramPlugin.getInstance(),
				LogicDiagramStatusCodes.IGNORED_EXCEPTION_WARNING, e
					.getLocalizedMessage(), e);
		}
		
	}

	protected boolean isSelectionListener() {
		return true;
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}
