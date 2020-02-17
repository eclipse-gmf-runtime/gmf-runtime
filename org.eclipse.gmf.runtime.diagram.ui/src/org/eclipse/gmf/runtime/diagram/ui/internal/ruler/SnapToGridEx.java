/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGrid;

/**
 * Overridden to:
 * <li> support snapping to grid in a restricted direction. See
 * {@link SnapToHelperUtil#RESTRICTED_DIRECTIONS}.</li>
 * 
 * @author crevells
 */
public class SnapToGridEx
    extends SnapToGrid {

    public SnapToGridEx(GraphicalEditPart container) {
        super(container);
    }

    public int snapRectangle(Request request, int snapLocations,
            PrecisionRectangle rect, PrecisionRectangle result) {

        Integer restrictedDirections = (Integer) request.getExtendedData().get(
            SnapToHelperUtil.RESTRICTED_DIRECTIONS);
        if (restrictedDirections == null
            || restrictedDirections == PositionConstants.NONE) {
            return super.snapRectangle(request, snapLocations, rect, result);
        }

        snapLocations = SnapToHelperUtil.updateSnapLocations(snapLocations,
            restrictedDirections);

        rect = rect.getPreciseCopy();
        makeRelative(container.getContentPane(), rect);
        PrecisionRectangle correction = new PrecisionRectangle();
        makeRelative(container.getContentPane(), correction);

        if (gridX > 0 && (snapLocations & EAST) != 0) {
            correction.preciseWidth -= Math.IEEEremainder(rect.preciseRight()
                - origin.x - 1, gridX);
            snapLocations &= ~EAST;
        }

        if ((snapLocations & (WEST | HORIZONTAL)) != 0 && gridX > 0) {
            double leftCorrection = Math.IEEEremainder(
                rect.preciseX - origin.x, gridX);

            // /////////////////// ADDED THIS CODE
            if ((restrictedDirections & EAST) != 0
                && (restrictedDirections & WEST) == 0 && leftCorrection > 0) {
                // restricted to moving EAST
                correction.preciseX += gridX - leftCorrection;
            } else if ((restrictedDirections & WEST) != 0
                && (restrictedDirections & EAST) == 0 && leftCorrection < 0) {
                // restricted to moving WEST
                correction.preciseX -= gridX + leftCorrection;
            } else {
                // no horizontal restrictions
                correction.preciseX -= leftCorrection;
            }
            // ///////////////////

            if ((snapLocations & HORIZONTAL) == 0)
                correction.preciseWidth += leftCorrection;
            snapLocations &= ~(WEST | HORIZONTAL);
        }

        if ((snapLocations & SOUTH) != 0 && gridY > 0) {
            correction.preciseHeight -= Math.IEEEremainder(rect.preciseBottom()
                - origin.y - 1, gridY);
            snapLocations &= ~SOUTH;
        }

        if ((snapLocations & (NORTH | VERTICAL)) != 0 && gridY > 0) {
            double topCorrection = Math.IEEEremainder(rect.preciseY - origin.y,
                gridY);

            // /////////////////// ADDED THIS CODE
            if ((restrictedDirections & SOUTH) != 0
                && (restrictedDirections & NORTH) == 0 && topCorrection > 0) {
                // restricted to moving SOUTH
                correction.preciseY += gridY - topCorrection;
            } else if ((restrictedDirections & NORTH) != 0
                && (restrictedDirections & SOUTH) == 0 && topCorrection < 0) {
                // restricted to moving NORTH
                correction.preciseY -= gridY + topCorrection;
            } else {
                // no vertical restrictions
                correction.preciseY -= topCorrection;
            }
            // ///////////////////

            if ((snapLocations & VERTICAL) == 0)
                correction.preciseHeight += topCorrection;
            snapLocations &= ~(NORTH | VERTICAL);
        }

        correction.updateInts();
        makeAbsolute(container.getContentPane(), correction);
        result.preciseX += correction.preciseX;
        result.preciseY += correction.preciseY;
        result.preciseWidth += correction.preciseWidth;
        result.preciseHeight += correction.preciseHeight;
        result.updateInts();

        return snapLocations;
    }

}
