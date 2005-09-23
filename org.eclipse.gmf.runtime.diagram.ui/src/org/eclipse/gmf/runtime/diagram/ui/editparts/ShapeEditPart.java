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

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ActionBarEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ShapeResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
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
	 * a static array of appearance property ids applicable to shapes
	 */
	private static final String[] appearanceProperties =
		new String[] {
			Properties.ID_FONTNAME,
			Properties.ID_FONTSIZE,
			Properties.ID_FONTBOLD,
			Properties.ID_FONTITALIC,
			Properties.ID_FONTCOLOR,
			Properties.ID_LINECOLOR,
			Properties.ID_FILLCOLOR};

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
		installEditPolicy(EditPolicyRoles.ACTIONBAR_ROLE, new ActionBarEditPolicy());
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
			setBackgroundColor(PresentationResourceManager.getInstance().getColor(c));
		} 
		else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature)) {
			Integer c = (Integer) notification.getNewValue();
			setForegroundColor(PresentationResourceManager.getInstance().getColor(c));
		} 
		else if (NotationPackage.eINSTANCE.getFontStyle().isInstance(notification.getNotifier()))
			refreshFont();
		else if (notification.getFeature() == NotationPackage.eINSTANCE.getView_Element()
		 && ((EObject)notification.getNotifier())== getNotationView())
			handleMajorSemanticChange();

		else if (notification.getEventType() == EventTypes.UNRESOLVE 
				&& notification.getNotifier() == ((View)getModel()).getElement())
			handleMajorSemanticChange();

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
	 * Returns an array of the appearance property ids applicable to the receiver.
	 * Fro this type it is  Properties.ID_FONT, Properties.ID_FONTCOLOR, Properties.ID_LINECOLOR, Properties.ID_FILLCOLOR,Properties.ID_AUTOSIZE 
	 * 
	 * @return - an array of the appearance property ids applicable to the receiver
	 */
	private String[] getAppearancePropertyIDs() {
		return appearanceProperties;
	}

	/* Contributing appearance properties specific to this class definition
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#fillAppearancePropertiesMap(java.util.Map)
	 */
	public void fillAppearancePropertiesMap(Map properties) {

		super.fillAppearancePropertiesMap(properties);

		Dictionary local_properties =
			(Dictionary) properties.get(
				((View)getModel()).getType());

		if (local_properties == null) {
			local_properties = new Hashtable();
		}

		if (getAppearancePropertyIDs().length > 0) {
			// only if there are any appearance properties
			for (int i = 0; i < getAppearancePropertyIDs().length; i++){
				ENamedElement element = MetaModelUtil.getElement(getAppearancePropertyIDs()[i]);
				if (element instanceof EStructuralFeature)
					local_properties.put(
						getAppearancePropertyIDs()[i],
						getStructuralFeatureValue((EStructuralFeature)element));
			}
			properties.put(
				((View)getModel()).getType(),
				local_properties);
		}
	}

	/**
	 * Return the editpolicy to be installed as an <code>EditPolicy#PRIMARY_DRAG_ROLE</code>
	 * role.  This method is typically called by <code>LayoutEditPolicy#createChildEditPolicy()</code>
	 * @return EditPolicy
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		return new ShapeResizableEditPolicy();
	}
}
