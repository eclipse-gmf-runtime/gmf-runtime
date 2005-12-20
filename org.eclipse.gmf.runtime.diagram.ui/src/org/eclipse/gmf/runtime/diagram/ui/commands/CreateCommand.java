/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CMValidator;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
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
public class CreateCommand extends AbstractModelCommand {

	// Create a custom validator which considers persistance.
	private class CreateValidator extends CMValidator {
		public boolean okToEdit(ICommand command) {
			return ((CreateCommand) command).isPersisted() 
			? super.okToEdit(command)
				: true;

		}
	}
	
	/** the view descriptor */
	protected final CreateViewRequest.ViewDescriptor viewDescriptor;
	/** The container view */
	protected final View containerView;
	
	/**
	 * Creates a new CreateCommand
	 * @param viewDescriptor the view descriptor associated with this command
	 * @param containerView the view that will containe the new view
	 */
	public CreateCommand(
		CreateViewRequest.ViewDescriptor viewDescriptor,
		View containerView) {

		super(DiagramUIMessages.CreateCommand_Label,  containerView); 

		Assert.isNotNull(viewDescriptor);
		Assert.isNotNull(containerView);
		
		this.viewDescriptor = viewDescriptor;
		this.containerView = containerView;
				
		// make sure the return object is available even before executing/undoing/redoing
		setResult(newOKCommandResult(viewDescriptor));
	}

	/**
	 * Return the cached view descriprot.
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


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
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
		return newOKCommandResult(viewDescriptor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
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
	 
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getValidator()
	 */
	public CMValidator getValidator() {
		return new CreateValidator();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		if (isPersisted())
			return super.getAffectedObjects();
		else
			return Collections.EMPTY_LIST;
	}
}
