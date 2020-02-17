/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

/**
 * This command is there to reposition elements in a list.
 * 
 * @author tisrar
 */
public class RepositionEObjectCommand
	extends AbstractTransactionalCommand {

	/**
	 * the element to operate on
	 */
	private EObject element;

	/**
	 * the amount to move element by relative to its position
	 */
	private int displacement;

	/**
	 * The list of elements in which reposition will take place.
	 */
	private EList elements;

	/**
	 * Constructs a runtime instance of <code>RepositionEObjectCommand</code>.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label label for command
	 * @param elements the list of elements in which reposition will take place
	 * @param element target element
	 * @param displacement amount of movement
	 */
	public RepositionEObjectCommand(TransactionalEditingDomain editingDomain, String label, EList elements, EObject element,
		int displacement) {

		super(editingDomain, label, getWorkspaceFiles(element));
		this.element = element;
		this.displacement = displacement;
		this.elements = elements;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
		CommandResult commandResult = null;
		try {
    		int currentPosition = elements.indexOf(element);
    		elements.move(currentPosition + displacement, element);
        }catch (RuntimeException exp){
            commandResult = CommandResult.newErrorCommandResult(exp);
        }
		return (commandResult == null) ? CommandResult.newOKCommandResult()
			: commandResult;
	}

}
