/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.ui.IWorkbenchPage;

/**
 * An action to ungroup a group of shapes.
 * 
 * @author crevells
 */
public class UngroupAction
    extends DiagramAction {

    /**
     * Creates a new instance.
     * 
     * @param workbenchPage
     */
    public UngroupAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
        setId(ActionIds.ACTION_UNGROUP);
        setText(DiagramUIActionsMessages.GroupAction_Ungroup_ActionLabelText);
        setToolTipText(DiagramUIActionsMessages.GroupAction_Ungroup_ActionToolTipText);
        setImageDescriptor(DiagramUIActionsPluginImages.DESC_UNGROUP);
        setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_UNGROUP_DISABLED);
        setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_UNGROUP);
    }

    protected Request createTargetRequest() {
        return new Request(getId());
    }

    protected boolean isSelectionListener() {
        return true;
    }

}
