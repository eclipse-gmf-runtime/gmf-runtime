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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectState;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * A command that destroys model elements.
 * 
 * @author khussey
 */
public class DestroyEObjectCommand
	extends AbstractModelCommand {

	/**
	 * The list of elements to be destroyed.
	 */
	private final List elements;

	/**
	 * Retrieves the value of the <code>elements</code> instance variable.
	 * 
	 * @return The value of the <code>elements</code> instance variable.
	 */
	protected List getElements() {
		return elements;
	}

	/**
	 * Constructs a new destroy element command with the specified label,
	 * model operation context, and element.
	 * 
	 * @param label The label for the new command.
	 * @param element The element to be destroyed.
	 */
	public DestroyEObjectCommand(String label, EObject element) {

		this(label, Arrays.asList(new EObject[] {element }));
	}

	/**
	 * Constructs a new destroy element command with the specified label,
	 * and list of elements.
	 * 
	 * @param label The label for the new command.
	 * @param elements The list of elements to be destroyed.
	 */
	public DestroyEObjectCommand(String label, List elements) {

		super(label, elements.isEmpty() ? null : (EObject) (elements.get(0)));

		this.elements = elements;
	}

	/**
	 * Executes this command by destroying the specified element(s).
	 * 
	 * @see AbstractCommand#doExecute(IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		for (Iterator i = getElements().iterator(); i.hasNext();) {
			EObject element = (EObject) i.next();

			MObjectState elementState = EObjectUtil.getState(element);
			if (MObjectState.DETACHED != elementState) {
				EObjectUtil.destroy(element);
			}
		}
		return newOKCommandResult();
	}

}
