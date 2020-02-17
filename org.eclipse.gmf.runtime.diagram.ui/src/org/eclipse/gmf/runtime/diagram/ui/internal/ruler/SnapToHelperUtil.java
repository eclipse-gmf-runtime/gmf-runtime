/******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;

/**
 * @author mmostafa, crevells
 * 
 * Utility class for the snapping behavior.
 * 
 */
public class SnapToHelperUtil implements PositionConstants {
    
    /**
     * This string can be used as a key in the extended map of the request sent
     * to the <code>SnapToHelper</code> to support snapipng in restricted
     * directions. This key should be associated with an Integer whose value is
     * either PositionConstants.NONE or a combination of EAST, WEST, NORTH, and
     * SOUTH. See the individual <code>SnapToHelpers</code> to see how this
     * affects the snapping algorithms. See <code>DragEditPartsTrackerEx</code>
     * to see the use case in which this is set.
     */
    public static final String RESTRICTED_DIRECTIONS = "org.eclipse.gmf.runtime.diagram.ui.RestrictedDirections"; //$NON-NLS-1$

    /**
     * returns the the appropriate snap helper(s), this method will always reach
     * for the first reachable DiagramEditPart using the passed edit part, then
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

        // for snap to geometry, attempt to locate a compartment as a parent
        GraphicalEditPart parent = editPart;
        while (parent != null && !(parent instanceof ISurfaceEditPart)) {
            parent = (GraphicalEditPart) parent.getParent();
        }

        if (parent == null)
            parent = diagramEditPart;

        List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
        EditPartViewer viewer = diagramEditPart.getViewer();

        Boolean val = (Boolean) editPart.getViewer().getProperty(
            RulerProvider.PROPERTY_RULER_VISIBILITY);

        if (val != null && val.booleanValue())
            snapStrategies.add(new SnapToGuidesEx(diagramEditPart));

        val = (Boolean) viewer
            .getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        if (val != null && val.booleanValue())
            snapStrategies.add(new SnapToGeometryEx(parent));

        val = (Boolean) viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);

        if (val != null && val.booleanValue())
            snapStrategies.add(new SnapToGridEx(diagramEditPart));

        if (snapStrategies.size() == 0)
            return null;

        if (snapStrategies.size() == 1)
            return snapStrategies.get(0);

        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
        for (int i = 0; i < snapStrategies.size(); i++)
            ss[i] = snapStrategies.get(i);
        return new CompoundSnapToHelperEx(ss);
    }

    /**
     * Updates the snapLocations if it violates the restricted directions.
     * 
     * @param snapLocations
     *            the locations in which snapping will occur
     * @param restrictedDirections
     *            the restricted directions for snapping
     * @return the updated snapLocations
     */
    public static int updateSnapLocations(int snapLocations,
            int restrictedDirections) {

        if (restrictedDirections == NONE) {
            return snapLocations;
        }
        if ((snapLocations & HORIZONTAL) != 0
            && (restrictedDirections & EAST) == 0
            && (restrictedDirections & WEST) == 0) {
            snapLocations &= ~HORIZONTAL;
        }
        if ((snapLocations & VERTICAL) != 0
            && (restrictedDirections & SOUTH) == 0
            && (restrictedDirections & NORTH) == 0) {
            snapLocations &= ~VERTICAL;
        }
        return snapLocations;
    }
}
