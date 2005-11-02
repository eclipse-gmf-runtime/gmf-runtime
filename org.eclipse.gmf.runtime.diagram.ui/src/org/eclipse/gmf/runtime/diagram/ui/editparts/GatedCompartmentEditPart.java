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
import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GatedShapeCompartmentDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemContainerFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Compartment that allows gates to be added.
 * 
 * @author jbruck
 * @deprecated Deleted. Wasn't being used anymore.
 */
public class GatedCompartmentEditPart extends ShapeCompartmentEditPart {
	/**
	 * Create an instance
	 * 
	 * @param view editpart model.
	 */
	public GatedCompartmentEditPart(View view) {
		super(view);
	}

	/** Return the compartment's displayable title. */
	protected String getTitleName() {
		return null;
	}

	/**
	 * Adds the following editpolicies: <BR>
	 * <UL>
	 * <LI> {@link EditPolicyRoles#DRAG_DROP_ROLE} :: {@link GatedShapeCompartmentDragDropEditPolicy}
	 * </UL>
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new GatedShapeCompartmentDragDropEditPolicy());
	}
	
	/**
	 * Returns a {@link BorderedFigure}that will <i>wrap </i> this editpart's
	 * main figure.
	 * 
	 * @see #createMainFigure()
	 */
	protected final IFigure createFigure() {
		BorderedFigure fig = new BorderedFigure(createMainFigure());
		return fig;
	}

	/**
	 * Creates this editpart's main figure.
	 * @return the created <code>IFigure</code>
	 */
	protected IFigure createMainFigure() {
		return super.createFigure();
	}

	/**
	 * getter for the gated pane figure.
	 * @return <code>BorderedFigure</code>
	 */
	protected final BorderedFigure getGatedPaneFigure() {
		return (BorderedFigure) getFigure();
	}

	
	/**
	 * gets this editpart's main figure.
	 * @return <code>IFigure</code>
	 */
	public IFigure getMainFigure() {
		return getGatedPaneFigure().getElementPane();
	}

	/**
	 * gets this editpart's gate pane figure.
	 * @return <code>IFigure</code>
	 */
	public IFigure getGateFigure() {
		return getGatedPaneFigure().getBorderItemContainer();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#getContentPaneFor(org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart)
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if ( editPart instanceof BorderItemEditPart ) {
			return ((BorderedFigure) figure).getBorderItemContainer();
		}
		else {
			return super.getContentPaneFor(editPart); // this will return the
													  // pane of the main
													  // figure.
		}
	}

	/**
	 * Adds the supplied child to the editpart's gate figure if it is 
	 * an instanceof {@link BorderItemEditPart} and its figure is an instanceof {@link BorderItemFigure}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {

		IFigure childFigure = ((GraphicalEditPart)childEditPart).getFigure();
		if ( childEditPart instanceof BorderItemEditPart && childFigure instanceof BorderItemFigure) {
			BorderItemFigure gateFigure = (BorderItemFigure) childFigure;
			BorderItemContainerFigure gatedFigure = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			IFigure boundaryFig = getMainFigure();
			gatedFigure.addBorderItem(gateFigure, new BorderItemFigure.BorderItemLocator(gateFigure, boundaryFig));
		}
		else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) childEditPart);
			fig.add(childFigure, index);
		}
	}

	/**
	 * Remove the supplied child editpart's figure from this editpart's figure.
	 */
	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart)child).getFigure();
		if ( child instanceof BorderItemEditPart && childFigure instanceof BorderItemFigure) {
			BorderItemFigure gateFigure = (BorderItemFigure)childFigure;
			BorderItemContainerFigure gatedFigure = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) child);
			gatedFigure.removeBorderItem(gateFigure);
		}
		else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) child);
			fig.remove(childFigure);
		}
	}

	/** Return the main figure. */
	public ResizableCompartmentFigure getCompartmentFigure() {
		return (ResizableCompartmentFigure) getMainFigure();
	}
}