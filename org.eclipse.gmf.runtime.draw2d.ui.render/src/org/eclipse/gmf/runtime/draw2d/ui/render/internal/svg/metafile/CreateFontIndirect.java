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

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;


/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */

public class CreateFontIndirect implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private static final int	EMF_ID_OFFSET			= 0;
	private static final int	EMF_HEIGHT_OFFSET		= 4;
	private static final int	EMF_ESCAPEMENT_OFFSET	= 12;
//	private static final int	EMF_ORIENTATION_OFFSET	= 16;
	private static final int	EMF_WEIGHT_OFFSET		= 20;
	private static final int	EMF_ITALIC_OFFSET		= 24;
	private static final int	EMF_UNDERLINE_OFFSET	= 25;
	private static final int	EMF_STRIKEOUT_OFFSET	= 26;
	private static final int	EMF_FACE_OFFSET			= 32;
	private static final int 	EMF_LF_FACESIZE         = 32;

	private static final int	WMF_HEIGHT_OFFSET		= 0;
//	private static final int	WMF_ESCAPEMENT_OFFSET	= 4;
//	private static final int	WMF_ORIENTATION_OFFSET	= 6;
	private static final int	WMF_WEIGHT_OFFSET		= 8;
	private static final int	WMF_ITALIC_OFFSET		= 10;
	private static final int	WMF_UNDERLINE_OFFSET	= 11;
	private static final int	WMF_STRIKEOUT_OFFSET	= 12;
	private static final int	WMF_FACE_OFFSET			= 18;
	private static final int 	WMF_LF_FACESIZE         = 32;

	private int		m_id 			= -1;
	private int		m_height		= 10;
	private int 	m_escapement	= 0;
	private boolean	m_bItalic		= false;
	private boolean	m_bUnderlined	= false;
	private boolean	m_bStrikeout	= false;
	private	int		m_weight		= 400;
	private String	m_faceName		= "SanSerif";  //$NON-NLS-1$
	
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_height		= Math.abs( rec.getShortAt( WMF_HEIGHT_OFFSET ) );
		m_bItalic 		= rec.getByteAt( WMF_ITALIC_OFFSET ) != 0;
		m_bUnderlined	= rec.getByteAt( WMF_UNDERLINE_OFFSET ) != 0;
		m_bStrikeout	= rec.getByteAt( WMF_STRIKEOUT_OFFSET ) != 0;
		m_weight		= rec.getShortAt( WMF_WEIGHT_OFFSET );
		m_escapement	= rec.getShortAt( EMF_ESCAPEMENT_OFFSET );

		// read in the face name.
		byte[] facename = rec.getBytesAt( WMF_FACE_OFFSET, WMF_LF_FACESIZE );
		
		int index = 0;
		for( index = 0; index < facename.length; index++ )
		{
			if( facename[ index ] == 0 )
			{
				break;
			}
		}
		m_faceName = new String( facename, 0, index );
	}
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		m_id 			= (int) rec.getDWORDAt( EMF_ID_OFFSET );
		m_height		= Math.abs( rec.getIntAt( EMF_HEIGHT_OFFSET ) );
		m_bItalic 		= rec.getByteAt( EMF_ITALIC_OFFSET ) != 0;
		m_bUnderlined	= rec.getByteAt( EMF_UNDERLINE_OFFSET ) != 0;
		m_bStrikeout	= rec.getByteAt( EMF_STRIKEOUT_OFFSET ) != 0;
		m_weight		= rec.getIntAt( EMF_WEIGHT_OFFSET );
		m_escapement	= rec.getIntAt( EMF_ESCAPEMENT_OFFSET );

		// read in the face name.
		char[] facename	= rec.getCharsAt( EMF_FACE_OFFSET, EMF_LF_FACESIZE );
		int index = 0;
		for( index = 0; index < facename.length; index++ )
		{
			if( facename[ index ] == 0 )
			{
				break;
			}
		}
		m_faceName = new String( facename, 0, index );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( m_id == -1 )
		{	
			m_id 	= context.getNextObjectId();
		}

		int height = context.scaleY( m_height );
		context.addGDI( m_id, new GdiFont( 	height, 
											m_bItalic, 
											m_bUnderlined, 
											m_bStrikeout, 
											m_weight > 400, 
											m_faceName, 
											m_escapement ) );
	}
}
