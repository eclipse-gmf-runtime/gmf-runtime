/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import com.ibm.xtools.notation.View;

/**
 * @author mboos
 * 
 * An action that toggles between showing and hiding labels on connectors and
 * diagrams.
 */
public abstract class IndividualConnectorLabelAction
	extends BooleanPropertyAction {

	private final String[] labelSemanticHints;

	/**
	 * Constructor
	 * @param workbenchPage the active workbenchPage 
	 * @param labelSemanticHints the semantic hints to use to figure out the target edit parts for this action 
	 */
	protected IndividualConnectorLabelAction(IWorkbenchPage workbenchPage,
			String[] labelSemanticHints) {
		super(
			workbenchPage,
			Properties.ID_ISVISIBLE,
			Messages
				.getString("ConstrainedFlowLayoutEditPolicy.changeVisibilityCommand.label")); //$NON-NLS-1$
		Assert.isNotNull(labelSemanticHints);
		this.labelSemanticHints = labelSemanticHints;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#getTargetEdiParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEdiParts(EditPart editpart) {
		EditPart targetEP = null;
		List editParts = new ArrayList();
		if (editpart instanceof ConnectionNodeEditPart) {
			final ConnectionNodeEditPart conEP = (ConnectionNodeEditPart) editpart;
			MEditingDomain editingDomain = MEditingDomainGetter.getMEditingDomain((View)editpart.getModel());
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
	 * Returns the request connector label semantic hint
	 * 
	 * @return The request connector label semantic hint
	 */
	protected String[] getLabelSemanticHints() {
		return labelSemanticHints;
	}

}