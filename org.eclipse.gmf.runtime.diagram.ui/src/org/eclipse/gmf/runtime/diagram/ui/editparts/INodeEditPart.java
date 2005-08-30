/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
