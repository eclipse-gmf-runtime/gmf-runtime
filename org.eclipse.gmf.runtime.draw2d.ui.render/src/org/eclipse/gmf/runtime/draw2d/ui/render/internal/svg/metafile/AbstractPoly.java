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
 */
abstract class AbstractPoly implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath, ITraceMe
{
	private static final int	EMF_POINT_COUNT_OFFSET	= 16;
	private static final int	EMF_POINT_ARRAY_START	= 20;
	
	private static final int	WMF_POINT_COUNT_OFFSET	= 0;
	private static final int	WMF_POINT_ARRAY_START	= 2;

	private int					m_count = 0;
	private int[]				m_xPoints = null;
	private int[]				m_yPoints = null;
	private boolean				m_b16Bits = true;

	protected int[]				m_xPointsConv = null;
	protected int[]				m_yPointsConv = null;

	public AbstractPoly( boolean b16Bits )
	{
		m_b16Bits = b16Bits;
	}
	
	public void readWMFRecord( Record emr ) throws IOException
	{
		m_count = emr.getShortAt( WMF_POINT_COUNT_OFFSET );
		readPoly( emr, WMF_POINT_ARRAY_START );
	}

	public void readEMFRecord( Record emr ) throws IOException
	{
		m_count = emr.getIntAt( EMF_POINT_COUNT_OFFSET );
		readPoly( emr, EMF_POINT_ARRAY_START );
	}
	
	protected int getCount()
	{
		return m_count;
	}
	
	protected int[] getXPoints()
	{
		return m_xPoints;
	}
	
	protected int[] getYPoints()
	{
		return m_yPoints;
	}

	private boolean readPoly( Record emr, int pointOffset ) throws IOException
	{
		/*
		 * RECTL  rclBounds;	// Polygon boundary, in device units.
		 * DWORD  cptl; 		// number of points
		 * POINTL aptl[1]; 		// array of cptl points (each a pair of integers)
		 */
		
		m_xPoints = new int[ m_count ];
		m_yPoints = new int[ m_count ];
		
		int curPos = pointOffset;
		
		for( int i = 0; i < m_count; i++ )
		{
			// Read the points
			if( m_b16Bits )
			{
				m_xPoints[ i ] = emr.getShortAt( curPos );
				m_yPoints[ i ] = emr.getShortAt( curPos + 2 );
				curPos += 4;
			}
			else
			{	
				m_xPoints[ i ] = emr.getIntAt( curPos );
				m_yPoints[ i ] = emr.getIntAt( curPos + 4  );
				curPos += 8;
			}
		}
		 
		return true; 
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("cnt=" + m_count); //$NON-NLS-1$
		dumpPoints(sb, ", Converted:", m_xPointsConv, m_yPointsConv); //$NON-NLS-1$
		dumpPoints(sb, ", Original:", m_xPoints, m_yPoints); //$NON-NLS-1$
		return sb.toString();
	}

	private void dumpPoints(StringBuffer sb, String what, int[] x, int[] y) {
		sb.append(what);
		if (x == null || y == null) {
			sb.append(" (null)"); //$NON-NLS-1$
		} else {
			for (int ix = 0; ix < m_count; ++ix) {
				sb.append(" (" + x[ix] + ',' + y[ix] + ')'); //$NON-NLS-1$
			}
		}
	}
}
