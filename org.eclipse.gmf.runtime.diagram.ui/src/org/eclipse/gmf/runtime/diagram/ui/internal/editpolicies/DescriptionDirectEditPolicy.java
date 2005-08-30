/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import com.ibm.xtools.notation.View;

/**
 * A direct edit policy for DescriptionCompartmentEditParts.
 * These edit parts do not necessarily have a model reference.
 * The direct edit policy will support editing for those edit parts
 * that do not have a model reference.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DescriptionDirectEditPolicy extends LabelDirectEditPolicy {


	protected Command getDirectEditCommand(DirectEditRequest edit) {
		String labelText = (String) edit.getCellEditor().getValue();
		TextCompartmentEditPart compartment =
			(TextCompartmentEditPart) getHost();
		View primaryView = compartment.getPrimaryView();
		IAdaptable elementAdapter = new EObjectAdapter(primaryView);

		// check to make sure an edit has occurred before returning a command.
		String prevText =
			compartment.getParser().getEditString(elementAdapter, 0);
		if (!prevText.equals(labelText)) {
			ICommand iCommand =
				compartment.getParser().getParseCommand(
					elementAdapter,
					labelText,
					0);
			return new EtoolsProxyCommand(iCommand);
		}

		return null;
	}

}
