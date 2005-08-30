/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;

/**
 * Checked action for the View Page Breaks workspace property
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class ViewPageBreaksAction extends PresentationAction {

	/**
	 * @param workbenchPage
	 */
	public ViewPageBreaksAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		setText(DiagramActionsResourceManager.getI18NString("ViewPageBreaks.textLabel")); //$NON-NLS-1$
		setId(ActionIds.ACTION_VIEW_PAGEBREAKS);
		setToolTipText(DiagramActionsResourceManager.getI18NString("ViewPageBreaks.toolTip")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_VIEWPAGEBREAKS);
		setDisabledImageDescriptor(Images.DESC_ACTION_VIEWPAGEBREAKS_DISABLED);
	}

	/**
	 * Returns null, this action modifies the user's workspace preference.
	 * @returns null
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return null;
	}
	
	/**
	 * Sets the action style to AS_CHECK_BOX
	 */
	public int getStyle() {
		return AS_CHECK_BOX;
	}
	
	/**
	 * Calculates the enblement state of the action.  This action  is
	 * enabled of the diagram is selected. 
	 * @return <code>true</code> if the diagram is selected
	 */
	protected boolean calculateEnabled() {
		if ( getDiagramGraphicalViewer() == null ) {
			return false;
		}
		return true; 
	}
	
	/**
	 * Does not execute a Command.  Updates the workspace preference store's
	 * WorkspaceViewerProperties.VIEWPAGEBREAKS value.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		((DiagramGraphicalViewer) getDiagramGraphicalViewer())
					.getWorkspaceViewerPreferenceStore()
					.setValue(WorkspaceViewerProperties.VIEWPAGEBREAKS, isChecked());		
	}
	 
	/**
	 * This action is interested in selection
	 * @return true
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	public boolean isSelectionListener() {
		return true;
	}
	
	/**
	 * Override setWorkbenchPart to reset checked value based on
	 * preference store of currently selected IDiagramWorkbenchPart.
	 * Sets the current workbencgPart
	 * 
	 * @param workbenchPart
	 *            The current workbenchPart
	 */
	protected void setWorkbenchPart(IWorkbenchPart workbenchPart) {
		super.setWorkbenchPart(workbenchPart);
		
		if ( (workbenchPart != null) && (isSelectionListener())){
			
			if (workbenchPart instanceof IDiagramWorkbenchPart){
				IDiagramGraphicalViewer viewer = ((IDiagramWorkbenchPart)workbenchPart).getDiagramGraphicalViewer();
				IPreferenceStore preferenceStore = ((DiagramGraphicalViewer)viewer).getWorkspaceViewerPreferenceStore();
				boolean shouldBeChecked = preferenceStore.getBoolean(WorkspaceViewerProperties.VIEWPAGEBREAKS);
				this.setChecked(shouldBeChecked);
			}			
		}
	}
	
}
