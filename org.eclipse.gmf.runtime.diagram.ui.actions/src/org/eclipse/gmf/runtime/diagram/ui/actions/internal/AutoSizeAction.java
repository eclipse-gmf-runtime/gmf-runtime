/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.ui.IWorkbenchPage;

/**
 * 
 * @author melaasar
 */
public class AutoSizeAction extends DiagramAction {

    /**
     * @param workbenchPage
     */
    public AutoSizeAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);

        setText(DiagramUIActionsMessages.AutoSizeAction_ActionLabelText);
        setId(ActionIds.ACTION_AUTOSIZE);
        setToolTipText(DiagramUIActionsMessages.AutoSizeAction_ActionToolTipText);
        
        setImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE);
        setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE_DISABLED);
        setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE);
    }

    /**
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
     */
    protected Request createTargetRequest() {
        return new Request(RequestConstants.REQ_AUTOSIZE);
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
     */
    protected boolean isSelectionListener() {
        return true;
    }

}
