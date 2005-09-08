/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelNonResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.VisibilityComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.LabelLocator;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.LabelSnapBackEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.ResizableLabelLocator;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.LabelViewConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Controller for all label that deals with interactions bewteen
 * the label figure the underline editpart
 * 
 * @author jcorchis
 */
public class LabelEditPart extends TopGraphicEditPart {
		
	private String semanticHint = null;	
	
	/** 
	 * Map which maintains the default offsets for labels.
	 * Each plugin which overrides LabelEditPart and wants to have snap back action
	 * support must provide put an entry into this map.  The entry consists of the view's
	 * creation hint (key) and a <code>Point</code> (value) which constains the label's default
	 * offset.
	 */
	private static HashMap snapBackMap = new HashMap();

	/**
	 * Registers the default snap back position for this label that is retrievable from
	 * the editpolicy that creates the command to move the label back to it's original position.
	 * Subclasses of <code>LabelEditPart</code> should call this to register their labels default
	 * position.
	 * 
	 * @param propertyName <code>String</code> that is usually the property identifier for the label or
	 * it can be any unique identifier the label subclass desires.
	 * @param offset <code>Point</code> that is the offset Point position from the keypoint.
	 */
	public static void registerSnapBackPosition(String propertyName, Point offset) {
		snapBackMap.put(propertyName, offset);
	}
	
	/**
	 * Retrieves the default snap back position for this label that was registered using using
	 * the {@link LabelEditPart#registerSnapBackPosition(String, Point)} method.
	 * 
	 * @param propertyName <code>String</code> that is usually the property identifier for the label or
	 * it can be any unique identifier the label subclass desires.
	 * @return offset <code>Point</code> that is the offset Point position from the keypoint.
	 */
	public static Point getSnapBackPosition(String propertyName) {
		return (Point)snapBackMap.get(propertyName);
	}

	/**
	 * constructor 
	 * @param view this edit part's view
	 */
	public LabelEditPart(View view) {
		super(view);
	}

	protected IFigure createFigure() {
		IFigure label = new Figure();
		label.setCursor(Cursors.ARROW);
		label.setLayoutManager(new ConstrainedToolbarLayout());
		return label;
	}
	
