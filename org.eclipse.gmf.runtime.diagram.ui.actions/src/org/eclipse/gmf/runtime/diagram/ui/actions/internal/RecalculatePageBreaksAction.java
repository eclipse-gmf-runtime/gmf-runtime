/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.RecalculatePageBreaksRequest;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Workspace Action to recalculate the page breaks.  This action is visible 
 * only when the pagebreaks are visible.
 * 
 * @author jcorchis
 */
public class RecalculatePageBreaksAction extends DiagramAction {

	/**
	 * @param workbenchPage
	 */
	public RecalculatePageBreaksAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);	
	}
	
	public RecalculatePageBreaksAction(IWorkbenchPart part) {
		super(part);
	}	
	
	public void init() {
		super.init();
		setText(DiagramUIActionsMessages.RecalcPageBreaks_textLabel);
		setId(ActionIds.ACTION_RECALC_PAGEBREAKS);
		setToolTipText(DiagramUIActionsMessages.RecalcPageBreaks_toolTip);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_RECALCPAGEBREAKS);
		setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_RECALCPAGEBREAKS_DISABLED);
	}

	/**
	 *  (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new RecalculatePageBreaksRequest();
	}
	
	/**
	 * Calculates the enblement state of the action
	 * 
	 * @return <code>true</code> if PageBreaks are being viewed
	 */
	protected boolean calculateEnabled() {
		if (getDiagramGraphicalViewer() instanceof DiagramGraphicalViewer) {
			return ((DiagramGraphicalViewer) getDiagramGraphicalViewer())
				.getWorkspaceViewerPreferenceStore().getBoolean(
					WorkspaceViewerProperties.VIEWPAGEBREAKS);
		}
		return false;
	}	
	
	/**
	 * Execute the request via the preformRequest() call.  This action does
	 * not modify the model and does not use the request/command infrastructure.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		((DiagramRootEditPart)getDiagramEditPart().getRoot()).performRequest(getTargetRequest());
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return false;
	}

}
