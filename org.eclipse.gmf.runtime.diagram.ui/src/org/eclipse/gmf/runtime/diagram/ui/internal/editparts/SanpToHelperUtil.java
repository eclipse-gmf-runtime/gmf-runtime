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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.SnapToGuidesEx;

/**
 * @author mmostafa
 * 
 * Utility class used by the EditPart to allow them to adapt a SnapeHelper.class
 * 
 */
public class SanpToHelperUtil {

    /**
     * returns the the appropiate snap helper(s), this method will always reach
     * for the first reachable DiagramEditPart using the poassed edit part, then
     * use this Diagram edit part to get the snap helper
     * 
     * @param editPart ,
     *            edit part to get the snap helper for
     * @return
     */
    static public Object getSnapHelper(GraphicalEditPart editPart) {
        // get the diagram Edit Part
        GraphicalEditPart diagramEditPart = editPart;
        while (diagramEditPart != null
            && !(diagramEditPart instanceof DiagramEditPart)) {
            diagramEditPart = (GraphicalEditPart) diagramEditPart.getParent();
        }

        if (diagramEditPart == null)
            return null;

        List snapStrategies = new ArrayList();
        EditPartViewer viewer = diagramEditPart.getViewer();

        Boolean val = (Boolean) editPart.getViewer().getProperty(
            RulerProvider.PROPERTY_RULER_VISIBILITY);

        if (val != null && val.booleanValue())
            snapStrategies.add(new SnapToGuidesEx(diagramEditPart));

        val = (Boolean) viewer
            .getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        if (val != null && val.booleanValue())
            snapStrategies.add(new SnapToGrid(diagramEditPart));

        if (snapStrategies.size() == 0)
            return null;

        if (snapStrategies.size() == 1)
            return snapStrategies.get(0);

        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
        for (int i = 0; i < snapStrategies.size(); i++)
            ss[i] = (SnapToHelper) snapStrategies.get(i);
        return new CompoundSnapToHelper(ss);
    }
}
