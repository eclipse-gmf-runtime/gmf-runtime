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

package org.eclipse.gmf.runtime.diagram.ui.render.internal.editparts;

import org.eclipse.gmf.runtime.diagram.ui.render.editparts.AbstractImageEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 * Concrete subclass of AbstractImageEditPart for images that are based on a
 * stored buffer instead of a file.
 */
public class BufferedImageEditPart
	extends AbstractImageEditPart {

	/**
	 * Default constructor
	 * 
	 * @param view
	 *            IShapeView that the EditPart is controlling
	 */
	public BufferedImageEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.AbstractImageEditPart#regenerateImageFromSource()
	 */
	public RenderedImage regenerateImageFromSource() {
		// TODO Auto-generated method stub
		return null;
	}
}