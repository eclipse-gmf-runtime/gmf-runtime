/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import org.apache.batik.transcoder.TranscoderException;

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
