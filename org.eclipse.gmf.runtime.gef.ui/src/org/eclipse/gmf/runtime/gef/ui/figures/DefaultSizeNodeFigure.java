/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.figures;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * A node figure that ensures a minimum preferred size (called the default size)
 * Extend this class if your node figure wants to ensure a minimum preferred size
 * regardless of what the layout manager is calculating
 * 
 * @author melaasar
 */
public class DefaultSizeNodeFigure extends NodeFigure {

	private Dimension defaultSize = new Dimension();
	
	/**
	 * Constructor
	 * 
	 * @param defSize a <code>Dimension</code> that is used to initialize the default size
	 */
	public DefaultSizeNodeFigure(Dimension defSize) {
		this(defSize.width, defSize.height);
	}

	/**
	 * Constructor
	 * 
	 * @param width the initial width to initialize the default size with
	 * @param height the initial height to initialize the default size with
	 */
	public DefaultSizeNodeFigure(int width, int height) {
		setDefaultSize(width, height);
	}

	/**
	 * @return a <code>Dimension</code> that represents the minimum or default size of 
	 * this figure.
	 */
	public Dimension getDefaultSize() {
		return defaultSize;
	}

	/**
	 * @param d The <code>Dimension</code> to set that represents the minimum or default size of 
	 * this figure.
	 */
	public void setDefaultSize(Dimension d) {
		setDefaultSize(d.width, d.height);
	}

	/**
	 * @param width the initial width to initialize the default size with
	 * @param height the initial height to initialize the default size with
	 */
	public void setDefaultSize(int width, int height) {
		this.defaultSize.width = width;
		this.defaultSize.height = height;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return super.getPreferredSize(wHint, hHint).getUnioned(
			getDefaultSize());
	}

}
