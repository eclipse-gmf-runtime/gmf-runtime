/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * @author sshaw
 *
 * Custom provider that lays out the directed graph in a top to down fashion.
 */
public class TopDownProvider extends DefaultProvider {
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateToGraph(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Rectangle translateToGraph(Rectangle r) {
		Rectangle rDP = new Rectangle(r);
		MapMode.translateToDP(rDP);
		return rDP;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateFromGraph(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Rectangle translateFromGraph(Rectangle rect) {
		Rectangle rLP = new Rectangle(rect);
		MapMode.translateToLP(rLP);
		return rLP;
	}
}
