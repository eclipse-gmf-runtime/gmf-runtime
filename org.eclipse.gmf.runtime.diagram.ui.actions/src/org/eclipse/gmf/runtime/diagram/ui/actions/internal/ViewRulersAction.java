/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;


/**
 * 
 * Checked action for the View Ruler workspace property
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class ViewRulersAction extends PresentationAction {

	private static final String MENU_LABEL = "ViewRuler.textLabel"; //$NON-NLS-1$
	private static final String TOOLTIP_LABEL = "ViewRuler.toolTip"; //$NON-NLS-1$
	
	/**
	 * Create a View Ruler Action
	 * @param workbenchPage
	 */
	public ViewRulersAction(IWorkbenchPage workbenchPage) {
		
		super(workbenchPage);
		setText(DiagramActionsResourceManager.getI18NString(MENU_LABEL));
		setId(ActionIds.ACTION_VIEW_RULERS);
		setToolTipText(DiagramActionsResourceManager.getI18NString(TOOLTIP_LABEL));
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
	 * Calculates the enablement state of the action.  This action  is
	 * enabled if the diagram is selected. 
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
	 * WorkspaceViewerProperties.VIEWRULERS value.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		((DiagramGraphicalViewer) getDiagramGraphicalViewer())
					.getWorkspaceViewerPreferenceStore()
					.setValue(WorkspaceViewerProperties.VIEWRULERS, isChecked());		
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
				boolean shouldBeChecked = preferenceStore.getBoolean(WorkspaceViewerProperties.VIEWRULERS);
				this.setChecked(shouldBeChecked);
			}			
		}
	}
}
