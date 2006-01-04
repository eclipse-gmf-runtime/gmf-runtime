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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;


/**
 * An editpart with support for border items. It has the main shape's figure and
 * a special figure to hold the border item figures.
 * 
 * @author cmahoney
 */
public interface IBorderedShapeEditPart {

	/**
	 * Returns the editpart's main figure.
	 * 
	 * @return <code>IFigure</code>
	 */
	public IFigure getMainFigure();

	/**
	 * Return the editpart's bordered figure.
	 * 
	 * @return <code>IFigure</code>
	 */
	public BorderedNodeFigure getBorderedFigure();
}
