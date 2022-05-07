/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.NodeEditPart;



/**
 * @author melaasar
 *
 * An interface for all connectable editparts
 */
public interface INodeEditPart 
	extends NodeEditPart, INotableEditPart {

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
