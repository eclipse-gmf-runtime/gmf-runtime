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

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Ratio;
import org.eclipse.gmf.runtime.notation.TitleStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

/**
 * An editpart for controlling generic resizable compartment views
 * The compartment can be collapsed, hidden, resized, or given a title
 * 
 * @author melaasar
 */
public abstract class ResizableCompartmentEditPart
	extends CompartmentEditPart implements IResizableCompartmentEditPart {

	/**
	 * Constructs a new resizable editpart
	 * 
	 * @param view The resizable compartment view
	 */
	public ResizableCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * This method helps in children navigation by scrolling the compartment
	 * until the child is visible in the viewport
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == ExposeHelper.class) {
			ViewportExposeHelper helper = new ViewportExposeHelper(this);
			return helper;
		}
		return super.getAdapter(key);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshFont();
		refreshFontColor();
		refreshShowCompartmentTitle();
		refreshCollapsed();
		refreshRatio();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		return new ResizableCompartmentFigure(getTitleName());
	}

	/**
	 * @return The compartment's figure
	 */
	public ResizableCompartmentFigure getCompartmentFigure() {
		return (ResizableCompartmentFigure) getFigure();
	}

	/**
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		if (getCompartmentFigure()!=null){
					
			return getCompartmentFigure().getContentPane();
		} else {
			return null;
		}
	}

	/**
	 * Subclasses should override to return the compartment title
	 * 
	 * @return The compartment title
	 */
	protected abstract String getTitleName();

	/**
	 * Handles the following properties: <BR>
	 * <UL>
	 * <LI>{@link Properties#ID_RATIO} calls {@link #refreshRatio()}
	 * <LI>{@link Properties#ID_COLLAPSED} calls {@link #refreshCollapsed()}
	 * <LI>{@link Properties#ID_SHOWCOMPARTMENTTITLE} calls {@link #refreshShowCompartmentTitle()}
	 * <UL>
	 * <BR>
	 * All other properties are forwarded to the parent class for processing.
	 * @param evt a property change event.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Properties.ID_RATIO) 
			|| evt.getOldValue() instanceof Ratio
			|| evt.getNewValue() instanceof Ratio)
			refreshRatio();
		else if (evt.getPropertyName().equals(Properties.ID_COLLAPSED)){		
			setCollapsed(((Boolean) evt.getNewValue()).booleanValue(), true);
			this.getFigure().revalidate();
		} else if (
			evt.getPropertyName().equals(Properties.ID_SHOWCOMPARTMENTTITLE))
			setShowCompartmentTitle(
				((Boolean) evt.getNewValue()).booleanValue());
		else if (evt.getPropertyName().equals(Properties.ID_FONTCOLOR)){
			Integer c = (Integer) evt.getNewValue();
			setFontColor(PresentationResourceManager.getInstance().getColor(c));
		}
		else
			super.handlePropertyChangeEvent(evt);
	}

	protected void handleNotificationEvent( NotificationEvent event ) {
		if (NotationPackage.eINSTANCE.getFontStyle().isInstance(event.getElement()))
			refreshFont();
		else
			super.handleNotificationEvent(event);
	}
		
	/**
	 * Refreshes the compartment ratio property
	 */
	protected void refreshRatio() {
		if (ViewUtil.isPropertySupported((View)getModel(), Properties.ID_RATIO))
			setRatio((Double) getPropertyValue(Properties.ID_RATIO));
		else
			setRatio(new Double(-1));
	}
	
	/**
	 * Refreshes the compartment's collapsed state
	 */
	protected void refreshCollapsed() {
		DrawerStyle style = (DrawerStyle) ((View)getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		if (style != null)		
			setCollapsed(style.isCollapsed(), false);
	}

	/**
	 * Refreshes the compartment title visibility
	 */
	protected void refreshShowCompartmentTitle() {
		TitleStyle style = (TitleStyle)  ((View)getModel()).getStyle(NotationPackage.eINSTANCE.getTitleStyle());
		if (style != null)
			setShowCompartmentTitle(style.isShowTitle());
	}
	
	/**
	 * Sets the collapse state of the compartment figure, considering the passed
	 * animate flag while doing so 
	 * @param collapsed the collapsed state 
	 * @param animate the animate flag
	 */
	protected void setCollapsed(boolean collapsed, boolean animate) {
		if (getCompartmentFigure()!=null) {
			if (collapsed) {
				if (animate)
					getCompartmentFigure().collapse();
				else
					getCompartmentFigure().setCollapsed();
			}
			else {
				if (animate)
					getCompartmentFigure().expand();
				else
					getCompartmentFigure().setExpanded();
			}
		}
	}
	
	
	

	
	
	/**
	 * Sets the ratio of the resizable compartment
	 * @param ratio
	 */
	protected void setRatio(Double ratio) {
		((IGraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			ratio);
	}

	/**
	 * Sets the visibility of the compartment title
	 * @param showCompartmentTitle
	 */
	protected void setShowCompartmentTitle(boolean showCompartmentTitle) {
		if (getCompartmentFigure()!=null)
			getCompartmentFigure().setTitleVisibility(showCompartmentTitle);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setFont(org.eclipse.swt.graphics.FontData)
	 */
	protected void setFont(FontData fontData) {
		if (getCompartmentFigure()!=null)
			fontData.setHeight(fontData.getHeight()-1);
		super.setFont(fontData);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setFontColor(org.eclipse.swt.graphics.Color)
	 */
	protected void setFontColor(Color color) {
		if (getCompartmentFigure()!=null)
			getCompartmentFigure().setFontColor(color);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#addNotationalListeners()
	 */
	protected void addNotationalListeners() {
		super.addNotationalListeners();
		addListenerFilter("PrimaryView", this, getPrimaryView()); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#removeNotationalListeners()
	 */
	protected void removeNotationalListeners() {
		super.removeNotationalListeners();
		removeListenerFilter("PrimaryView"); //$NON-NLS-1$
	}	

	/* onl
	 * @see org.eclipse.gef.EditPart#isSelectable()
	 */
	public boolean isSelectable() {
		
		if ( super.isSelectable()){
			return (!(getParent() instanceof ResizableCompartmentEditPart));
			
		}
		return false;
	}
}
