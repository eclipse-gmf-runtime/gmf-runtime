/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import java.util.Map;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
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
	 * @param terminal <code>Terminal</code> to create a figure of
	 * @return <code>NodeFigure</code> that is the figure for the terminal.
	 */
	public NodeFigure createOwnedTerminalFigure(Terminal terminal);

}
