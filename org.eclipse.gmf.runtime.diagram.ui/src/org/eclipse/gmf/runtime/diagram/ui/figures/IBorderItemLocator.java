/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Controls the location of border items around another figure.
 * 
 * An <code>IBorderItemLocator</code> is used in conjunction with
 * {@link org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy}
 * for feedback when moving border items and with
 * {@link org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderItemEditPart}
 * when refreshing the bounds of the border item figure.
 * 
 * @author cmahoney
 */
public interface IBorderItemLocator
	extends Locator {

	/**
	 * Sets the constraint for the location of this border item.
	 * 
	 * @param constraint
	 *            the position and optionally size
	 */
	public void setConstraint(Rectangle constraint);

	/**
	 * Returns a suitable location for the border item given a proposed
	 * location. By implementing this method, the feedback shown when the user
	 * moves a border item can reflect where the locator will actually place the
	 * border item.
	 * <p>
	 * For example, if the border item is restricted to being on the border of
	 * its parent shape, when the user attempts to move the border item outside
	 * the border of the parent shape (the proposed location), the feedback will
	 * always show the border item on the border. In this case, this method
	 * would return a location on the border close to the proposed location.
	 * </p>
	 * 
	 * @param proposedLocation
	 *            a proposed location and optionally size
	 * @param borderItem
	 *            the border item in question
	 * @return a rectangle containing the valid location
	 */
	public Rectangle getValidLocation(Rectangle proposedLocation,
			IFigure borderItem);
	
	/**
	 * Returns the side of the parent figure on which the border item is
	 * currently on.
	 * 
	 * @return the side on which this border item appears as defined in
	 *         {@link PositionConstants}
	 */
	public int getCurrentSideOfParent();
}
