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
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GroupComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GroupXYLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableEditPolicyEx;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.GroupFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The editpart for a group. A group is a special type of container around
 * shapes.
 * 
 * @author crevells, mmostafa
 * @since 2.1
 */
public class GroupEditPart
    extends ShapeNodeEditPart {

    /**
     * Creates a new <code>GroupEditPart</code>.
     * 
     * @param view
     *            the view controlled by this edit part
     */
    public GroupEditPart(View view) {
        super(view);
    }

    protected NodeFigure createNodeFigure() {
        return new GroupFigure();
    }

    public boolean canAttachNote() {
        return false;
    }

    public IFigure getContentPane() {
        return ((GroupFigure) getFigure()).getContainerFigure();
    }

    public void setLayoutConstraint(EditPart child, IFigure childFigure,
            Object constraint) {
        getContentPaneFor((IGraphicalEditPart) child).setConstraint(
            childFigure, constraint);
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
    public List getShapeChildren() {
        List flatChildren = new ArrayList(getChildren().size());
        for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
            Object childEP = iter.next();
            if (childEP instanceof GroupEditPart) {
                flatChildren.addAll(((GroupEditPart) childEP)
                    .getShapeChildren());
            } else {
                flatChildren.add(childEP);
            }
        }
        return flatChildren;
    }

}
