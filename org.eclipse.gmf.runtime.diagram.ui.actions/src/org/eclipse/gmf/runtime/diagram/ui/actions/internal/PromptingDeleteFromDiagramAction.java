/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
import org.eclipse.gef.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Extends DeleteFromDiagramAction to ensure that preference for prompt on
 * delete from diagram is respected when using context menu delete from diagram option.
 * 
 * @author lgrahek
 * 
 */
public class PromptingDeleteFromDiagramAction extends DeleteFromDiagramAction {
	/**
	 * Constructs a new diagram action
	 * 
	 * @param workbenchPart
	 *            The workbench part associated with this action
	 */
	public PromptingDeleteFromDiagramAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Constructs a new diagram action
	 * 
	 * @param workbenchPage
	 *            The workbench page associated with this action
	 */
	public PromptingDeleteFromDiagramAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Creates the delete request that will allow taking into account the preference 
     * to show the information dialog.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.DeleteFromDiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		GroupRequestViaKeyboard deleteReq = new GroupRequestViaKeyboard(
				RequestConstants.REQ_DELETE);

		deleteReq.setShowInformationDialog(false);		
		return deleteReq;
	}

	/** First gets the delete command, where target request is modified to include the information about
	 * whether confirmation prompt should be issued. Then it runs the obtained command.
	 *  
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		Request req = getTargetRequest();
		boolean showInformationDialog = ((IPreferenceStore) getPreferencesHint()
				.getPreferenceStore())
				.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM);
		((GroupRequestViaKeyboard) req)
				.setShowInformationDialog(showInformationDialog);
		super.doRun(progressMonitor);
	}
		
	
}
