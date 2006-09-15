/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableShapeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * the base controler for shapes
 * @author mmostafa
 *
 */
public abstract class ShapeEditPart extends TopGraphicEditPart implements IPrimaryEditPart {

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
		else if (NotationPackage.eINSTANCE.getFontStyle().isInstance(notification.getNotifier()))
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
		return new ResizableShapeEditPolicy();
	}
}
