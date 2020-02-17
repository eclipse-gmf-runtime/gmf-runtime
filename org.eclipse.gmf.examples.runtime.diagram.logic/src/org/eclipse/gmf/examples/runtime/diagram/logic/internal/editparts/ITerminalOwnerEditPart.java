/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import java.util.Map;

import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;


/**
 * @author qili / sshaw
 *
 * The interface for logic shapes that own connection points.
 */
public interface ITerminalOwnerEditPart {
	
	/**
	 * Create connection points position map on the given editpart.
	 * @return connection points position map
	 */
	public Map createBoundsMap();
	
	/**
	 * Method for returning the owned terminal figure based on an ID value.
	 * 
	 * @param terminalEP <code>Terminal</code> to create a figure of
	 * @return <code>NodeFigure</code> that is the figure for the terminal.
	 */
	public NodeFigure createOwnedTerminalFigure(TerminalEditPart terminalEP);

}
