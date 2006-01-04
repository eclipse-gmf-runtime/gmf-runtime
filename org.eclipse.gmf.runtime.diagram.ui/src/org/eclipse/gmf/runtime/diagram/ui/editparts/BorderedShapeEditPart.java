/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemContainerFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This is a shape which contains border items. The shape responsible for
 * setting the client area such that border items can be placed on the sides of
 * the shape. Also it is responsible for adding the border item figure into the
 * proper list. Created On: Jul 14, 2003
 * 
 * @author tisrar
 * @author jbruck
 * @deprecated 01/04/2006 See API change documentation in bugzilla 111935
 *             (https://bugs.eclipse.org/bugs/show_bug.cgi?id=111935)
 */
public abstract class BorderedShapeEditPart
	extends ShapeNodeEditPart {

	/**
	 * Create an instance.
	 * 
	 * @param view
	 *            editpart's model
	 */
	public BorderedShapeEditPart(View view) {
		super(view);
	}

	/**
	 * Returns the editpart's main figure.
	 * 
	 * @return <code>IFigure</code>
	 */
	protected IFigure getMainFigure() {
		return getBorderedFigure().getElementPane();
	}

	/**
	 * Return the editpart's bordered figure.
	 * 
	 * @return <code>IFigure</code>
	 */
	protected final BorderedFigure getBorderedFigure() {
		return (BorderedFigure) getFigure();
	}

	public void setLayoutConstraint(EditPart child, IFigure childFigure,
			Object constraint) {
		getContentPaneFor((IGraphicalEditPart) child).setConstraint(
			childFigure, constraint);
	}

	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof BorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		} else {
			return getMainFigure();
		}
	}

	/**
	 * Adds the supplied child to the editpart's border item figure if it is an
	 * instanceof {@link BorderItemEditPart} and its figure is an instanceof
	 * {@link BorderItemFigure}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
		if (childEditPart instanceof BorderItemEditPart
			&& childFigure instanceof BorderItemFigure) {
			BorderItemFigure borderItem = (BorderItemFigure) childFigure;
			BorderItemContainerFigure borderItemContainer = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			IFigure boundaryFig = getBorderItemBoundaryFigure();
			if (borderItem.getLocator() != null) {
				borderItemContainer.addBorderItem(borderItem, borderItem
					.getLocator());
			} else {
				borderItemContainer.addBorderItem(borderItem,
					new BorderItemFigure.BorderItemLocator(borderItem,
						boundaryFig));
			}
		} else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) childEditPart);
			fig.add(childFigure, index);
		}
	}

	/**
	 * Return the figure on which the border item elements will be drawn.
	 * 
	 * @return {@link #getMainFigure()}
	 */
	protected IFigure getBorderItemBoundaryFigure() {
		return getMainFigure();
	}

	/**
	 * Remove the supplied child editpart's figure from this editpart's figure.
	 */
	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		if (child instanceof BorderItemEditPart
			&& childFigure instanceof BorderItemFigure) {
			BorderItemFigure borderItem = (BorderItemFigure) childFigure;
			BorderItemContainerFigure borderItemContainer = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) child);
			borderItemContainer.removeBorderItem(borderItem);
		} else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) child);
			fig.remove(childFigure);
		}
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTION_LABELS_ROLE,
			new ConnectionLabelsEditPolicy());

	}

	/**
	 * Returns a {@link BorderedFigure}that will <i>wrap </i> this editpart's
	 * main figure.
	 * 
	 * @see #createMainFigure()
	 */
	protected NodeFigure createNodeFigure() {
		return new BorderedFigure(createMainFigure());
	}

	/**
	 * Creates this editpart's main figure.
	 * 
	 * @return the created <code>NodeFigure</code>
	 */
	protected abstract NodeFigure createMainFigure();
}