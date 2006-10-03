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

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectionLabelsRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;


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
            CompositeCommand cc = 
                new CompositeCommand(DiagramUIMessages.Command_hideLabel_Label);
            Object model = getHost().getModel();
            if (model instanceof View){
                View hostView = (View)model;
       			Iterator iter = hostView.getChildren().iterator();
       			while(iter.hasNext()) {
                    View childView = (View)iter.next();
                    if (isLabelView(getHost() ,hostView, childView)){
                        cc.add(new SetPropertyCommand(getEditingDomain(),
                            new EObjectAdapter(childView),
                            Properties.ID_ISVISIBLE,
                            DiagramUIMessages.Command_hideLabel_Label,
                            Boolean.valueOf(showHide)));
                    }
        		}
        		return new ICommandProxy(cc);
            }
		}
		return null;
	}
    
   protected TransactionalEditingDomain getEditingDomain() {
       return ((IGraphicalEditPart) getHost()).getEditingDomain();
   }
    
    
   /**
    * determines if the passed view is a label view or not
    * the default provided implementation is just an educated/generic guss
    * clients can override this method to provide more specific response
    * @param node
    * @return
    */
    protected boolean isLabelView(EditPart containerEditPart,View parentView, View view) {
    	// labels are not compartments
        // labels contained by Node Shape Edit Parts or connection edit parts
        // labels had location constrain
        // labels had the string Type set on them 
        if ((containerEditPart instanceof ShapeNodeEditPart ||
             containerEditPart instanceof ConnectionEditPart )
             &&  view instanceof Node){
            Node node = (Node)view;
            String nodeType = node.getType();
            if (!isCompartment(node) &&
                (nodeType != null && nodeType.length()>0)){
                LayoutConstraint lContraint = node.getLayoutConstraint();
                if (lContraint instanceof Location){
                    return true;
                }
            }
        }
               
       return false;
    }
    
    /**
     * determines if the passed view is a compartment view or not
     * the default provided implementation is just an educated/generic guss
     * clients can override this method to provide more specific response
     * @param node
     * @return
     */
    protected boolean isCompartment(Node node) {
        if (node.getStyle(NotationPackage.eINSTANCE.getDrawerStyle())!=null){
              return true;
        }
        return false;
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
