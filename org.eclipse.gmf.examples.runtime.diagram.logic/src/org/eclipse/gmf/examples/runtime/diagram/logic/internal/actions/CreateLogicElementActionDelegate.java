/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;

public class CreateLogicElementActionDelegate extends AbstractActionDelegate
		implements IObjectActionDelegate {

	protected void doRun(IProgressMonitor progressMonitor) {

		// Action ID corresponds to the ID of the element type to be created
		String actionId = getAction().getId();
		IElementType elementType = ElementTypeRegistry.getInstance().getType(
				actionId);

		if (elementType == null) {
			// Problem in the Action contribution XML
			throw new IllegalArgumentException("Action id '" //$NON-NLS-1$
					+ actionId
					+ "' does not correspond to an existing element type ID."); //$NON-NLS-1$
		}

		// Get the selected edit part
		IStructuredSelection structuredSelection = getStructuredSelection();
		Object selection = structuredSelection.getFirstElement();

		if (!(selection instanceof IGraphicalEditPart)) {
			// Action enablement criteria expected to prevent this
			throw new IllegalArgumentException("Selected element '" //$NON-NLS-1$
					+ selection + "' is not an IGraphicalEditPart."); //$NON-NLS-1$
		}

		IGraphicalEditPart container = (IGraphicalEditPart) selection;

		// Get the command to create the new element and its view
		CreateViewRequest createRequest = CreateViewRequestFactory
				.getCreateShapeRequest(elementType, container
						.getDiagramPreferencesHint());

		Command command = container.getCommand(createRequest);

		if (command == null || !(command.canExecute())) {
			// Action enablement criteria expected to prevent this
			throw new IllegalArgumentException("Command for '" //$NON-NLS-1$
					+ actionId + "' is not executable."); //$NON-NLS-1$
		}

		// Create the new element
		DiagramCommandStack commandStack = container.getDiagramEditDomain()
				.getDiagramCommandStack();
		commandStack.execute(command);
	}
}
