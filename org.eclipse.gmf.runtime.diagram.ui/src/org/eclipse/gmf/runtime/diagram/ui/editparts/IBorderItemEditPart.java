/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator;

/**
 * A border item editpart is added to the border item container of a
 * {@link org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderedShapeEditPart}.
 * The locator is used for setting the location of the border item and for
 * selection feedback.
 * 
 * @author cmahoney
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IBorderItemEditPart
	extends IGraphicalEditPart {

	/**
	 * Gets the locator used for this border item's figure. May return null.
	 * 
	 * @return the <code>IBorderItemLocator</code> or null
	 */
	public IBorderItemLocator getBorderItemLocator();

}
