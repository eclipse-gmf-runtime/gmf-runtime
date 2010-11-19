/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**  
 * @author dhabib
 */
abstract class ObjectBase implements IEmf2SvgConverter, IWmf2SvgConverter
{
	protected static final int	ENHMETA_STOCK_OBJECT    = 0x80000000;
	protected static final int	WHITE_BRUSH         	= 0;
	protected static final int	LTGRAY_BRUSH        	= 1;
	protected static final int	GRAY_BRUSH          	= 2;
	protected static final int	DKGRAY_BRUSH        	= 3;
	protected static final int	BLACK_BRUSH         	= 4;
	protected static final int	NULL_BRUSH          	= 5;
	protected static final int	WHITE_PEN           	= 6;
	protected static final int	BLACK_PEN           	= 7;
	protected static final int	NULL_PEN            	= 8;
	protected static final int	OEM_FIXED_FONT      	= 10;
	protected static final int	ANSI_FIXED_FONT     	= 11;
	protected static final int	ANSI_VAR_FONT       	= 12;
	protected static final int	SYSTEM_FONT         	= 13;
	protected static final int	DEVICE_DEFAULT_FONT 	= 14;
	protected static final int	DEFAULT_PALETTE     	= 15;
	protected static final int	SYSTEM_FIXED_FONT   	= 16;
	
	private int m_id;
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_id = rec.getShortAt( 0 );		
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_id = (int) rec.getDWORDAt( 0 );		
	}

	public int getId()
	{
		return m_id;
	}

}
