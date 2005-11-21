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
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AutoSizeAction extends DiagramAction {

	/**
	 * @param workbenchPage
	 */
	public AutoSizeAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);

		setText(DiagramActionsResourceManager.getI18NString("AutoSizeAction.ActionLabelText")); //$NON-NLS-1$
		setId(ActionIds.ACTION_AUTOSIZE);
		setToolTipText(DiagramActionsResourceManager.getI18NString("AutoSizeAction.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_AUTOSIZE);
		setImageDescriptor(enabledImage);
		setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance()
			.getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_AUTOSIZE_DISABLED));
		setHoverImageDescriptor(enabledImage);
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
