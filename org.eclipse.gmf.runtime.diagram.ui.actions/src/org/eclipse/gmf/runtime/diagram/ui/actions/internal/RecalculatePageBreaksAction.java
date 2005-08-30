/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.RecalculatePageBreaksRequest;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;

/**
 * Workspace Action to recalculate the page breaks.  This action is visible 
 * only when the pagebreaks are visible.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class RecalculatePageBreaksAction extends PresentationAction {

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
		setText(DiagramActionsResourceManager.getI18NString("RecalcPageBreaks.textLabel")); //$NON-NLS-1$
		setId(ActionIds.ACTION_RECALC_PAGEBREAKS);
		setToolTipText(DiagramActionsResourceManager.getI18NString("RecalcPageBreaks.toolTip")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTON_RECALCPAGEBREAKS);	
		setDisabledImageDescriptor(Images.DESC_ACTON_RECALCPAGEBREAKS_DISABLED);	
	}

	/**
	 *  (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
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
