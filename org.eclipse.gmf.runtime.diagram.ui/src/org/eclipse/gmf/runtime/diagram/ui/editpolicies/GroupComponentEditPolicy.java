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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Iterator;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.core.commands.UngroupCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.GroupRequestViaKeyboard;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * A <code>ComponentEditPolicy</code> for a <code>GroupEditPart</code>.
 * 
 * @author crevells
 * @since 2.1
 */
public class GroupComponentEditPolicy
    extends ComponentEditPolicy {

    public boolean understandsRequest(Request request) {
        if (ActionIds.ACTION_UNGROUP.equals(request.getType())) {
            return true;
        }
        return super.understandsRequest(request);
    }

    public Command getCommand(Request request) {
        if (ActionIds.ACTION_UNGROUP.equals(request.getType())) {
            return getUngroupCommand(request);
        }
        return super.getCommand(request);
    }

    public EditPart getTargetEditPart(Request request) {
        return understandsRequest(request) ? getHost()
            : null;
    }

    /**
     * Returns a command to ungroup and then delete the group in the request.
     * 
     * @param request
     *            the request containing the group to be ungrouped
     * @return the command to perform the ungrouping
     */
    protected Command getUngroupCommand(Request request) {
        UngroupCommand cmd = new UngroupCommand(((GroupEditPart) getHost())
            .getEditingDomain(), (Node) getHost().getModel());
        return new ICommandProxy(cmd);
    }

    /**
     * Override to delete the group's children's semantic elements.
     */
    protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();

        boolean shouldShowPrompt = (deleteRequest instanceof GroupRequestViaKeyboard) ? ((GroupRequestViaKeyboard) deleteRequest)
            .isShowInformationDialog()
            : false;

        EditCommandRequestWrapper editCommandRequest = new EditCommandRequestWrapper(
            new DestroyElementRequest(editingDomain, shouldShowPrompt),
            deleteRequest.getExtendedData());

        CompoundCommand cc = new CompoundCommand();
        for (Iterator iter = ((GroupEditPart) getHost()).getShapeChildren()
            .iterator(); iter.hasNext();) {
            IGraphicalEditPart childEP = (IGraphicalEditPart) iter.next();
            Command semanticCmd = childEP.getCommand(editCommandRequest);
            if (semanticCmd != null && semanticCmd.canExecute()) {
                cc.add(semanticCmd);
            }
        }

        if (!cc.isEmpty()) {
            cc.add(createDeleteViewCommand(deleteRequest));
            return cc;
        }

        return createDeleteViewCommand(deleteRequest);
    }

}
