/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * @author dhabib
 */
public class SetMiterLimit implements IEmf2SvgConverter, ITraceMe
{
	private float m_miterLimit	= 10.0f;
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		// In all Microsoft documentation for SetMiterLimit this is defined as a
		// "float". However, in the actual file it is persisted as an integer!
		// So read an integer but then treat it as a float...
		m_miterLimit = rec.getIntAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setMiterLimit( m_miterLimit );
	}

	public String toString() {
		return Float.toString(m_miterLimit);
	}
}
