/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectionLabelsRequest;


/**
 * EditPolicy which toggles the visibility of the labels associated with
 * the host.
 * 
 * @author jcorchis
 */
public class ConnectionLabelsEditPolicy extends AbstractEditPolicy {
	
	/**
	 * Understands the RequestConstants.REQ_TOGGLE_CONNECTION_LABELS request. 
	 * @param request the request
	 * @return true if RequestConstants.REQ_TOGGLE_CONNECTION_LABELS.equals(request.getType())
	 * and false otherwise.
	 */
	public boolean understandsRequest(Request request) {
		return RequestConstants.REQ_TOGGLE_CONNECTION_LABELS.equals(request
				.getType());
	}
	/**
	 * Returns a <code>Command<code> which changes the visibility for 
	 * the labels owned by the host.
	 * @param request the request
	 * @return the property change commands
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_TOGGLE_CONNECTION_LABELS.equals(request.getType())) {
			boolean showHide = ((ToggleConnectionLabelsRequest) request)
					.showConnectionLabel();		
			CompoundCommand cc = new CompoundCommand();
			List children = getHost().getChildren();
			Iterator iter = children.iterator();
			while(iter.hasNext()) {
				GraphicalEditPart ep = (GraphicalEditPart) iter.next();	
				if (ep instanceof LabelEditPart) {
					ChangePropertyValueRequest req = new ChangePropertyValueRequest(
						DiagramResourceManager.getInstance().getString("Command.hideLabel.Label"), //$NON-NLS-1$
						Properties.ID_ISVISIBLE,
						Boolean.valueOf(showHide));
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
			Command command = getHost().getCommand(
				new ToggleConnectionLabelsRequest(false));
			if (command != null && command.canExecute())
				return getHost();
		}
		return null;
	}
}
