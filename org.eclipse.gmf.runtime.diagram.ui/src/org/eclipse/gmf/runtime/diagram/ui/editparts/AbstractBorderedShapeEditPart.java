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

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This is a shape which may contain border items. It has accessors for the main
 * figure and the bordered figure. It delegates calls to the main figure or
 * bordered figure as appropriate. It handles the adding of border items with a
 * default locator; to change the locator, override the method
 * {@link #addBorderItem(IFigure, IBorderItemEditPart)}.
 * 
 * @author tisrar, jbruck, cmahoney
 */
public abstract class AbstractBorderedShapeEditPart
	extends ShapeNodeEditPart
	implements IBorderedShapeEditPart {

	/**
	 * Create an instance.
	 * 
	 * @param view
	 *            editpart's model
	 */
	public AbstractBorderedShapeEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderedShapeEditPart#getElementFigure()
	 */
	public IFigure getMainFigure() {
		return getBorderedFigure().getMainFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderedShapeEditPart#getBorderedFigure()
	 */
	public final BorderedNodeFigure getBorderedFigure() {
		return (BorderedNodeFigure) getFigure();
	}

	public void setLayoutConstraint(EditPart child, IFigure childFigure,
			Object constraint) {
		getContentPaneFor((IGraphicalEditPart) child).setConstraint(
			childFigure, constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#getContentPaneFor(org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart)
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof IBorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		} else {
			return getMainFigure();
		}
	}

	/**
	 * Adds the supplied child to the editpart's border item container figure if
	 * it is an instanceof {@link IBorderItemEditPart}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
		if (childEditPart instanceof IBorderItemEditPart) {
			IFigure borderItemContainer = getContentPaneFor((IGraphicalEditPart) childEditPart);
			addBorderItem(borderItemContainer,
				(IBorderItemEditPart) childEditPart);
		} else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) childEditPart);
			fig.add(childFigure, index);
		}
	}

	/**
	 * Remove the supplied child editpart's figure from this editpart's figure.
	 */
	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		IFigure fig = getContentPaneFor((IGraphicalEditPart) child);
		fig.remove(childFigure);
	}

	/**
	 * Adds the border item figure to the border item container with a locator.
	 * 
	 * @param borderItemContainer
	 *            the figure to which the border item figure is added
	 * @param borderItemEditPart
	 *            the border item editpart from which to retrieve the border
	 *            item figure and determine which locator to create
	 */
	protected void addBorderItem(IFigure borderItemContainer,
			IBorderItemEditPart borderItemEditPart) {
		borderItemContainer.add(borderItemEditPart.getFigure(),
			new BorderItemLocator(getMainFigure()));
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTION_LABELS_ROLE,
			new ConnectionLabelsEditPolicy());
	}

	/**
	 * Returns a {@link BorderedNodeFigure}that will <i>wrap</i> this
	 * editpart's main figure.
	 * 
	 * @see #createMainFigure()
	 */
	protected NodeFigure createNodeFigure() {
		return new BorderedNodeFigure(createMainFigure());
	}

	/**
	 * Creates this editpart's main figure.
	 * 
	 * @return the created <code>NodeFigure</code>
	 */
	protected abstract NodeFigure createMainFigure();
}
