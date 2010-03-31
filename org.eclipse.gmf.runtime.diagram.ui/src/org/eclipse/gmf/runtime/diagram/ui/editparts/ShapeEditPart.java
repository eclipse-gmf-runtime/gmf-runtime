/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableShapeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * the base controler for shapes
 * @author mmostafa, crevells
 *
 */
public abstract class ShapeEditPart extends TopGraphicEditPart implements IPrimaryEditPart {

    /**
	 * A <code>ContainerEditPolicy</code> for a <code>ShapeEditPart</code> that
	 * lays out compartments contained by the host editpart for
	 * "arrange selection" request.
	 * <p>
	 * <b>Note:</b> it is not used by default, because a graph layout algorithm
	 * may support arrange of 1 node in a graph. Therefore, the expected
	 * behaviour is non-deterministic and is up to the API client
	 * 
	 * @since 2.1
	 */
    protected static class ShapeContainerEditPolicy
        extends ContainerEditPolicy {
    	
    	/**
    	 * Constructor for ShapeContainerEditPolicy
		 * @since 1.4
		 */
    	public ShapeContainerEditPolicy() {
			super();			
		}
    	
        protected Command getArrangeCommand(ArrangeRequest request) {
            if (ActionIds.ACTION_ARRANGE_SELECTION.equals(request.getType())
                || ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION.equals(request
                    .getType())) {
                List parts = request.getPartsToArrange();
                if (parts.size() == 1 && parts.contains(getHost())) {
                    // Create arrange commands for the compartments within this shape.
                    CompoundCommand cc = new CompoundCommand();
                    for (Iterator iterator = getHost().getChildren().iterator(); iterator
                        .hasNext();) {
                        Object childEP = iterator.next();
                        if (childEP instanceof CompartmentEditPart
                            && ((CompartmentEditPart) childEP).getContentPane()
                                .getLayoutManager() instanceof XYLayout) {
                            ArrangeRequest newRequest = createRequest(request,
                                ((CompartmentEditPart) childEP).getChildren());
                            cc.add(super.getArrangeCommand(newRequest));
                        }
                    }
                    return cc;
                }
            }
            return super.getArrangeCommand(request);
        }

        private ArrangeRequest createRequest(ArrangeRequest request,
                List partsToArrange) {
            ArrangeRequest newRequest = new ArrangeRequest((String) request
                .getType(), request.getLayoutType());
            newRequest.setExtendedData(request.getExtendedData());
            newRequest.setPartsToArrange(partsToArrange);
            return newRequest;
        }
    }
    
	/**
	 * copnstructor
	 * @param view the view controlled by this edit part
	 */
	public ShapeEditPart(View view) {
		super(view);
	}
    
    
    protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy());
		installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE, new PopupBarEditPolicy());
	}

	/**
	 * gets the location of this edit part's Figure
	 * @return <code>Point</code>
	 */
	final public Point getLocation() {
		return getFigure().getBounds().getLocation();
	}

	/**
	 * gets the size of this edit part's Figure 
	 * @return <code>Dimension</code>
	 */
	final public Dimension getSize() {
		return getFigure().getBounds().getSize();
	}

	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.eINSTANCE.getSize_Width().equals(feature)
			|| NotationPackage.eINSTANCE.getSize_Height().equals(feature)
			|| NotationPackage.eINSTANCE.getLocation_X().equals(feature)
			|| NotationPackage.eINSTANCE.getLocation_Y().equals(feature)) {
			refreshBounds();
		} 
		else if (NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {
			Integer c = (Integer) notification.getNewValue();
			setBackgroundColor(DiagramColorRegistry.getInstance().getColor(c));
		} 
		else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature)) {
			Integer c = (Integer) notification.getNewValue();
			setForegroundColor(DiagramColorRegistry.getInstance().getColor(c));
		} 
		else if (NotationPackage.eINSTANCE.getFontStyle().getEAllAttributes().contains(feature))
			refreshFont();
		else if (notification.getFeature() == NotationPackage.eINSTANCE.getView_Element()
		 && ((EObject)notification.getNotifier())== getNotationView())
			handleMajorSemanticChange();
        else if (notification.getEventType() == EventType.UNRESOLVE && hasNotationView()){
            // make sure we refresh if the unresolved element is the edit
            // part's semantic element the comparison should be id based not
            // instance based, since get element will resolve the element
            // and resolving the element will  result in returning a different
            // instance than the proxy we had as a notifier
            EObject notifier = (EObject) notification.getNotifier();
            EObject viewElement = getNotationView().getElement();
            if (viewElement!=null){
                String id1 = EMFCoreUtil.getProxyID(notifier);
                String id2 = EMFCoreUtil.getProxyID(viewElement);
                if (id1.equals(id2)) {
                    handleMajorSemanticChange();
                }
            }
         }
         else
			super.handleNotificationEvent(notification);
	}

	
	/**
	 * refresh the bounds 
	 */
	protected void refreshBounds() {
		int width = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Width())).intValue();
		int height = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Height())).intValue();
		Dimension size = new Dimension(width, height);
		int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_X())).intValue();
		int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getLocation_Y())).intValue();
		Point loc = new Point(x, y);
		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			new Rectangle(loc, size));
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshBounds();
		refreshBackgroundColor();
		refreshForegroundColor();
		refreshFont();
	}

	/**
	 * Return the editpolicy to be installed as an <code>EditPolicy#PRIMARY_DRAG_ROLE</code>
	 * role.  This method is typically called by <code>LayoutEditPolicy#createChildEditPolicy()</code>
	 * @return EditPolicy
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		EditPolicy policy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		return policy != null ? policy : new ResizableShapeEditPolicy();
	}

    public EditPart getTargetEditPart(Request request) {

        if (RequestConstants.REQ_SELECTION == request.getType()
            && getParent() instanceof GroupEditPart) {
            
            // If the shape is already selected then do not give up selection to
            // the group.
            if (getSelected() != SELECTED_NONE) {
                return super.getTargetEditPart(request);
            }

            GroupEditPart groupEP = (GroupEditPart) getParent();

            // Normally when a shape is not selected, the right-mouse button
            // will cause the shape to be selected and the context menu to show.
            // If the shape is in a group, we do not want this behavior as we
            // want the context menu of the group to show.
            if (getSelected() == SELECTED_NONE
                && (request instanceof SelectionRequest)
                && ((SelectionRequest) request).getLastButtonPressed() == 3) {
                return groupEP.getTargetEditPart(request);
            }
          
            // If the group is currently selected, then this is the second click
            // then the shape should be selected.
            if (groupEP.getSelected() != SELECTED_NONE) {
                return super.getTargetEditPart(request);
            }

            // If any of the group's children are currently selected then the
            // selection of another child of the group will result in the child
            // being selected and not the group.
            for (Iterator iter = groupEP.getChildren().iterator(); iter
                .hasNext();) {
                EditPart childEP = (EditPart) iter.next();
                if (childEP.getSelected() != SELECTED_NONE) {
                    return super.getTargetEditPart(request);
                }

            }

            // otherwise we want the group to get selected
            return groupEP.getTargetEditPart(request);
        }

        return super.getTargetEditPart(request);
    }

     
}
