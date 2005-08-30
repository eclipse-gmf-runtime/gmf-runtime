/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ApplyAppearancePropertiesRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
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

			ApplyAppearancePropertiesRequest aapr =
				(ApplyAppearancePropertiesRequest) request;

			CompoundCommand cc =
				new CompoundCommand(APPLY_APPEARANCE_PROPERTIES_UNDO_COMMAND_NAME);

			Iterator semanticHints = aapr.getSemanticHints().iterator();
			IGraphicalEditPart part = (IGraphicalEditPart) getHost();
			View view = part.getNotationView();
			String semanticHint = ""; //$NON-NLS-1$
			if (view!=null)
				semanticHint = view.getType();
			
			while (semanticHints.hasNext()) {
				// iterate through all factory hints
				String hint = (String) semanticHints.next();
				// find out the target of the future  request
				IGraphicalEditPart target =
					hint.equals(semanticHint)
						? part
						: part.getChildBySemanticHint(hint);

				if (target != null) {
					Dictionary properties = aapr.getPropertiesFor(hint);
					Enumeration propertyIDs = properties.keys();

					while (propertyIDs.hasMoreElements()) {
						// iterate through all the properties applicable to this target
						String propertyID = (String) propertyIDs.nextElement();

						// create a request											
						ChangePropertyValueRequest cpvr =
							new ChangePropertyValueRequest(
								APPLY_APPEARANCE_PROPERTIES_UNDO_COMMAND_NAME,
								propertyID,
								properties.get(propertyID));
						Command command = target.getCommand(cpvr);
						if (command != null)
							// double check if the property is supported
							cc.add(command);
					}
				}
			}

			return cc;

		}

		return null;
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
