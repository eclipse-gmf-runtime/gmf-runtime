/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

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
