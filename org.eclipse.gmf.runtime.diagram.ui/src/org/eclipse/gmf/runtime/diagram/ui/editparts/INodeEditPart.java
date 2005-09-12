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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.NodeEditPart;

import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.INoteableEditPart;


/**
 * @author melaasar
 *
 * An interface for all connectable editparts
 */
public interface INodeEditPart 
	extends NodeEditPart, INoteableEditPart {

	/**
	 * Method mapConnectionAnchorToTerminal.
	 * @param c
	 * @return Anchor
	 */ 
	public String mapConnectionAnchorToTerminal(ConnectionAnchor c);

	/**
	 * Method mapTerminalToConnectionAnchor.
	 * @param terminal
	 * @return ConnectionAnchor
	 */
	public ConnectionAnchor mapTerminalToConnectionAnchor(String terminal);
	
}
