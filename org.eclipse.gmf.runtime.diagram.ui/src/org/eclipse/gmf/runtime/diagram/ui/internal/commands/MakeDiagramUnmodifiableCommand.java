/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;


/**
 * <p>
 * Performs whatever operations are necessary to make a diagram unmodifiable.
 * </p>
 * <p>
 * <LI>Parts should not be movable</LI>
 * <LI>Parts with compartments should not have the capability to expand/collapse the compartment</LI>
 * <LI>Parts should not allow a user to modify the name of the underlying semantic elements</LI>
 * <LI>The diagram and its parts should not produce any usable action bars</LI>
 * </p>
 * 
 * @author cmcgee
 */
public class MakeDiagramUnmodifiableCommand
	extends Command {
	private DiagramEditPart diagramPart;
	private boolean modify;
	
	/**
	 * Constructs a command to make a diagram unmodifyable or modifyable.
	 * 
	 * @param part The diagram edit part for the diagram.
	 * @param modify true/false whether the diagram should be modifyable.
	 */
	public MakeDiagramUnmodifiableCommand(DiagramEditPart part, boolean modify) {
		diagramPart = part;
		this.modify = modify;
	}
	
	public void execute() {
		setModifiable(diagramPart,modify);
		diagramPart = null;//for garbage collection
	}
	
	/**
	 * Sets a diagram edit part as modifiable or not.
	 * 
	 * @param part A valid diagram edit part.
	 * @param modify true/false whether the diagram should be modifyable.
	 */
	public static void setModifiable(DiagramEditPart part ,boolean modify) {
		if (modify) {
			part.enableEditMode();
		} else {
			part.disableEditMode();
		}
	} 
}
