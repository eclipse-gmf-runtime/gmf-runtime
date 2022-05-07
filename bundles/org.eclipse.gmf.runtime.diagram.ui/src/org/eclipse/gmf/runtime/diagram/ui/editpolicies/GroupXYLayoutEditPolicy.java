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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.core.commands.UpdateGroupLocationCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A <code>XYLayoutEditPolicy</code> for a <code>GroupEditPart</code>.
 * 
 * @author crevells
 * @since 2.1
 */
public class GroupXYLayoutEditPolicy
    extends XYLayoutEditPolicy {

    public boolean understandsRequest(Request req) {
        if (RequestConstants.REQ_AUTOSIZE.equals(req.getType())) {
            return true;
        }
        return super.understandsRequest(req);
    }

    public Command getCommand(Request request) {
        if (RequestConstants.REQ_AUTOSIZE.equals(request.getType()))
            return getCommandFromChildren(request);
        return super.getCommand(request);
    }

    /**
     * Overridden so that if a child shape is moved or resized such that the
     * group's location (i.e. top left corner) changes, the group's location as
     * well as all the children's relative locations are updated.
     */
    protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
        CompoundCommand resize = new CompoundCommand();
        resize.add(super.getResizeChildrenCommand(request));
        resize.add(new ICommandProxy(new UpdateGroupLocationCommand(
            ((IGraphicalEditPart) getHost()).getEditingDomain(),
            (View) getHost().getModel())));
        return resize;
    }

    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            return null;
        } else if (RequestConstants.REQ_AUTOSIZE.equals(request.getType())) {
            return getHost();
        }
        return super.getTargetEditPart(request);
    }

    /**
     * Gets a command from each child in the group.
     * 
     * @param request
     * @return the compound command
     */
    private Command getCommandFromChildren(Request request) {
        CompoundCommand cc = new CompoundCommand();
        for (Iterator iter = getHost().getChildren().iterator(); iter.hasNext();) {
            EditPart childEP = (EditPart) iter.next();
            cc.add(childEP.getCommand(request));
        }
        cc.unwrap();
        return cc;
    }

}
