/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;

/**
 * This interface is implemented by IEmf2SvgConverter's that support rendering to a 'path'.
 * This is used for generating fillable paths (See Windows 'BeginPath'/'EndPath'/etc functions).
 * 
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public interface IRenderToPath
{
	/**
	 * Called to render the Converter's data into a path.
	 *  
	 * @param g GeneralPath to render this shape into.
	 * @param context Current device context.
	 * @throws TranscoderException Throws this exception if rendering cannot be completed for any reason.
	 */
	public void render( DeviceContext context ) throws TranscoderException;
}
