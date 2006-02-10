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
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class SetBkMode implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private int 	m_bkMode = 0;

	public void readWMFRecord( Record rec ) throws IOException
	{
		m_bkMode = rec.getShortAt( 0 );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_bkMode = rec.getIntAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setBkMode( m_bkMode );
	}
}
