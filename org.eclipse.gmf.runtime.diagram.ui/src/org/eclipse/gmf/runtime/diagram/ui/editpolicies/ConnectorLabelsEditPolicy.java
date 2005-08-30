/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectorLabelsRequest;


/**
 * EditPolicy which toggles the visibility of the labels associated with
 * the host.
 * 
 * @author jcorchis
 */
public class ConnectorLabelsEditPolicy extends AbstractEditPolicy {
	
	/**
	 * Understands the RequestConstants.REQ_TOGGLE_CONNECTOR_LABELS request. 
	 * @param request the request
	 * @return true if RequestConstants.REQ_TOGGLE_CONNECTOR_LABELS.equals(request.getType())
	 * and false otherwise.
	 */
	public boolean understandsRequest(Request request) {
		return RequestConstants.REQ_TOGGLE_CONNECTOR_LABELS.equals(request
				.getType());
	}
	/**
	 * Returns a <code>Command<code> which changes the visibility for 
	 * the labels owned by the host.
	 * @param request the request
	 * @return the property change commands
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_TOGGLE_CONNECTOR_LABELS.equals(request.getType())) {
			boolean showHide = ((ToggleConnectorLabelsRequest) request)
					.showConnectorLabel();		
			CompoundCommand cc = new CompoundCommand();
			List children = getHost().getChildren();
			Iterator iter = children.iterator();
			while(iter.hasNext()) {
				GraphicalEditPart ep = (GraphicalEditPart) iter.next();	
				if (ep instanceof LabelEditPart) {
					ChangePropertyValueRequest req = new ChangePropertyValueRequest(
						PresentationResourceManager.getInstance().getString("Command.hideLabel.Label"), //$NON-NLS-1$
						Properties.ID_ISVISIBLE,
						new Boolean(showHide));
					Command setLabelVisCmd = ep.getCommand(req);
					if (setLabelVisCmd != null && setLabelVisCmd.canExecute())
						cc.add(setLabelVisCmd);
				}
			}
			return cc;
		}
		return null;
	}
	
	/**
	 * If the request returns an executable command the host is returned, otherwise null.
	 * @param request
	 * @return getHost() if the request is supported or null.
	 */
	public EditPart getTargetEditPart(Request request) {
		if (understandsRequest(request)) {
			Command command = getHost().getCommand(new ToggleConnectorLabelsRequest(false));
			if (command != null && command.canExecute())
				return getHost();
		}
		return null;
	}
}
