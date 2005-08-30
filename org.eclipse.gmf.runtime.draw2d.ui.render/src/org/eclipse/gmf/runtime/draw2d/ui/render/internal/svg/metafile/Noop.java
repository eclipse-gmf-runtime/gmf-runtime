/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**
 * No-op class.  This is used for converter records that do not seem to have any
 * visible effect on the output.
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
class Noop implements IEmf2SvgConverter, IWmf2SvgConverter
{
	public void readWMFRecord( Record rec ) throws IOException
	{
	    // Nothing to read.
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
	    // Nothing to read.
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
	    // Do nothing since this is a no-op.
	}
}
