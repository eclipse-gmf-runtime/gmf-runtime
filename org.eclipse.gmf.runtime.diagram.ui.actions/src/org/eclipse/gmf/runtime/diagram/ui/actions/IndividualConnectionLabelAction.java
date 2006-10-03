/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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

import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsPlugin;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DiagramActionsStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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
			PackageUtil.getID(NotationPackage.eINSTANCE.getView_Visible()),
			DiagramUIActionsMessages.ConstrainedFlowLayoutEditPolicy_changeVisibilityCommand_label);
		Assert.isNotNull(labelSemanticHints);
		this.labelSemanticHints = labelSemanticHints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getTargetEditParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEditParts(EditPart editpart) {
		EditPart targetEP = null;
		List editParts = new ArrayList();
		if (editpart instanceof ConnectionNodeEditPart) {
			final ConnectionNodeEditPart conEP = (ConnectionNodeEditPart) editpart;
			TransactionalEditingDomain editingDomain = conEP.getEditingDomain();
			for (int i = 0; i < getLabelSemanticHints().length; i++) {
				final int index = i;
				
				try {
					targetEP = (EditPart) editingDomain
						.runExclusive(new RunnableWithResult.Impl() {

							public void run() {
								setResult(conEP
									.getChildBySemanticHint(getLabelSemanticHints()[index]));
							}
						});
				} catch (InterruptedException e) {
					Trace.catching(DiagramActionsPlugin.getInstance(),
						DiagramActionsDebugOptions.EXCEPTIONS_CATCHING,
						getClass(), "getTargetEditParts", e); //$NON-NLS-1$
					Log.error(DiagramActionsPlugin.getInstance(),
						DiagramActionsStatusCodes.IGNORED_EXCEPTION_WARNING,
						"getTargetEditParts", e); //$NON-NLS-1$
				}
				
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