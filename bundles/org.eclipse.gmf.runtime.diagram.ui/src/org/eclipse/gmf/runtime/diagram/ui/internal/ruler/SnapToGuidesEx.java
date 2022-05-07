/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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

import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Overridden to:
 * <li> support various mapmode units</li>
 * <li> support snapping to geometry in a restricted direction. See
 * {@link SnapToHelperUtil#RESTRICTED_DIRECTIONS}.</li>
 * 
 * @author jschofie, crevells
 */
public class SnapToGuidesEx
    extends SnapToGuides {

    /**
     * The vertical guides in logical mapmode units.
     */
    private int[] verticalGuidesMM;

    /**
     * The horizontal guides in logical mapmode units.
     */
    private int[] horizontalGuidesMM;

    public SnapToGuidesEx(GraphicalEditPart container) {
        super(container);
    }

    protected double getThreshold() {
        IMapMode mm = MapModeUtil.getMapMode(container.getFigure());
        return mm.DPtoLP((int) super.getThreshold());
    }

    protected int[] getHorizontalGuides() {
        if (horizontalGuidesMM == null) {
            int guides[] = super.getHorizontalGuides();
            IMapMode mm = MapModeUtil.getMapMode(container.getFigure());
            horizontalGuidesMM = new int[guides.length];
            for (int i = 0; i < guides.length; i++) {
                int guide = guides[i];
                horizontalGuidesMM[i] = mm.DPtoLP(guide);
            }
        }
        return horizontalGuidesMM;
    }

    protected int[] getVerticalGuides() {
        if (verticalGuidesMM == null) {
            int guides[] = super.getVerticalGuides();
            IMapMode mm = MapModeUtil.getMapMode(container.getFigure());
            verticalGuidesMM = new int[guides.length];
            for (int i = 0; i < guides.length; i++) {
                int guide = guides[i];
                verticalGuidesMM[i] = mm.DPtoLP(guide);
            }
        }
        return verticalGuidesMM;
    }

    protected double getCorrectionFor(int[] guides, double value,
            Map extendedData, boolean vert, int side) {

        Integer restrictedDirections = (Integer) extendedData
            .get(SnapToHelperUtil.RESTRICTED_DIRECTIONS);
        if (restrictedDirections == null
            || restrictedDirections == PositionConstants.NONE) {
            return super.getCorrectionFor(guides, value, extendedData, vert,
                side);
        }

        if (restrictedDirections == NONE) {
            return super.getCorrectionFor(guides, value, extendedData, vert,
                side);
        }

        boolean increaseOK = vert ? (restrictedDirections & EAST) != 0
            : (restrictedDirections & SOUTH) != 0;
        boolean decreaseOK = vert ? (restrictedDirections & WEST) != 0
            : (restrictedDirections & NORTH) != 0;

        int filteredGuides[] = new int[guides.length];
        int count = 0;
        for (int i = 0; i < guides.length; i++) {
            if ((increaseOK && guides[i] > value)
                || (decreaseOK && guides[i] < value)) {
                filteredGuides[count++] = guides[i];
            }
        }

        // remove empty entries
        int[] filteredGuides2 = new int[count];
        for (int i = 0; i < count; i++) {
            filteredGuides2[i] = filteredGuides[i];
        }

        return super.getCorrectionFor(filteredGuides2, value, extendedData,
            vert, side);
    }

}
