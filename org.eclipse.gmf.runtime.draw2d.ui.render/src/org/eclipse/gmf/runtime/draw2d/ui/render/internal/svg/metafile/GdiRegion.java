/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * Represents a selectable region.  Not currently implemented or used, this is just a placeholder.
 *  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class GdiRegion 
{
	private GeneralPath	m_curClipRegion = null;

	public GdiRegion( Shape shape )
	{
		if( shape != null )
		{	
			m_curClipRegion = new GeneralPath();
			m_curClipRegion.append( shape, false );
		}
	}
	
	public GdiRegion( GdiRegion clip )
	{
		if( clip.m_curClipRegion != null )
		{
			m_curClipRegion = (GeneralPath) clip.m_curClipRegion.clone();
		}
	}

	public void apply( Graphics2D g )
	{
		g.setClip( m_curClipRegion );
	}
}
