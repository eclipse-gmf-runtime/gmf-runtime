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
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;


/**  
 * @author dhabib
 */
public class MoveTo implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath, ITraceMe
{
	private static final int EMF_X_OFFSET	= 0;
	private static final int EMF_Y_OFFSET	= 4;
	private static final int WMF_X_OFFSET	= 2;
	private static final int WMF_Y_OFFSET	= 0;
	
	private int m_xPos	= 0;
	private int m_yPos	= 0;

	private int m_xPosConv	= -1;
	private int m_yPosConv	= -1;

	public void readWMFRecord( Record emr ) throws IOException
	{
		m_xPos = emr.getShortAt( WMF_X_OFFSET );
		m_yPos = emr.getShortAt( WMF_Y_OFFSET );
	}
	
	public void readEMFRecord( Record emr ) throws IOException
	{
		// First members of the structure are the x,y coordinates to move to.
		m_xPos = emr.getIntAt( EMF_X_OFFSET );
		m_yPos = emr.getIntAt( EMF_Y_OFFSET );
	}
	
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setCurPosX( m_xPos );
		context.setCurPosY( m_yPos );

		m_xPosConv = m_xPos;
		m_yPosConv = m_yPos;
	}

	public void render( DeviceContext context ) throws TranscoderException
	{
		GeneralPath p 	= context.getGdiPath().getCurrentFigure();
		m_xPosConv = context.convertXToSVGLogicalUnits( m_xPos );
		m_yPosConv = context.convertYToSVGLogicalUnits( m_yPos );
		p.moveTo( m_xPosConv, 
				  m_yPosConv );
		context.setCurPosX( m_xPos );
		context.setCurPosY( m_yPos );
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('(');
		sb.append(m_xPosConv);
		sb.append(',');
		sb.append(m_yPosConv);
		sb.append(')');
		return sb.toString();
	}
}