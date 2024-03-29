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

import java.io.IOException;

/**  
 * @author dhabib
 */
abstract class AbstractSetExtentViewport implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private static final int EMF_X_OFFSET	= 0;
	private static final int EMF_Y_OFFSET	= 4;
	
	private static final int WMF_X_OFFSET	= 2;
	private static final int WMF_Y_OFFSET	= 0;

	private int m_xExt = 0;
	private int m_yExt = 0;
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_xExt = rec.getShortAt( WMF_X_OFFSET );		
		m_yExt = rec.getShortAt( WMF_Y_OFFSET );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_xExt = rec.getIntAt( EMF_X_OFFSET );		
		m_yExt = rec.getIntAt( EMF_Y_OFFSET );		
	}
	
	protected int getX()
	{
		return m_xExt;
	}
	
	protected int getY()
	{
		return m_yExt;
	}

}
