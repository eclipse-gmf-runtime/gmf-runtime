/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.providers;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.CompositeLayoutProvider;

/**
 * @author mmostafa
 *
 * Custom provider that lays out the composite directed graph in a left to right fashion.
 */

public class CompositeLeftRightProvider
    extends CompositeLayoutProvider {
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateToGraph(org.eclipse.draw2d.geometry.Rectangle)
     */
    protected Rectangle translateToGraph(Rectangle r) {
        Rectangle rDP = new Rectangle(r.y, r.x, r.height, r.width);
        getMapMode().LPtoDP(rDP);
        return rDP;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.providers.internal.DefaultProvider#translateFromGraph(org.eclipse.draw2d.geometry.Rectangle)
     */
    protected Rectangle translateFromGraph(Rectangle rect) {
        Rectangle rLP = new Rectangle(rect.y, rect.x, rect.height, rect.width);
        getMapMode().DPtoLP(rLP);
        return rLP;
    }

}
