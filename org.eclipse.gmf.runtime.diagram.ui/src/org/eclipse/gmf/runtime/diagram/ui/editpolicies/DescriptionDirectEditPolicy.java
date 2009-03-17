/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

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
		ITextAwareEditPart compartment =
			(ITextAwareEditPart) getHost();
        IGraphicalEditPart gep = (IGraphicalEditPart)getHost();
        
		View primaryView = gep.getPrimaryView();
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
			return new ICommandProxy(iCommand);
		}

		return null;
	}

}
