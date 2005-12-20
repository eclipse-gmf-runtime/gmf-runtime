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

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
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
	 * @param model The resizable compartment view
	 */
	public ResizableCompartmentEditPart(EObject model) {
		super(model);
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
		return new ResizableCompartmentFigure(getTitleName(), getMapMode().DPtoLP(ResizableCompartmentFigure.MIN_CLIENT_DP));
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

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart#getCompartmentName()
	 */
	public String getCompartmentName() {
		return new String();
	}
	
	/**
	 * Subclasses should override to return the compartment title
	 * 
	 * @return The compartment title
	 * @deprecated use {@link ResizableCompartmentEditPart#getCompartmentName()} instead
	 * 		deprecated on Dec 20th / 2005
	 * 		will be removed on Jan 30th / 2006
	 */
	protected String getTitleName() {
		return getCompartmentName();
	}
		
	/**
	 * Handles the following properties: <BR>
	 * <UL>
	 * <LI>{@link NotationPackage.eINSTANCE.getRatio_Value()} calls {@link #refreshRatio()}
	 * <LI>{@link NotationPackage.eINSTANCE.getDrawerStyle_Collapsed()} calls {@link #refreshCollapsed()}
	 * <LI>{@link NotationPackage.eINSTANCE.getTitleStyle_ShowTitle()} calls {@link #refreshShowCompartmentTitle()}
	 * <UL>
	 * <BR>
	 * All other properties are forwarded to the parent class for processing.
	 * @param evt a property change event.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handleNotificationEvent(Notification event ) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getRatio_Value().equals(feature) 
			|| event.getOldValue()instanceof Ratio
			|| event.getNewValue() instanceof Ratio)
			refreshRatio();
		else if (NotationPackage.eINSTANCE.getDrawerStyle_Collapsed().equals(feature)){		
			setCollapsed(event.getNewBooleanValue(), true);
			this.getFigure().revalidate();
		} else if (NotationPackage.eINSTANCE.getTitleStyle_ShowTitle().equals(feature))
			setShowCompartmentTitle(event.getNewBooleanValue());
		else if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(feature)){
			Integer c = (Integer) event.getNewValue();
			setFontColor(DiagramColorRegistry.getInstance().getColor(c));
		}
		else if (NotationPackage.eINSTANCE.getFontStyle().isInstance(event.getNotifier())){
			refreshFont();
		}else
			super.handleNotificationEvent(event);
	}
		
	/**
	 * Refreshes the compartment ratio property
	 */
	protected void refreshRatio() {
		if (ViewUtil.isPropertySupported((View)getModel(), Properties.ID_RATIO))
			setRatio((Double)getStructuralFeatureValue(NotationPackage.eINSTANCE.getRatio_Value()));
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
