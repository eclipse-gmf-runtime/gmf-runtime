/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * Checked action for the Snap to Grid workspace property
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class SnapToGridAction extends DiagramAction {
	
	/**
	 * Create a Snap To Grid Action
	 * @param workbenchPage
	 */
	public SnapToGridAction(IWorkbenchPage workbenchPage) {
		
		super(workbenchPage);
		setText(DiagramUIActionsMessages.SnapToGrid_textLabel);
		setId(ActionIds.ACTION_SNAP_TO_GRID);
		setToolTipText(DiagramUIActionsMessages.SnapToGrid_toolTip);
	}

	/**
	 * Returns null, this action modifies the user's workspace preference.
	 * @returns null
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
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
	 * WorkspaceViewerProperties.SNAPTOGRID value.
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		((DiagramGraphicalViewer) getDiagramGraphicalViewer())
					.getWorkspaceViewerPreferenceStore()
					.setValue(WorkspaceViewerProperties.SNAPTOGRID, isChecked());		
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
			// set checked to 'false' if the IDiagramWorkbenchPart doesn't have a
			// graphical viewer or the viewer doesn't have a preference store
			boolean shouldBeChecked = false;
			
			if (workbenchPart instanceof IDiagramWorkbenchPart){
				IDiagramGraphicalViewer viewer = ((IDiagramWorkbenchPart)workbenchPart).getDiagramGraphicalViewer();
				
				if (viewer != null) {
					IPreferenceStore preferenceStore = ((DiagramGraphicalViewer)viewer).getWorkspaceViewerPreferenceStore();
				
					if (preferenceStore != null) {
						shouldBeChecked = preferenceStore.getBoolean(WorkspaceViewerProperties.SNAPTOGRID);
					}
				}
				this.setChecked(shouldBeChecked);
			}			
		}
	}
}
