/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Override to support HiMetrics *
 * 
 * @author cli
 */
public class SnapToGeometryEx
    extends SnapToGeometry {

    public SnapToGeometryEx(GraphicalEditPart container) {
        super(container);
    }

    // This code will be required to translate the figure's coordinates
    // accordingly if Bugzilla 188308 is fixed
    // protected static class EntryEx extends Entry {
    //
    // protected EntryEx(int type, int location) {
    // super(type, location);
    // }
    // }
    //

    // protected void populateRowsAndCols(List parts) {
    //		
    // rows = new Entry[parts.size() * 3];
    // cols = new Entry[parts.size() * 3];
    // GraphicalEditPart diagramEditPart = (GraphicalEditPart)parts.get(0);
    //		
    // while (diagramEditPart.getParent() != null){
    // diagramEditPart = (GraphicalEditPart)diagramEditPart.getParent();
    // if (diagramEditPart instanceof DiagramEditPart) break;
    // }
    //		
    // for (int i = 0; i < parts.size(); i++) {
    //			
    // GraphicalEditPart child = (GraphicalEditPart)parts.get(i);
    // Rectangle bounds = getFigureBounds(child).getCopy();
    //			
    // //translate the figure's coordinates to absolute, then relative
    // //to the diagram (i.e the figure's layout constraint on the diagram)
    // child.getFigure().translateToAbsolute(bounds);
    // diagramEditPart.getFigure().translateToRelative(bounds);
    //			
    // cols[i * 3] = new EntryEx(-1, bounds.x);
    // rows[i * 3] = new EntryEx(-1, bounds.y);
    // cols[i * 3 + 1] = new EntryEx(0, bounds.x + (bounds.width - 1) / 2);
    // rows[i * 3 + 1] = new EntryEx(0, bounds.y + (bounds.height - 1) / 2);
    // cols[i * 3 + 2] = new EntryEx(1, bounds.right() - 1);
    // rows[i * 3 + 2] = new EntryEx(1, bounds.bottom() - 1);
    //
    // }
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.SnapToGeometry#getThreshold()
     */

    protected double getThreshold() {
        IMapMode mm = MapModeUtil.getMapMode(container.getFigure());
        return mm.DPtoLP((int) super.getThreshold());
    }

}
