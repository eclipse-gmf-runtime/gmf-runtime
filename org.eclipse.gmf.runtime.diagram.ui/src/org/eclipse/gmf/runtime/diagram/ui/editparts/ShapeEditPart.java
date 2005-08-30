/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ActionBarEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ShapeResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.View;

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

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Properties.ID_EXTENTX)
			|| evt.getPropertyName().equals(Properties.ID_EXTENTY)
			|| evt.getPropertyName().equals(Properties.ID_POSITIONX)
			|| evt.getPropertyName().equals(Properties.ID_POSITIONY)) {
			refreshBounds();
		} 
		else if (evt.getPropertyName().equals(Properties.ID_FILLCOLOR)) {
			Integer c = (Integer) evt.getNewValue();
			setBackgroundColor(PresentationResourceManager.getInstance().getColor(c));
		} 
		else if (evt.getPropertyName().equals(Properties.ID_LINECOLOR)) {
			Integer c = (Integer) evt.getNewValue();
			setForegroundColor(PresentationResourceManager.getInstance().getColor(c));
		} 
		else {
			super.handlePropertyChangeEvent(evt);
		}
	}

	protected void handleNotificationEvent(NotificationEvent e) {
		Notification event = e.getNotification();
		
		if (NotationPackage.eINSTANCE.getFontStyle().isInstance(e.getElement()))
			refreshFont();
		else if (event.getFeature() == NotationPackage.eINSTANCE.getView_Element()
		 && ((EObject)event.getNotifier())== getNotationView())
			handleMajorSemanticChange();

		else if (event.getEventType() == EventTypes.UNRESOLVE 
				&& event.getNotifier() == ((View)getModel()).getElement())
			handleMajorSemanticChange();

		else
			super.handleNotificationEvent(e);
	}

	/**
	 * refresh the bounds 
	 */
	protected void refreshBounds() {
		int width = ((Integer) getPropertyValue(Properties.ID_EXTENTX)).intValue();
		int height = ((Integer) getPropertyValue(Properties.ID_EXTENTY)).intValue();
		Dimension size = new Dimension(width, height);
		int x = ((Integer) getPropertyValue(Properties.ID_POSITIONX)).intValue();
		int y = ((Integer) getPropertyValue(Properties.ID_POSITIONY)).intValue();
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

			for (int i = 0; i < getAppearancePropertyIDs().length; i++)
				local_properties.put(
					getAppearancePropertyIDs()[i],
					getPropertyValue(getAppearancePropertyIDs()[i]));
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
