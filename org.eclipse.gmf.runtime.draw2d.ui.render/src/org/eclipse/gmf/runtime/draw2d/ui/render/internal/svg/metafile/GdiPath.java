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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Vector;

/**
 * Represents a 'path'.  A path is a set of lines and curves that represent closed or open shapes.
 * Paths may be filled with a brush or stroked with a pen.  There is only ever one path
 * open on the system, it may be retrieved from the DeviceContext.
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class GdiPath
{
	private Vector			m_figures		= new Vector();
	private boolean			m_bPathIsOpen	= false;
	private DeviceContext	m_context		= null;
	private GeneralPath		m_curFigure		= null;

	/**
	 * Creates a new path.
	 * @param context Device context the path is associated with.  This is a 1-to-1 relationship.
	 */
	public GdiPath( DeviceContext context )
	{
		m_context = context;
	}
	
	/**
	 * Creates a copy of the specified path, associated with the specified context.
	 * @param context
	 * @param path
	 */
	GdiPath( DeviceContext context, GdiPath path )
	{
		m_bPathIsOpen = path.m_bPathIsOpen;
		m_context = context;
		
		for( int index = 0; index < path.m_figures.size(); index++ )
		{
			GeneralPath p = (GeneralPath) path.m_figures.get( index );
			
			GeneralPath newP = new GeneralPath( p );
			m_figures.add( newP );
			
			if( p == path.m_curFigure )
			{
				m_curFigure = newP;
			}
		}
	}
	
	/**
	 * Starts a new path.  Any existing path information is lost.
	 */
	public void begin()
	{
		m_figures.clear();
		newFigure();
		m_curFigure = null;
		m_bPathIsOpen = true;
	}
	
	/**
	 * Ends a path.
	 */
	public void end()
	{
		m_bPathIsOpen = false;
	}
	
	/**
	 * Aborts the current path.  All path information is lost.
	 */
	public void abort()
	{
		m_bPathIsOpen = false;
		m_curFigure = null;
		m_figures.clear();
	}
	
	/**
	 * @return true if a path has been begun, false otherwise.
	 */
	public boolean isOpen()
	{
		return m_bPathIsOpen;
	}
	
	/**
	 * Adds a figure to the path.
	 * @param s Figure to add to the path.
	 */
	public void appendFigure( GeneralPath s )
	{
		if( m_bPathIsOpen )
		{
			m_figures.add( s );
			m_curFigure = null;
		}
	}
	
	/**
	 * Adds a figure to the path.
	 * @param s Figure to add to the path.
	 */
	public void appendFigure( Shape s )
	{
		if( m_bPathIsOpen )
		{
			GeneralPath p = new GeneralPath();
			p.append( s, false );
			m_figures.add( p );
			m_curFigure = null;
		}
	}

	/**
	 * Closes any open figures. 
	 */
	public void closeAll()
	{
		// Iterate over all the figures and call 'closePath' on them.
		for( int index = 0; index < m_figures.size(); index++ )
		{
			GeneralPath gp = (GeneralPath) m_figures.get( index );
			
			if( gp != null )
			{	
				gp.closePath();
			}
		}
	}
	
	/**
	 * @return The current figure.
	 */
	public GeneralPath getCurrentFigure()
	{
		if( m_curFigure == null )
		{
			m_curFigure = newFigure();
		}

		return m_curFigure;
	}
	
	/**
	 * @return The 'conglomerate' path.  Appends all figures to a new GeneralPath.
	 */
	public GeneralPath getPath()
	{
		GeneralPath path = new GeneralPath();
		
		for( int index = 0; index < m_figures.size(); index++ )
		{
			path.append( (Shape) m_figures.get( index ), false );
		}
		
		return path;
	}
	
	/**
	 * Flattens the current path (makes rounded shapes squared off).
	 */
	public void flatten()
	{
		// Iterate over all the figures and flatten them
		for( int index = 0; index < m_figures.size(); index++ )
		{
			GeneralPath gp = (GeneralPath) m_figures.get( index );
			
			if( gp != null )
			{
				gp = flatten( gp );
				m_figures.remove( index );
				m_figures.insertElementAt( gp, index );
			}
		}
	}
	
	private GeneralPath newFigure()
	{
		GeneralPath gp = new GeneralPath();
		
		gp.moveTo( 	m_context.convertXToSVGLogicalUnits( m_context.getCurPosX() ),
					m_context.convertYToSVGLogicalUnits( m_context.getCurPosY() ) );
		m_figures.add( gp );

		return gp;
	}

	private GeneralPath flatten( GeneralPath gp )
	{
		PathIterator it = gp.getPathIterator( new AffineTransform() );
		FlatteningPathIterator flatIterator = new FlatteningPathIterator( it, 20 );
		
		GeneralPath retval = new GeneralPath();
		retval.append( flatIterator, false );
		
		return retval;
	}

}
