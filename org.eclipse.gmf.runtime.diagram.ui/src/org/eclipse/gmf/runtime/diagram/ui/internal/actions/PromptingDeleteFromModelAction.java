/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;


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
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		
		boolean shouldPrompt = ((IPreferenceStore) getPreferencesHint()
			.getPreferenceStore())
			.getBoolean(IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL);
		
		DestroyElementRequest destroyRequest = new DestroyElementRequest(shouldPrompt);

		return 	new EditCommandRequestWrapper(destroyRequest);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		setTargetRequest(null);
		Command command = getCommand();
		if ((command instanceof CompoundCommand)&&(((CompoundCommand)command).getChildren().length > 0)){
			CompositeModelCommand compositeModelActionCommand = new CompositeModelCommand(
				getCommandLabel());
			CompoundCommand compoundCommand = (CompoundCommand)command;
			Iterator iterator = compoundCommand.getCommands().iterator();
			while (iterator.hasNext()){
				compositeModelActionCommand.compose(new XtoolsProxyCommand((Command)iterator.next()));				
			}
			command = new EtoolsProxyCommand(compositeModelActionCommand); 
		}
		if (command != null)
			execute(command, progressMonitor);
	}
	


}
