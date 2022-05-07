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

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

public interface TransparentBorder extends Border {

	/**
	 * Method getTransparentInsets.
	 * This method allows a border to indicate which part of it's inset
	 * is transparent.  This is important so that if certain methods wish to
	 * ignore this part of the border they can. i.e. When placing resize
	 * handles around a shape.
	 * @param figure IFigure to calculate the insets of
	 * @return Insets object containing the border offsets which are 
	 * 		transparent.
	 */
	public Insets getTransparentInsets(IFigure figure);
}

