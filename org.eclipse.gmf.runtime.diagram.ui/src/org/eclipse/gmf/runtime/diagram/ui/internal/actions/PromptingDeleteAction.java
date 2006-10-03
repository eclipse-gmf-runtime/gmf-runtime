/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPart;


/**
 * Delete Action originating via keyboard using the 'Delete' hot/shortcut key
 * 
 * @author bagrodia
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * Created on Jun 30, 2004
 */
public class PromptingDeleteAction
	extends DeleteAction {
	

	/**
	 * Constructs a <code>PromptingDeleteAction</code> using the specified part.
	 * @param part The part for this action
	 */
	public PromptingDeleteAction(IWorkbenchPart part) {
		super(part);
	}

	
	
	/**
	 * create a command for the passed list of objects
	 * @param objects objects to associate with the command
	 * @return <code>Command</code>
	 */
	
	public Command createCommand(List objects) {
		
		if(objects.isEmpty()) return null;
		
		
		/* Create the delete request */
		GroupRequestViaKeyboard deleteReq =
			new GroupRequestViaKeyboard(RequestConstants.REQ_DELETE);
		
		deleteReq.setShowInformationDialog(DiagramUIPlugin.getInstance()
			.getPreferenceStore().getBoolean(
				IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM));

		CompoundCommand deleteCC =
			new CompoundCommand(DiagramUIMessages.DeleteCommand_Label);


		deleteReq.setShowInformationDialog(false);
		boolean containsSemanticElement = false;
		CompositeTransactionalCommand command = null;
        TransactionalEditingDomain editingDomain = null;
		for (int i = 0; i < objects.size(); i++) {			
			if(objects.get(i) instanceof EditPart){
				/* Get the next part */
				EditPart editPart = (EditPart) objects.get(i);
				if (editPart instanceof IGraphicalEditPart){
                    if (editingDomain == null) {
                        editingDomain = ((IGraphicalEditPart) editPart).getEditingDomain();
                    }
					if (!containsSemanticElement&&
						ViewUtil.resolveSemanticElement((View)editPart.getModel()) != null){
						containsSemanticElement = true;
						deleteReq.setShowInformationDialog(true);
					}
				}

				/* Send the request to the edit part */
				Command command2 = editPart.getCommand(deleteReq);
				if (command2 != null && editingDomain != null) {
					if (command == null)
						command = new CompositeTransactionalCommand(editingDomain, command2.getLabel());
					command.compose(new CommandProxy(command2));
				}
			}
		}		
		
		
		if ((command != null)&&(command.size() > 0))
			deleteCC.add(new ICommandProxy(command));
		return deleteCC;
	}	
	
	
	
	/**
	 * This is by purpose set to true since there is no need to go to editpart 
	 * and obtain commands.
	 * @see org.eclipse.gef.ui.actions.DeleteAction#calculateEnabled()
	 * 
	 */
	protected boolean calculateEnabled() {		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.DeleteAction#run()
	 */
	public void run() {
		Command command = createCommand(getSelectedObjects());
		if (command != null)
			execute(command);
	}


}
