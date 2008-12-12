/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.ui.IWorkbenchPage;

/**
 * 
 * @author melaasar
 */
public class AutoSizeAction extends DiagramAction {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommandLabel()
	 */
	protected String getCommandLabel() {
		return DiagramUIActionsMessages.AutoSizeAction_ActionLabelText;
	}

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
    
    protected boolean isOperationHistoryListener() {
        return true;
    }
    
    @Override
    protected Command getCommand(Request request) {
    	boolean foundNonAutosizedPart = false;
    	List operationSet = getOperationSet();
		Iterator editParts = operationSet.iterator();
		CompoundCommand command = new CompoundCommand(getCommandLabel());
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();
			
			//check if the editpart is autosized
			if (editPart instanceof GraphicalEditPart) {
				GraphicalEditPart graphicalEditPart = (GraphicalEditPart) editPart;
				Integer containerWidth = (Integer) graphicalEditPart
						.getStructuralFeatureValue(NotationPackage.eINSTANCE
								.getSize_Width());
				Integer containerHeight = (Integer) graphicalEditPart
						.getStructuralFeatureValue(NotationPackage.eINSTANCE
								.getSize_Height());
				if (containerWidth.intValue() != -1
						|| containerHeight.intValue() != -1) {
					foundNonAutosizedPart = true;
				}
			}
			
			Command curCommand = editPart.getCommand(request);
			if (curCommand != null) {
				command.add(curCommand);
			}
		}
		return command.isEmpty() || command.size() != operationSet.size() || !foundNonAutosizedPart ? UnexecutableCommand.INSTANCE
			: (Command) command;
    	
    	
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		super.refresh();
		setEnabled(calculateEnabled());
	}

}
