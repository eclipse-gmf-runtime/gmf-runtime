/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.layout;

import java.util.Iterator;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Free form layout that respect the min and max size of the shape 
 *  
 * @author mmostafa
 *
 */

public class FreeFormLayoutEx
    extends FreeformLayout {

    public void layout(IFigure parent) {
        Iterator children = parent.getChildren().iterator();
        Point offset = getOrigin(parent);
        IFigure f;
        while (children.hasNext()) {
            f = (IFigure)children.next();
            Rectangle bounds = ((Rectangle)getConstraint(f)).getCopy();
            if (bounds == null) continue;

            int widthHint = bounds.width;
            int heightHint = bounds.height;
            if (widthHint == -1 || heightHint == -1) {
                Dimension _preferredSize = f.getPreferredSize(widthHint, heightHint);
                bounds = bounds.getCopy();
                if (widthHint == -1)
                    bounds.width = _preferredSize.width;
                if (heightHint == -1)
                    bounds.height = _preferredSize.height;
            }
            Dimension min = f.getMinimumSize(widthHint, heightHint);
            Dimension max = f.getMaximumSize();
            
            if (min.width>bounds.width)
                bounds.width = min.width;
            else if (max.width < bounds.width)
                bounds.width = max.width;
            
            if (min.height>bounds.height)
                bounds.height = min.height;
            else if (max.height < bounds.height)
                bounds.height = max.height;
            bounds = bounds.getTranslated(offset);
            f.setBounds(bounds);
        }
    }
    
    

}
