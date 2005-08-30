/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import com.ibm.xtools.notation.View;


/**
 * EditPolicy which support the RequestConstants.REQ_SNAP_BACK request type.
 * 
 * Returns a command to position a label's offset to it default set at creation.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class LabelSnapBackEditPolicy
	extends GraphicalNodeEditPolicy {


	/**
	 * Understands RequestConstants.REQ_SNAP_BACK request types
	 * @param request
	 * @return boolean
	 */
	public boolean understandsRequest(Request request) {
		return RequestConstants.REQ_SNAP_BACK.equals(request.getType());
	}
	
	/**
	 * Returns a <code>Command</code> which sets the label's offset to its
	 * original position.
	 * 
	 * @param request the request
	 * @return the command
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_SNAP_BACK.equals(request.getType())) {
			View view = (View)getHost().getModel();
			String hint = view.getType();
			Point offset = LabelEditPart.getSnapBackPosition(hint);
			if (offset == null)
				return null;
	 		ICommand moveCommand = 
	 			new SetBoundsCommand(
	 				PresentationResourceManager.getI18NString("SetLocationCommand.Label.Resize"),//$NON-NLS-1$
	 				new EObjectAdapter(view),
					offset); 
			return new EtoolsProxyCommand(moveCommand);
		}
		return null;
	}
}
