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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GroupComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GroupXYLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The editpart for a group. A group is a special type of container around
 * shapes.
 * 
 * @author crevells, mmostafa
 */
public class GroupEditPart
    extends ShapeEditPart {
    
    class GroupFigure extends NodeFigure {
        public boolean containsPoint(int x, int y) {
            Point absolutePoint = new Point(x, y);
            translateToAbsolute(absolutePoint);
            for (Iterator iterator = getChildren().iterator(); iterator
                .hasNext();) {
                IFigure child = (IFigure) iterator.next();
                Point pt = absolutePoint.getCopy();
                child.translateToRelative(pt);
                if (child.containsPoint(pt)) {
                    return true;
                }
            }
            return false;
        }

        protected boolean useLocalCoordinates() {
            return true;
        }
    }

    /**
     * constructor
     * 
     * @param view
     *            the view controlled by this edit part
     */
    public GroupEditPart(View view) {
        super(view);
    }

    protected IFigure createFigure() {
        IFigure f = new GroupFigure();
        f.setLayoutManager(new XYLayout());
        f.setOpaque(false);
        return f;
    }

    public DragTracker getDragTracker(Request request) {

        // we only want to select the group if the user clicked the area
        // over one of the shapes in the group
        if (request instanceof SelectionRequest) {

            for (Iterator iterator = getChildren().iterator(); iterator
                .hasNext();) {
                IGraphicalEditPart childEP = (IGraphicalEditPart) iterator
                    .next();
                Point location = ((SelectionRequest) request).getLocation()
                    .getCopy();

                childEP.getFigure().translateToRelative(location);

                if (childEP.getFigure().containsPoint(location)) {
                    return super.getDragTracker(request);
                }
            }

            // in this case the user has not clicked over one of the shapes,
            // so disable selection; however, we must still support dragging
            // to move the group if it is already selected.
            return new DragEditPartsTrackerEx(this) {

                protected boolean handleButtonDown(int button) {
                    int selectedState = getSelected();
                    if (selectedState == SELECTED
                        || selectedState == SELECTED_PRIMARY) {
                        return super.handleButtonDown(button);
                    }

                    // do nothing if the group isn't selected
                    return true;
                }

            };
        }

        return super.getDragTracker(request);
    }

    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.DECORATION_ROLE,
            new DecorationEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new GroupXYLayoutEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE,
            new GroupComponentEditPolicy());
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
        installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
            new SnapFeedbackPolicy());
    }

    public EditPolicy getPrimaryDragEditPolicy() {
        return new NonResizableEditPolicyEx();
    }

    /**
     * Gets all the shape children of this group, digging into any nested groups
     * found.
     * 
     * @return all the shape children including shapes in nested groups
     */
    public List getFlattenedChildren() {
        List flatChildren = new ArrayList(getChildren().size());
        for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
            Object childEP = iter.next();
            if (childEP instanceof GroupEditPart) {
                flatChildren.addAll(((GroupEditPart) childEP)
                    .getFlattenedChildren());
            } else {
                flatChildren.add(childEP);
            }
        }
        return flatChildren;
    }

}
