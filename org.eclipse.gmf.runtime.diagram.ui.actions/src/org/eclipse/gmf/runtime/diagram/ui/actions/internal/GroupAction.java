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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

/**
 * An action to group shapes together.
 * 
 * @author mmostafa, crevells
 */
public class GroupAction
    extends DiagramAction {

    /**
     * Creates a new instance.
     * 
     * @param workbenchPage
     */
    public GroupAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
        setId(ActionIds.ACTION_GROUP);
        setText(DiagramUIActionsMessages.GroupAction_Group_ActionLabelText);
        setToolTipText(DiagramUIActionsMessages.GroupAction_Group_ActionToolTipText);
        setImageDescriptor(DiagramUIActionsPluginImages.DESC_GROUP);
        setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_GROUP_DISABLED);
        setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_GROUP);
    }

    protected Request createTargetRequest() {
        return new GroupRequest(getId());
    }

    protected void updateTargetRequest() {
        GroupRequest request = (GroupRequest) getTargetRequest();
        request.setEditParts(getOperationSet());
    }

    protected Command getCommand() {
        if (getOperationSet().size() > 1) {
            EditPart parent = ((EditPart) getOperationSet().get(0)).getParent();
            return parent.getCommand(getTargetRequest());
        }
        return UnexecutableCommand.INSTANCE;
    }

    protected List createOperationSet() {
        List selection = getSelectedObjects();
        if (selection.size() <= 1
            || !(selection.get(0) instanceof IGraphicalEditPart))
            return Collections.EMPTY_LIST;

        return ToolUtilities.getSelectionWithoutDependants(selection);
    }

    protected boolean isSelectionListener() {
        return true;
    }

    protected void doRun(IProgressMonitor progressMonitor) {

        super.doRun(progressMonitor);

        Object model = ((EditPart) ((GroupRequest) getTargetRequest())
            .getEditParts().get(0)).getModel();
        if (model instanceof View) {
            Object groupView = ((View) model).eContainer();
            Object groupEP = getDiagramGraphicalViewer().getEditPartRegistry()
                .get(groupView);
            if (groupEP != null) {
                getDiagramGraphicalViewer().setSelection(
                    new StructuredSelection(groupEP));
            }
        }
    }

}
