/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;


/**
 * Delete Action originating via keyboard using the 'Ctrl+d' hot/shortcut key
 * 
 * @author bagrodia
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * Created on Jun 30, 2004
 */
public class PromptingDeleteFromModelAction
	extends DeleteFromModelAction {	
	

	/**
	 * Creates a <code>PromptingDeleteFromModelAction</code> with a default label.
	 *
	 * @param part The part this action will be associated with.
	 */
	public PromptingDeleteFromModelAction(IWorkbenchPart part) {
		super(part);		
	}

	/**
	 * Creates a <code>PromptingDeleteFromModelAction</code> with a default label.
	 * @param workbenchPage The page this action will be associated with.
	 */
	public PromptingDeleteFromModelAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);		
	}
	
	
	/**
	 *  Return the semantic request to destroy the element
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		
		boolean shouldPrompt = ((IPreferenceStore) getPreferencesHint()
			.getPreferenceStore())
			.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL);
        
		TransactionalEditingDomain editingDomain = getEditingDomain();
        if (editingDomain != null) {
            DestroyElementRequest destroyRequest = new DestroyElementRequest(
                editingDomain, shouldPrompt);
            return new EditCommandRequestWrapper(destroyRequest);
        }
        return null;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		setTargetRequest(null);
		Command command = getCommand();
		if ((command instanceof CompoundCommand)&&(((CompoundCommand)command).getChildren().length > 0)){
			CompositeTransactionalCommand compositeModelActionCommand = new CompositeTransactionalCommand(getEditingDomain(),
                getCommandLabel());
			CompoundCommand compoundCommand = (CompoundCommand)command;
			Iterator iterator = compoundCommand.getCommands().iterator();
			while (iterator.hasNext()){
				compositeModelActionCommand.compose(new CommandProxy((Command)iterator.next()));				
			}
			command = new EtoolsProxyCommand(compositeModelActionCommand); 
		}
		if (command != null)
			execute(command, progressMonitor);
	}
	


}
