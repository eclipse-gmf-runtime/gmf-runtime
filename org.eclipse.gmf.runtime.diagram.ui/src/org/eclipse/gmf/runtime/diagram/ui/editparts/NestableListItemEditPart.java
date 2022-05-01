/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.NestedResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Ratio;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Extends ListItemEditPart to support nesting of list compartments. This edit
 * part supports a TextCompartmentView that contains children. Should be used if
 * you have a TextCompartment that contains other children such as a list
 * compartment.
 * 
 * @author choang
 * 
 */
public class NestableListItemEditPart
	extends ListItemEditPart
	implements IResizableCompartmentEditPart {

	final static int IS_RESIZABLE_FLAG = MAX_FLAG << 5;

	/**
	 * @param view
	 */
	public NestableListItemEditPart(EObject view) {
		super(view);

		setFlag(IS_RESIZABLE_FLAG, false);
		Object model = basicGetModel();
		if (model instanceof Node) {
			Node node = (Node) model;
			Style style = node.getStyle(NotationPackage.Literals
				.DRAWER_STYLE);
			if (style != null)
				setFlag(IS_RESIZABLE_FLAG, true);
		}
	}

	WrapLabel textLabel = null;

	/**
	 * @return Returns the textLabel.
	 */
	public WrapLabel getLabel() {

		if (isResizable()) {
			if (textLabel == null) {
				textLabel = createWrapLabel();
			}
			return textLabel;
		} else {
			return super.getLabel();
		}
	}
	
	/**
     * The main label figure for this editpart (that is, the only label figure
     * if this is a regular list item or the header label of the nested list
     * items if this is a nestable list item. Clients may override to create
     * other label types.
     * 
     * @return the main label figure
     */
    protected IFigure getMainLabel() {
        return getLabel();
    }

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart#getCompartmentName()
	 */
	public String getCompartmentName() {
		return getLabelDelegate().getText();
	}
	
	/**
	 * Override to create a figure that will create a figure that contains a
	 * text compartment and a pane to store the child figures of the text
	 * compartment.
	 */
	protected IFigure createFigure() {
		if (isResizable()) {
            IMapMode mm = getMapMode();
			ResizableCompartmentFigure compartmentFigure = new NestedResizableCompartmentFigure(mm);
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setStretchMajorAxis(false);
			layout.setStretchMinorAxis(false);
			layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT);
			compartmentFigure.getContentPane().setLayoutManager(layout);

			compartmentFigure.getTextPane().add(getMainLabel());

			// if the compartment is resizeable then we need to put a border
			// around the text compartment so that we have enough room for the
			// collpase handle.
			
			int one = mm.DPtoLP(1);
            int half_15 = mm.DPtoLP(15) / 2;

			// indent to make room for collapse handle for each nested list item
			compartmentFigure.getTextPane().setBorder(
				new MarginBorder(one, half_15, one, half_15));
			// indent for visual appearance
			compartmentFigure.getContentPane().setBorder(
				new MarginBorder(one, mm.DPtoLP(15), one, half_15));

			getMainLabel().setVisible(true);
			return compartmentFigure;
		} else {
			return super.createFigure();
		}

	}
    
	/**
	 * Adds additional edit policy EditPolicy.PRIMARY_DRAG_ROLE to provide
	 * collapsiblity for list compartments
	 */
	protected void createDefaultEditPolicies() {

		super.createDefaultEditPolicies();
		if (isResizable()) {
			installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new ResizableCompartmentEditPolicy());
		}
	}

	/**
	 * This edit part can support either being ListItemListCompartmentEditPart
	 * or ListItemListCompartmentEditPart that is nested with children and hence
	 * is resizable. This helper method will help us determine which behavior we
	 * want.
	 * 
	 * @return true iff the TextCompartment is mean to support children.
	 */
	final protected boolean isResizable() {
		return getFlag(IS_RESIZABLE_FLAG);
	}

	/*
	 * @return getView()).getChildren() this editpart supports having children.
	 */
	protected List getModelChildren() {

		if (getModel() instanceof View) {
			return ((View) getModel()).getChildren();
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * This method helps in children navigation by scrolling the compartment
	 * until the child is visible in the viewport
	 */
	public Object getAdapter(Class key) {

		if (key == ExposeHelper.class) {
			ViewportExposeHelper helper = new ViewportExposeHelper(this);
			return helper;
		}
		return super.getAdapter(key);
	}

	protected void refreshVisuals() {
		super.refreshVisuals();

		if (isResizable()) {
			refreshCollapsed();
			refreshRatio();
		}
	}

	/**
	 * @return The compartment's figure if isResizable() else return null;
	 */
	private ResizableCompartmentFigure getCompartmentFigure() {
		if (isResizable()) {
			return (ResizableCompartmentFigure) getFigure();
		} else {
			return null;
		}
	}

	public IFigure getContentPane() {
		if (getCompartmentFigure() != null) {

			return getCompartmentFigure().getContentPane();
		} else {
			return super.getContentPane();
		}
	}

	/**
	 * Handles the following properties: <BR>
	 * <UL>
	 * <LI>{@link Properties#ID_RATIO} calls {@link #refreshRatio()}
	 * <LI>{@link Properties#ID_COLLAPSED} calls {@link #refreshCollapsed()}
	 * <UL>
	 * <BR>
	 * All other properties are forwarded to the parent class for processing.
	 * 
	 * @param evt
	 *            a property change event.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handleNotificationEvent(Notification evt) {
		Object feature = evt.getFeature();
		if (NotationPackage.Literals.RATIO__VALUE.equals(feature)
			|| evt.getOldValue() instanceof Ratio
			|| evt.getNewValue() instanceof Ratio)
			refreshRatio();
		else if (NotationPackage.Literals.DRAWER_STYLE__COLLAPSED.equals(
			feature)) {
			setCollapsed(((Boolean) evt.getNewValue()).booleanValue(), true);
			this.getFigure().revalidate();
		} else
			super.handleNotificationEvent(evt);
	}

	/**
	 * Refreshes the compartment ratio property
	 */
	protected void refreshRatio() {
		if (ViewUtil
			.isPropertySupported((View) getModel(), Properties.ID_RATIO))
			setRatio((Double) ViewUtil.getStructuralFeatureValue(
				(View) getModel(), NotationPackage.Literals.RATIO__VALUE));
		else
			setRatio(Double.valueOf(-1));
	}

	/**
	 * Refreshes the compartment collapse state
	 */
	protected void refreshCollapsed() {
		DrawerStyle style = (DrawerStyle) ((View) getModel())
			.getStyle(NotationPackage.Literals.DRAWER_STYLE);
		if (style != null)
			setCollapsed(style.isCollapsed(), false);
	}

	/**
	 * Sets the collapse state of the compartment figure, considering the passed
	 * animate flag while doing so
	 * 
	 * @param collapsed
	 *            the collapsed state
	 * @param animate
	 *            the animate flag
	 */
	protected void setCollapsed(boolean collapsed, boolean animate) {
		if (getCompartmentFigure() != null) {
			if (collapsed) {
				if (animate)
					getCompartmentFigure().collapse();
				else
					getCompartmentFigure().setCollapsed();
			} else {
				if (animate)
					getCompartmentFigure().expand();
				else
					getCompartmentFigure().setExpanded();
			}
		}
	}

	/**
	 * Sets the ratio of the resizable compartment
	 * 
	 * @param ratio
	 */
	protected void setRatio(Double ratio) {
		((IGraphicalEditPart) getParent()).setLayoutConstraint(this,
			getFigure(), ratio);
	}

	/**
	 * Sets the visibility of the compartment title
	 * 
	 * @param showCompartmentTitle
	 */
	protected void setShowCompartmentTitle(boolean showCompartmentTitle) {
		if (getCompartmentFigure() != null)
			getCompartmentFigure().setTitleVisibility(showCompartmentTitle);
	}

}
