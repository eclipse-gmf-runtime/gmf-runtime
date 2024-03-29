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

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;


/**  
 * @author dhabib
 */
public class SetBkColor implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private Color m_color = new Color( 0x00ffffff );	// Default to white.
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_color = rec.getColorAt( 0 );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_color = rec.getColorAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setBackColor( m_color );
	}
}
