/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

/**
 * This is used to force the drawing of lines to be suppressed.  When the metafile
 * specifies a pen type of 'PS_NULL', this is used.
 * 
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
class NullStroke implements Stroke
{

	/* (non-Javadoc)
	 * @see java.awt.Stroke#createStrokedShape(java.awt.Shape)
	 */
	public Shape createStrokedShape(Shape p)
	{
		// Return an empty shape so nothing gets drawn.
		return new GeneralPath();
	}

}
