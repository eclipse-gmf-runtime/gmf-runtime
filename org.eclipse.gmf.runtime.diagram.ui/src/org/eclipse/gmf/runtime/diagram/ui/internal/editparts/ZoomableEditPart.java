/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * Interface that abstracts the methods needed to perform zoom within a specific EditPart.
 * 
 */
public interface ZoomableEditPart {
	
	/**
	 * zoomOut
	 * Method will zoom out the EditPart to the next available level
	 */
	public void zoomOut();
	
	/**
	 * zoomIn
	 * Method will zoom in the EditPart to the next available level
	 */
	public void zoomIn();
	
	/**
	 * zoomOut
	 * Method will zoom out the EditPart to the next available level centered around a given point
	 * @param center Point around which the zoom will be centered in absolute coordinates
	 */
	public void zoomOut(Point center);
	
	/**
	 * zoomIn
	 * Method will zoom in the EditPart to the next available level centered around a given point
	 * @param center Point around which the zoom will be centered in absolute coordinates
	 */
	public void zoomIn(Point center);
	
	/**
	 * zoomTo
	 * This method allows implementators to zoom to a certain level centered around a given point.
	 * @param zoom double value where 1.0 represents 100%.
	 * @param center Point around which the zoom will be centered in absolute coordinates
	 */
	public void zoomTo(double zoom, Point center);
	
	/**
	 * zoomTo
	 * This method allows implementors to zoom into or out to a rectangular area.
	 * @param rect Rectangle that the edit part will zoom into our out to in absolute coordinates.
	 */
	public void zoomTo(Rectangle rect);
}
