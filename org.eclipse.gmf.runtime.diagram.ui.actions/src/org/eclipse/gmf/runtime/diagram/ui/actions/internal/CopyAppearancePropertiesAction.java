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

import java.util.List;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ApplyAppearancePropertiesRequest;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Action that takes the appearance properties of edit part first in the selection, and
 * applies it to the rest of the selected objects.
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %level1
 * @author melaasar
 */
public class CopyAppearancePropertiesAction extends PresentationAction {

	/**
	 * @param workbenchPage
	 */
	public CopyAppearancePropertiesAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);

		setId(ActionIds.ACTION_COPY_APPEARANCE_PROPERTIES);
		setText(DiagramActionsResourceManager.getInstance().getString("CopyAppearancePropertiesAction.text")); //$NON-NLS-1$
		setToolTipText(DiagramActionsResourceManager.getInstance().getString("CopyAppearancePropertiesAction.toolTip")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_COPY_APPEARANCE); 
		setDisabledImageDescriptor(Images.DESC_ACTION_COPY_APPEARANCE_DISABLED);
		setHoverImageDescriptor(DiagramActionsResourceManager.getInstance().createImageDescriptor("copy_appearance_properties.gif")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new ApplyAppearancePropertiesRequest();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		ApplyAppearancePropertiesRequest request =
			(ApplyAppearancePropertiesRequest) getTargetRequest();
		List set = super.createOperationSet();
		if (!set.isEmpty()) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) set.get(0);
			request.setProperties(getEditPartAppearancePropertiesMap(editPart));
		}
		super.updateTargetRequest();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createOperationSet()
	 */
	protected List createOperationSet() {
		List operationSet = super.createOperationSet();
		if (!operationSet.isEmpty())
			operationSet.remove(0);
		return operationSet;
	}

	/**
	 * Get the apprearance properties map of the given editpart
	 * 
	 * @param editPart
	 * @return
	 */
	private Map getEditPartAppearancePropertiesMap(final IGraphicalEditPart editPart) {
		return (Map)MEditingDomainGetter.getMEditingDomain((View)editPart.getModel()).runAsRead( new MRunnable() {
			public Object run() {
				return editPart.getAppearancePropertiesMap();
			}
		});
	}

}
