/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;


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
		CompositeCommand cc = new CompositeCommand(StringStatics.BLANK);
		List toDel = deleteRequest.getEditParts();
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		if (toDel == null || toDel.isEmpty()) {
			SetPropertyCommand c = new SetPropertyCommand(editingDomain,
				DiagramUIMessages.Command_hideLabel_Label,
				new EObjectAdapter((View) getHost().getModel()),
				Properties.ID_ISVISIBLE, Boolean.FALSE);
			cc.compose(c);
		} else {
			for (int i = 0; i < toDel.size(); i++) {
				IGraphicalEditPart gep = (IGraphicalEditPart) toDel.get(i);
				SetPropertyCommand c = new SetPropertyCommand(editingDomain,
					DiagramUIMessages.Command_hideLabel_Label,
					new EObjectAdapter((View)gep.getModel()),
					Properties.ID_ISVISIBLE,
					Boolean.FALSE);
				cc.compose(c);
			}
		}
		return new ICommandProxy(cc.reduce());		
	}

	/** 
	 * Returns null.
	 * @see #shouldDeleteSemantic()
	 * @param deleteRequest the original delete request.
	 */
	protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
		return null;
	}
    
	protected Command getDeleteCommand(GroupRequest deleteRequest) {
        // Override this method to avoid prompt when deleting views with no
        // semantic meaning. See bugzilla 158845.
        if (shouldDeleteSemantic()) {
            return createDeleteSemanticCommand(deleteRequest);
        }
        return createDeleteViewCommand(deleteRequest);
    }


}
