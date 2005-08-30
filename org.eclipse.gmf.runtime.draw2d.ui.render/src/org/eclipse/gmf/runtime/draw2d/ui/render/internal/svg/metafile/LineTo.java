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

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class LineTo implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath
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
		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{
			curPen.apply( g, context );

			m_xPosConv = context.convertXToSVGLogicalUnits( m_xPos );
			m_yPosConv = context.convertYToSVGLogicalUnits( m_yPos );

			g.drawLine(	context.convertXToSVGLogicalUnits( context.getCurPosX() ), 
						context.convertYToSVGLogicalUnits( context.getCurPosY() ), 
						m_xPosConv, 
						m_yPosConv );
		}
				
		context.setCurPosX( m_xPos );
		context.setCurPosY( m_yPos );
	}

	public void render( DeviceContext context ) throws TranscoderException
	{
		GeneralPath p 	= context.getGdiPath().getCurrentFigure();

		m_xPosConv = context.convertXToSVGLogicalUnits( m_xPos );
		m_yPosConv = context.convertYToSVGLogicalUnits( m_yPos );

		p.lineTo( 	m_xPosConv , 
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
