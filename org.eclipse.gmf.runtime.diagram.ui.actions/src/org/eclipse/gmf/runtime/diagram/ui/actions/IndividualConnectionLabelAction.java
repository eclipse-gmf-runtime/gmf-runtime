/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author mboos
 * 
 * An action that toggles between showing and hiding labels on connections and
 * diagrams.
 */
public abstract class IndividualConnectionLabelAction
	extends BooleanPropertyAction {

	private final String[] labelSemanticHints;

	/**
	 * Constructor
	 * 
	 * @param workbenchPage
	 *            the active workbenchPage
	 * @param labelSemanticHints
	 *            the semantic hints to use to figure out the target edit parts
	 *            for this action
	 */
	protected IndividualConnectionLabelAction(IWorkbenchPage workbenchPage,
			String[] labelSemanticHints) {
		super(
			workbenchPage,
			MetaModelUtil.getID(NotationPackage.eINSTANCE.getView_Visible()),
			DiagramUIActionsMessages.ConstrainedFlowLayoutEditPolicy_changeVisibilityCommand_label);
		Assert.isNotNull(labelSemanticHints);
		this.labelSemanticHints = labelSemanticHints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getTargetEdiParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEdiParts(EditPart editpart) {
		EditPart targetEP = null;
		List editParts = new ArrayList();
		if (editpart instanceof ConnectionNodeEditPart) {
			final ConnectionNodeEditPart conEP = (ConnectionNodeEditPart) editpart;
			MEditingDomain editingDomain = MEditingDomainGetter
				.getMEditingDomain((View) editpart.getModel());
			for (int i = 0; i < getLabelSemanticHints().length; i++) {
				final int index = i;
				targetEP = (EditPart) editingDomain.runAsRead(new MRunnable() {

					public Object run() {
						return conEP
							.getChildBySemanticHint(getLabelSemanticHints()[index]);
					}
				});
				if (targetEP != null)
					editParts.add(targetEP);
			}

		}
		return (editParts.isEmpty()) ? Collections.EMPTY_LIST
			: editParts;
	}

	/**
	 * Returns the request connection label semantic hint
	 * 
	 * @return The request connection label semantic hint
	 */
	protected String[] getLabelSemanticHints() {
		return labelSemanticHints;
	}

}