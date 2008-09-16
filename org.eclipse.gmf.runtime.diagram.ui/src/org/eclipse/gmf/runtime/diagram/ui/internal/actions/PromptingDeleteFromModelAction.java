/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.DeleteFromModelAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;


/**
 * Delete Action originating via keyboard using the 'Ctrl+d' hot/shortcut key
 * as well as using context menu "Delete from model"
 * 
 * @author bagrodia
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * Created on Jun 30, 2004
 */
public class PromptingDeleteFromModelAction
	extends DeleteFromModelAction {	
	
	/**
	 * used to distinguish context menu calls from keyboard calls (the only difference is in calculating enablement) 
	 */
	private boolean calledFromContextMenu;

	/**
	 * Creates a <code>PromptingDeleteFromModelAction</code> with a default label.
	 *
	 * @param part The part this action will be associated with.
	 */
	public PromptingDeleteFromModelAction(IWorkbenchPart part) {
		super(part);
		calledFromContextMenu = false;
	}

	/**
	 * Creates a <code>PromptingDeleteFromModelAction</code> with a default label.
	 * @param workbenchPage The page this action will be associated with.
	 */
	public PromptingDeleteFromModelAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		calledFromContextMenu = false;
	}
	
	/**
	 * Creates a <code>PromptingDeleteFromModelAction</code> with a default label.
	 * @param workbenchPage The page this action will be associated with.
	 */
	public PromptingDeleteFromModelAction(IWorkbenchPage workbenchPage, boolean fromContextMenu) {
		super(workbenchPage);
		calledFromContextMenu = fromContextMenu;
	}	
	
	/**
	 * Calculates enablement of this action. 
	 * 
	 * @return <code>true</code> if call is made via keyboard, or from context menu and action should be enabled,
	 *         <code>false</code> otherwise
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (calledFromContextMenu) {
			// Need to calculate enablement.
			return super.calculateEnabled();
		} else {
			// for calls from keyboard, always return true for performance reasons
			return true; 
		}
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
					.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL);
		DestroyElementRequest targetRequest = (DestroyElementRequest)((EditCommandRequestWrapper)req).getEditCommandRequest();		
		targetRequest.setConfirm(showInformationDialog);

		Command command = getCommand();
		if ((command instanceof CompoundCommand)&&(((CompoundCommand)command).getChildren().length > 0)){
			CompositeTransactionalCommand compositeModelActionCommand = new CompositeTransactionalCommand(getEditingDomain(),
                getCommandLabel());
			CompoundCommand compoundCommand = (CompoundCommand)command;
			Iterator iterator = compoundCommand.getCommands().iterator();
			while (iterator.hasNext()){
				compositeModelActionCommand.compose(new CommandProxy((Command)iterator.next()));				
			}
			command = new ICommandProxy(compositeModelActionCommand); 
		}
		if (command != null)
			execute(command, progressMonitor);
	}
	
	/**
	 *  Return the semantic request to destroy the element
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {		
		TransactionalEditingDomain editingDomain = getEditingDomain();
        if (editingDomain != null) {
            DestroyElementRequest destroyRequest = new DestroyElementRequest(
                editingDomain, false);
            return new EditCommandRequestWrapper(destroyRequest);
        }
        return null;
	}


}
