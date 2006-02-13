/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * A view creation command that creates a <code>View</code>.
 * After execution, the command result is a singleton list containing 
 * an <code>IAdatable</code> object that adapts to <code>View</code>.
 * Before execution, after undo or after redo, the returned <code>IAdaptable</code>
 * adapts to nothing (will return <code>null</code> when adapted to <code>View</code>)
 */
/*
 * @canBeSeenBy %partners
 */
public class CreateCommand extends AbstractTransactionalCommand {
	
	/** the view descriptor */
	protected final CreateViewRequest.ViewDescriptor viewDescriptor;
	/** The container view */
	protected final View containerView;
	
	/**
	 * Creates a new CreateCommand
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param viewDescriptor the view descriptor associated with this command
	 * @param containerView the view that will containe the new view
	 */
	public CreateCommand(TransactionalEditingDomain editingDomain, 
		CreateViewRequest.ViewDescriptor viewDescriptor,
		View containerView) {

		super(editingDomain, DiagramUIMessages.CreateCommand_Label, getWorkspaceFiles(containerView)); 

		Assert.isNotNull(viewDescriptor);
		Assert.isNotNull(containerView);
		
		this.viewDescriptor = viewDescriptor;
		this.containerView = containerView;
				
		// make sure the return object is available even before executing/undoing/redoing
		setResult(CommandResult.newOKCommandResult(viewDescriptor));
	}

	/**
     * Return the cached view descriprot.
     * 
     * @return view descriprot
     */
	protected CreateViewRequest.ViewDescriptor getViewDescriptor() {
		return viewDescriptor;
	}


	/**
	 * give access to the view, where that will contain the created view
	 * @return the conatiner view
	 */
	protected View getContainerView() {
		return containerView;
	}

    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException {

		View view =
			ViewService.getInstance().createView(
				viewDescriptor.getViewKind(),
				viewDescriptor.getElementAdapter(),
				containerView,
				viewDescriptor.getSemanticHint(),
				viewDescriptor.getIndex(),
				viewDescriptor.isPersisted(),
				viewDescriptor.getPreferencesHint());
		Assert.isNotNull(view, "failed to create a view"); //$NON-NLS-1$
		viewDescriptor.setView(view);
        
        return CommandResult.newOKCommandResult(viewDescriptor);
	}

    public boolean canExecute() {
		return ViewService.getInstance().provides(
			viewDescriptor.getViewKind(),
			viewDescriptor.getElementAdapter(),
			containerView,
			viewDescriptor.getSemanticHint(),
			viewDescriptor.getIndex(),
			viewDescriptor.isPersisted(),
			viewDescriptor.getPreferencesHint());
	}
	
	/**
	 * returns true if the view that will be created will be a transient view, transient views
	 * will not dirty the model and will never get serialized in the saved file
	 * @return true if persisted false if transient
	 */
	public boolean isPersisted() {
	 	return getViewDescriptor().isPersisted(); 
	 }
    
    public List getAffectedFiles() {
        if (isPersisted())
            return super.getAffectedFiles();
        else
            return Collections.EMPTY_LIST;
    }
}
