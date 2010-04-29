/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * a Border item aware implementation for the free form layer
 * This layer will make sure that scroll bars and bounds calculations 
 * respect the border items implementation
 * @author mmostafa
 *
 */
public class BorderItemsAwareFreeFormLayer
    extends FreeformLayer {
    
    private BorderItemAwareFreeFormHelper _helper = new BorderItemAwareFreeFormHelper(this);
    
    Rectangle extendedBounds = null;
    
    /**
     * The helper class used by the border item aware free form layer
     * This helper class will calculate the correct extent of the layer, considering the 
     * border items
     * @author mmostafa
     *
     */
    public class BorderItemAwareFreeFormHelper implements FreeformListener
    {

        class ChildTracker implements FigureListener {
            public void figureMoved(IFigure source) {
                invalidate();
            }
        }

        private FreeformFigure host;
        private Rectangle freeformExtent;
        private FigureListener figureListener = new ChildTracker();

        public BorderItemAwareFreeFormHelper(FreeformFigure host) {
            this.host = host;
        }
        
        public void reset(){
            freeformExtent = null;
        }

        public Rectangle getFreeformExtent() {
            if (freeformExtent != null)
                return freeformExtent;
            Rectangle r;
            List children = host.getChildren();
            for (int i = 0; i < children.size(); i++) {
                IFigure child = (IFigure)children.get(i);
                if (child instanceof FreeformFigure)
                    r = ((FreeformFigure) child).getFreeformExtent();
                else if (child instanceof IExpandableFigure)
                    r = ((IExpandableFigure) child).getExtendedBounds();
                else
                    r = child.getBounds();
                if (freeformExtent == null)
                    freeformExtent = r.getCopy();
                else
                    freeformExtent.union(r);
            }
            Insets insets = host.getInsets();
            if (freeformExtent == null)
                freeformExtent = new Rectangle(0, 0, insets.getWidth(), insets.getHeight());
            else {
                host.translateToParent(freeformExtent);
                freeformExtent.expand(insets);
            }
            return freeformExtent;
        }

        public void hookChild(IFigure child) {
            invalidate();
            if (child instanceof FreeformFigure)
                ((FreeformFigure)child).addFreeformListener(this);
            else
                child.addFigureListener(figureListener);
        }

        void invalidate() {
            freeformExtent = null;
            host.fireExtentChanged();
            if (host.getParent() != null)
                host.getParent().revalidate();
            else
                host.revalidate();
        }

        public void notifyFreeformExtentChanged() {
            //A childs freeform extent has changed, therefore this extent must be recalculated
            invalidate();
        }

        public void setFreeformBounds(Rectangle bounds) {
            host.setBounds(bounds);
            bounds = bounds.getCopy();
            host.translateFromParent(bounds);
            List children = host.getChildren();
            for (int i = 0; i < children.size(); i++) {
                IFigure child = (IFigure)children.get(i);
                if (child instanceof FreeformFigure)
                    ((FreeformFigure) child).setFreeformBounds(bounds);
            }
        }

        public void unhookChild(IFigure child) {
            invalidate();
            if (child instanceof FreeformFigure)
                ((FreeformFigure)child).removeFreeformListener(this);
            else
                child.removeFigureListener(figureListener);
        }

   }

    
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
    
    /**
     * @see IFigure#add(IFigure, Object, int)
     */
    public void add(IFigure child, Object constraint, int index) {
        super.add(child, constraint, index);
        _helper.hookChild(child);
    }
    
    /**
     * @see FreeformFigure#getFreeformExtent()
     */
    public Rectangle getFreeformExtent() {
        return _helper.getFreeformExtent();
    }
    
    /**
     * @see IFigure#remove(IFigure)
     */
    public void remove(IFigure child) {
        _helper.unhookChild(child);
        super.remove(child);
    }
    
    /**
     * @see FreeformFigure#setFreeformBounds(Rectangle)
     */
    public void setFreeformBounds(Rectangle bounds) {
        _helper.setFreeformBounds(bounds);
    }
    
    /**
     * will notify the layer that a border item had been moved; which will result in 
     * invalidating the layer (recalculating the bounds and extent).
     */
    public void borderFigureMoved(){
        _helper.invalidate();
    }
    
}
