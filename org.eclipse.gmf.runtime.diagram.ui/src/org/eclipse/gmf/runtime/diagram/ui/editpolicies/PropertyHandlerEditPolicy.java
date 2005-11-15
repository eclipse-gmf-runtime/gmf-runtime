/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewRefactorHelper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ApplyAppearancePropertiesRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw 
 *
 * Edit policy to handle any property change requests.  In the case of the change
 * request for ID_AUTOSIZE we will create a AUTO_SIZE request and return the command 
 * for that instead of a property change request.
 */
public class PropertyHandlerEditPolicy extends AbstractEditPolicy {

	static private final String APPLY_APPEARANCE_PROPERTIES_UNDO_COMMAND_NAME = "Apply appearance properties"; //$NON-NLS-1$
	
	/**
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {

		if (!understandsRequest(request)) {
			return null;
		}

		if (request.getType().equals(RequestConstants.REQ_PROPERTY_CHANGE)) {

			ChangePropertyValueRequest cpvr =
				(ChangePropertyValueRequest) request;
			EditPart ep = getHost();

			if (ep instanceof IGraphicalEditPart) {
				View view = (View)((IGraphicalEditPart) ep).getModel();

				if (ViewUtil.isPropertySupported(view,cpvr.getPropertyID())) {
					return new EtoolsProxyCommand(
						new SetPropertyCommand(
							new EObjectAdapter(view),
							cpvr.getPropertyID(),
							cpvr.getPropertyName(),
							((ChangePropertyValueRequest) request).getValue()));
				}
			}
		} else if (
			request instanceof ApplyAppearancePropertiesRequest
				&& getHost() instanceof IGraphicalEditPart) {

			final ApplyAppearancePropertiesRequest aapr =
				(ApplyAppearancePropertiesRequest) request;

			final IGraphicalEditPart gep = (IGraphicalEditPart)getHost();
			final ViewRefactorHelper vrh = new ViewRefactorHelper(gep.getDiagramPreferencesHint());
			final List exclusions = getStyleExclusionsForCopyAppearance();
			
			ICommand viewStyleCommand = new AbstractModelCommand(APPLY_APPEARANCE_PROPERTIES_UNDO_COMMAND_NAME, null) {//$NON-NLS-1$
				protected CommandResult doExecute(IProgressMonitor progressMonitor) {
					
					vrh.copyViewAppearance(aapr.getViewToCopyFrom(), gep.getNotationView(), exclusions);
					return newOKCommandResult();
				}
			};
			
			return new EtoolsProxyCommand(viewStyleCommand);
		}

		return null;
	}

	/**
	 * @return a <code>List</code> of <code>EClass</code> <code>Style</code> types that are
	 * to be excluded from the copy process.
	 */
	protected List getStyleExclusionsForCopyAppearance() {
		List exclusions = new ArrayList();
		exclusions.add(NotationPackage.eINSTANCE.getDescriptionStyle());
		exclusions.add(NotationPackage.eINSTANCE.getImageBufferStyle());
		return exclusions;
	}
	
	/**
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
	 */
	public boolean understandsRequest(Request request) {
		if (request.getType().equals(RequestConstants.REQ_PROPERTY_CHANGE))
			return true;

		if (request instanceof ApplyAppearancePropertiesRequest
			&& getHost() instanceof IGraphicalEditPart)
			return true;
		return super.understandsRequest(request);
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (!understandsRequest(request))
			return null;

		if (request.getType().equals(RequestConstants.REQ_PROPERTY_CHANGE)) {
			return getHost();
		} else if (request instanceof ApplyAppearancePropertiesRequest) {
			return getHost();
		}
		return super.getTargetEditPart(request);
	}

}
