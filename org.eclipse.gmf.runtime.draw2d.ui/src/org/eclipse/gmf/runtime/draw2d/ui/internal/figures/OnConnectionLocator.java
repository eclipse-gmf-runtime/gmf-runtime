/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;


/**
 * This locator places the figure on the connector, a specified 
 * percentage of the connector length away from the source end.
 * 
 * @author chmahone
 */
public class OnConnectionLocator extends ConnectionLocator {

	/** percentage away from source end */
	private int percentageFromSource;

	/**
	 * Constructor for <code>OnConnectionLocator</code>.
	 * @param connection the parent <code>Connection</code> figure
	 * @param percentageFromSource percentage of the connector length 
	 * away from the source end (range is from 0 to 100)
	 */
	public OnConnectionLocator(
		Connection connection,
		int percentageFromSource) {

		super(connection);
		this.percentageFromSource = percentageFromSource;
	}

	/**
	 * Puts the figure a percentage of the connector length away from the source end.
	 * @see org.eclipse.draw2d.ConnectionLocator#getLocation(PointList)
	 */
	protected Point getLocation(PointList points) {
		Point p =
			PointListUtilities.calculatePointRelativeToLine(
				PointListUtilities.copyPoints(points),
				0,
				getPercentageFromSource(),
				true);
		return p;
	}

	/**
	 * Gets the value of <code>percentageFromSource</code>.
	 * @return int the value of <code>percentageFromSource</code>
	 */
	protected int getPercentageFromSource() {
		return percentageFromSource;
	}

}
