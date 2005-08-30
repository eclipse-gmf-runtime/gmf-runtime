/*
 * +------------------------------------------------------------------------+ |
 * Licensed Materials - Property of IBM | | (C) Copyright IBM Corp. 2002, 2003.
 * All Rights Reserved. | | | | US Government Users Restricted Rights - Use,
 * duplication or disclosure | | restricted by GSA ADP Schedule Contract with
 * IBM Corp. |
 * +------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GatedShapeCompartmentDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedPaneFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Compartment that allows gates to be added.
 * 
 * @author jbruck
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
	 * Returns a {@link GatedPaneFigure}that will <i>wrap </i> this editpart's
	 * main figure.
	 * 
	 * @see #createMainFigure()
	 */
	protected final IFigure createFigure() {
		GatedPaneFigure fig = new GatedPaneFigure(createMainFigure());
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
	 * @return <code>GatedPaneFigure</code>
	 */
	protected final GatedPaneFigure getGatedPaneFigure() {
		return (GatedPaneFigure) getFigure();
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
		return getGatedPaneFigure().getGatePane();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#getContentPaneFor(org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart)
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if ( editPart instanceof GateEditPart ) {
			return ((GatedPaneFigure) figure).getGatePane();
		}
		else {
			return super.getContentPaneFor(editPart); // this will return the
													  // pane of the main
													  // figure.
		}
	}

	/**
	 * Adds the supplied child to the editpart's gate figure if it is 
	 * an instanceof {@link GateEditPart} and its figure is an instanceof {@link GateFigure}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {

		IFigure childFigure = ((GraphicalEditPart)childEditPart).getFigure();
		if ( childEditPart instanceof GateEditPart && childFigure instanceof GateFigure) {
			GateFigure gateFigure = (GateFigure) childFigure;
			GatedFigure gatedFigure = (GatedFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			IFigure boundaryFig = getMainFigure();
			gatedFigure.addGate(gateFigure, new GateFigure.GateLocator(gateFigure, boundaryFig));
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
		if ( child instanceof GateEditPart && childFigure instanceof GateFigure) {
			GateFigure gateFigure = (GateFigure)childFigure;
			GatedFigure gatedFigure = (GatedFigure) getContentPaneFor((IGraphicalEditPart) child);
			gatedFigure.removeGate(gateFigure);
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