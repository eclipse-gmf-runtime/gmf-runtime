/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;

/**
 * Interface class for figures that are anchorable to other figures.
 * @author sshaw
 */
public interface IAnchorableFigure {
	/**
	 * Given a string identifier, return the associated anchor for that identifier
	 * 
	 * @param terminal <code>String</code> identifier associated with the anchor
	 * @return <code>ConnectionAnchor</code> that is associated with the given string.
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal);

	/**
	 * Dynamically allocates a new anchor if needed.  Otherwise, recycles old anchors
	 * no longer in use.
	 * 
	 * @param c the <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure
	 * @return a <code>String</code> that represents the anchor identifier.
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c);

	/** 
	 * Gets the source connection anchor at a given point on the figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return a <code>ConnectionAnchor</code> reference to an anchor associated with the given 
	 * point on the figure.
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p);

	/** 
	 * Gets the target connection anchor at a given point on the figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure.
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p);
}
