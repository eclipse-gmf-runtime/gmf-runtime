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

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */

import java.awt.Graphics2D;
import java.awt.Point;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class SetViewportExtent extends AbstractSetExtentViewport 
{
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setViewportExtent( new Point( getX(), getY() ) );
	}
}