	/**
	 * Returns the model's semantic hint.  This is used to calculate the reference
	 * point for the label's figure.
	 * @return the semantic type
	 */
	protected String getSemanticType() {
		if (semanticHint == null) {
			semanticHint = ((String) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {
				public Object run() {
					return new String(((View)getModel()).getType());
				}
			}));
		}
		return semanticHint;
	}


	public void refresh() {
		super.refresh();
		refreshBounds();
	}
	
	/**
	 * Updates the locator based on the changes to the offset. 
	 */
	public void refreshBounds() {
		// try to handle both of resizable and nonresizable labels
		if (isResizable()){
			handleResizableRefreshBounds();
		} else {
			handleNonResizableRefreshBoundS();
		}
	}

	/**
	 * handles non resizable lable refresh bounds
	 */
	private void handleNonResizableRefreshBoundS() {
		int dx = ((Integer) getPropertyValue(Properties.ID_POSITIONX))
			.intValue();
		int dy = ((Integer) getPropertyValue(Properties.ID_POSITIONY))
			.intValue();
		Point offset = new Point(dx, dy);
		if (getParent() instanceof AbstractConnectionEditPart) {
			((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), new LabelLocator(
					((AbstractConnectionEditPart) getParent())
						.getConnectionFigure(), offset, getKeyPoint()));
		} else {
			getFigure().getParent().setConstraint( getFigure(),  new LabelLocator(
				getFigure().getParent(),
					offset, getKeyPoint()));
		}
		
	}

	/**
	 * handles resizable lable refresh bounds
	 */
	private void handleResizableRefreshBounds() {
		int dx = ((Integer) getPropertyValue(Properties.ID_POSITIONX))
			.intValue();
		int dy = ((Integer) getPropertyValue(Properties.ID_POSITIONY))
			.intValue();
		int width = ((Integer) getPropertyValue(Properties.ID_EXTENTX))
			.intValue();
		int height = ((Integer) getPropertyValue(Properties.ID_EXTENTY))
			.intValue();
		Rectangle rectangle = new Rectangle(dx,dy,width,height);
		if (getParent() instanceof AbstractConnectionEditPart) {
			((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), new ResizableLabelLocator(
					((AbstractConnectionEditPart) getParent())
						.getConnectionFigure(), rectangle, getKeyPoint()));
		} else {
			getFigure().getParent().setConstraint( getFigure(),  new ResizableLabelLocator(
				getFigure().getParent(),
				rectangle, getKeyPoint()));
		}
	}

	/**
	 * check if the edit part had a resizable edit policy installed or not 
	 * @return	true is resizable edit policy is installed otherwise false
	 */
	private boolean isResizable() {
		EditPolicy editPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if(editPolicy instanceof ResizableEditPolicy )
			return true;
		return false;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
			EditPolicy.PRIMARY_DRAG_ROLE,
			new LabelNonResizableEditPolicy());
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new VisibilityComponentEditPolicy());
		installEditPolicy(
			EditPolicyRoles.SNAP_FEEDBACK_ROLE,
			new LabelSnapBackEditPolicy());
	}

	/** Return a {@link DragTracker} instance. */
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTrackerEx(this) {
			protected boolean isMove() {
				return true;
			}
		};
	}

	/**
	 * Method isSnapBackNeeded.
	 * @return boolean
	 *
	 * returns false only if the current location of the figure
	 * is not default one
	 */
	public boolean isSnapBackNeeded() {
		return true;
	}


	/**
	 * 
	 * @see AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshForegroundColor();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (   evt.getPropertyName().equals(Properties.ID_POSITIONX) 
				|| evt.getPropertyName().equals(Properties.ID_POSITIONY) 
				|| evt.getPropertyName().equals(Properties.ID_EXTENTX)
				|| evt.getPropertyName().equals(Properties.ID_EXTENTY)) {
			refreshBounds();
		}else if (evt.getPropertyName().equals(Properties.ID_LINECOLOR)){
			Integer c = (Integer) evt.getNewValue();
			setForegroundColor(PresentationResourceManager.getInstance().getColor(c));
		}
		else
			super.handlePropertyChangeEvent(evt);
	}

	protected void addNotationalListeners() {
		super.addNotationalListeners();
		addListenerFilter("PrimaryView", this, ViewUtil.getPropertyChangeNotifier(getPrimaryView())); //$NON-NLS-1$
	}

	protected void removeNotationalListeners() {
		super.removeNotationalListeners();
		removeListenerFilter("PrimaryView"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accessibleEP == null)
			accessibleEP = new AccessibleGraphicalEditPart() {
			public void getName(AccessibleEvent e) {
				e.result = getAccessibleText();
			}
		};
		return accessibleEP;
	}

	/**
	 * Concatenates the text of all the text compartment children of
	 * this editpart to be used as the accessible text.
	 * @return String the string to be used as the accessible text
	 */
	protected String getAccessibleText() {
		String accessibleString = ""; //$NON-NLS-1$
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			IGraphicalEditPart ep = (IGraphicalEditPart) iter.next();
			if (ep instanceof TextCompartmentEditPart) {
				IFigure fig = ep.getFigure();
				if (fig instanceof WrapLabel) {
					accessibleString += ((WrapLabel)fig).getText() + " "; //$NON-NLS-1$
				}
			}
		}
		return accessibleString;
	}

	/**
	 * Returns an object which is an instance of the given class
	 * associated with this object.  If there are specific class types
	 * that should be delegated to the connector editpart they must
	 * be explicitely handled here (e.g. <code>IPropertySource</code>
	 * is delegated to the connector editpart so that the property pages
	 * reflect the connector when the label is selected).  Otherwise, 
	 * the adapter from this editpart is first retrieved; if this is 
	 * null, then the adapter from the connector editpart is returned.
	 * This means if a client asks for <code>IView</code> the label view
	 * will be returned, but if a client asks for <code>IPrimaryView</code>
	 * the owner view will be returned.
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IPropertySource.class) {
			return getParent().getAdapter(key);
		}
		Object adapterFromSuper = super.getAdapter(key);
		if (adapterFromSuper == null) {
			return getParent().getAdapter(key);
		}
		return adapterFromSuper;
	}
	
	/**
	 * Returns a ConnectionLocator.MIDDLE as the key point.
	 * @return the key point
	 */
	public int getKeyPoint() {
		return ConnectionLocator.MIDDLE;		
	}
	
	/**
	 * Returns a <code>Point</code> located on the parent which is
	 * used by the LabelEditPart to orient itself.
	 * @return the anchorPoint
	 */
	public Point getReferencePoint() {
		if (getParent() instanceof AbstractConnectionEditPart) {
			switch (getKeyPoint()) {
				case ConnectionLocator.TARGET:
					return calculateRefPoint(LabelViewConstants.SOURCE_LOCATION);
				case ConnectionLocator.SOURCE:
					return calculateRefPoint(LabelViewConstants.TARGET_LOCATION);
				case ConnectionLocator.MIDDLE:
					return calculateRefPoint(LabelViewConstants.MIDDLE_LOCATION);
				default:
					return calculateRefPoint(LabelViewConstants.MIDDLE_LOCATION);
			}
		} 
		
		return ((AbstractGraphicalEditPart)getParent()).getFigure().getBounds().getTopLeft();
	}
	
	/**
	 * Calculates a point located at a percentage of the connector 
	 * @param percent
	 * @return the point
	 */
	private Point calculateRefPoint(int percent) {
		if (getParent() instanceof AbstractConnectionEditPart) {
			PointList ptList = ((Connection)((ConnectionEditPart)getParent()).getFigure()).getPoints();			
			Point refPoint = PointListUtilities.calculatePointRelativeToLine(ptList, 0, percent, true);
			return refPoint;
		} else if (getParent() instanceof GraphicalEditPart) {
			return ((AbstractGraphicalEditPart)getParent()).getFigure().getBounds().getTopLeft();
		}
		return null;			
	}
	
}
