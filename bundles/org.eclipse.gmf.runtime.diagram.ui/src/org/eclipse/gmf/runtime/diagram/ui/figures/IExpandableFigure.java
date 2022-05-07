/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An Expandable Figure.
 * 
 * @since 1.4
 * @author Anthony Hunter
 */
public interface IExpandableFigure {

	/**
	 * Returns the smallest rectangle completely enclosing the IFigure and its
	 * border children figures. Implementation may return the Rectangle by
	 * reference. For this reason, callers of this method must not modify the
	 * returned Rectangle. The Rectangle's values may change in the future.
	 * 
	 * @return This IExpandableFigure bounds that include border items.
	 */
	Rectangle getExtendedBounds();

}
