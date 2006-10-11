/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ***/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.IExpandableFigure;


public class BorderItemsAwareFreeFormLayer
    extends FreeformLayer {
    
    Rectangle extendedBounds = null;
    
    public Rectangle getBounds() {
        if (extendedBounds==null){
            Iterator  figuresIter = getChildren().iterator();
            Rectangle _bounds = super.getBounds().getCopy();
            while (figuresIter.hasNext()) {
                Figure element = (Figure) figuresIter.next();
                Rectangle rect = null;
                if (element instanceof IExpandableFigure){
                    rect = ((IExpandableFigure)element).getExtendedBounds();
                }else {
                    rect = element.getBounds();
                }
                _bounds.union(rect);
            }
            extendedBounds = _bounds; 
        }
        return extendedBounds;
    }

    public void invalidate() {
        extendedBounds = null;
        super.invalidate();
    }

    public void validate() {
        extendedBounds = null;
        super.validate();
    }
    
    
}
