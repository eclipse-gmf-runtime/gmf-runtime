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


package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.IFigure;


/**
 * Utility class used to provide helper methods commonly used by the 
 * Border Item figures 
 * @author mmostafa
 *
 */
public class BorderItemsUtil {
    
    /**
     * returns the border item waare free form layer for the passed figure
     * @param borderFigure  the figure to use to get the <code>BorderItemsAwareFreeFormLayer</code>
     * @return BorderItemsAwareFreeFormLayer or null if there is no one found
     */
    static public BorderItemsAwareFreeFormLayer getBorderItemLayer(IFigure borderFigure){
        IFigure figure = borderFigure.getParent();
        while (figure!=null && !(figure instanceof BorderItemsAwareFreeFormLayer)){
            figure = figure.getParent();
        }
        if (figure!=null){
            return (BorderItemsAwareFreeFormLayer)figure;
        }
        return null;
    }

}
