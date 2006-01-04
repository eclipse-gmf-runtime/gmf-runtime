/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
