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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import com.ibm.xtools.notation.View;


/**
 * Component edit policy which sets the <code>Property.ID_ISVISIBLE</code> to
 * <i>false</i> rather than deleting the view.  Doesn't support delete semantic.
 * 
 * @author jcorchis
 */
public class VisibilityComponentEditPolicy
	extends ComponentEditPolicy {
	
	/** 
	 * Return to make the <code>GraphicalEditPart</code>'s figure not visible.
	 * @param deleteRequest the original delete request.
	 */
	protected Command createDeleteViewCommand(GroupRequest deleteRequest) {
		CompositeCommand cc = new CompositeCommand(null);
		List toDel = deleteRequest.getEditParts();
		if (toDel == null || toDel.isEmpty()) {
			SetPropertyCommand c = new SetPropertyCommand(
				PresentationResourceManager.getInstance().getString("Command.hideLabel.Label"), //$NON-NLS-1$
				new EObjectAdapter((View) getHost().getModel()),
				Properties.ID_ISVISIBLE, new Boolean(
					false));
			cc.compose(c);
		} else {
			for (int i = 0; i < toDel.size(); i++) {
				IGraphicalEditPart gep = (IGraphicalEditPart) toDel.get(i);
				SetPropertyCommand c = new SetPropertyCommand(
					PresentationResourceManager.getInstance().getString("Command.hideLabel.Label"), //$NON-NLS-1$
					new EObjectAdapter((View)gep.getModel()),
					Properties.ID_ISVISIBLE,
					new Boolean(false));
				cc.compose(c);
			}
		}
		return new EtoolsProxyCommand(cc.unwrap());		
	}

	/** 
	 * Returns null.
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest the original delete request.
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
		return null;
	}


}
