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
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AutoSizeAction extends PresentationAction {

	/**
	 * @param workbenchPage
	 */
	public AutoSizeAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);

		setText(Messages.getString("AutoSizeAction.ActionLabelText")); //$NON-NLS-1$
		setId(ActionIds.ACTION_AUTOSIZE);
		setToolTipText(Messages.getString("AutoSizeAction.ActionToolTipText")); //$NON-NLS-1$
		setImageDescriptor(Images.DESC_ACTION_AUTOSIZE);
		setDisabledImageDescriptor(Images.DESC_ACTION_AUTOSIZE_DISABLED);
		setHoverImageDescriptor(Images.DESC_ACTION_AUTOSIZE);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
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
