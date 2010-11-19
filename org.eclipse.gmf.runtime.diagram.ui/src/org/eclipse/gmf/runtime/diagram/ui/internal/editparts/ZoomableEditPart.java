/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author sshaw
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
