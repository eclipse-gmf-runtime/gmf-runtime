/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;

/**
 * This command is there to reposition elements in a list.
 * 
 * @author tisrar
 */
public class RepositionEObjectCommand
	extends AbstractModelCommand {

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
	 * @param label label for command
	 * @param elements the list of elements in which reposition will take place
	 * @param element target element
	 * @param displacement amount of movement
	 */
	public RepositionEObjectCommand(String label, EList elements, EObject element,
		int displacement) {

		super(label, element);
		this.element = element;
		this.displacement = displacement;
		this.elements = elements;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		CommandResult commandResult = null;

		int currentPosition = elements.indexOf(element);
		
		elements.move(currentPosition + displacement, element);

		return (commandResult == null) ? newCancelledCommandResult()
			: commandResult;
	}

}
