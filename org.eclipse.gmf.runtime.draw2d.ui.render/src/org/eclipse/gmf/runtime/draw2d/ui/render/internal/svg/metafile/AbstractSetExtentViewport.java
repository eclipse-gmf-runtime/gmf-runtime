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

import java.io.IOException;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
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
