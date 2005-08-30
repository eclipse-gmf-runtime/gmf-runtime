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

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedPaneFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import com.ibm.xtools.notation.View;

/**
 * This is a shape which contains gates. The shape responsible for setting the
 * client area such that gates can be placed on the sides of the shape. Also it
 * is responsible for adding the gate figure into the proper list. Created On:
 * Jul 14, 2003
 * 
 * @author tisrar
 * @author jbruck
 */
public abstract class GatedShapeEditPart extends ShapeNodeEditPart {
	/**
	 * Create an instance.
	 * @param view editpart's model
	 */
	public GatedShapeEditPart(View view) {
		super(view);
	}	
	
	/**
	 * Returns the editpart's main figure.
	 * @return <code>IFigure</code>
	 */
	protected final IFigure getMainFigure() {
		return getGatedPaneFigure().getElementPane();
	}

	/**
	 * gets this editpart's gate figure.
	 * @return <code>IFigure</code>
	 */
	protected final IFigure getGateFigure() {
		return getGatedPaneFigure().getGatePane();
	}

	/**
	 * Return the editpart's gated pane figure.
	 * @return <code>IFigure</code>
	 */
	protected final GatedPaneFigure getGatedPaneFigure() {
		return (GatedPaneFigure)getFigure();
	}
	
	/**
	 * Sets the supplied constraint on the <tt>childFigure</tt>.
	 * @see org.eclipse.gef.GraphicalEditPart#setLayoutConstraint(EditPart,
	 *      IFigure, Object)
	 */
	public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint) {
		getContentPaneFor((IGraphicalEditPart) child).setConstraint(childFigure, constraint);
	}

	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if ( editPart instanceof GateEditPart ) {
			return getGatedPaneFigure().getGatePane();
		}
		else {
			return getMainFigure();
		}
	}

	/**
	 * Adds the supplied child to the editpart's gate figure if it is 
	 * an instanceof {@link GateEditPart} and its figure is an instanceof {@link GateFigure}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure childFigure = ((GraphicalEditPart)childEditPart).getFigure();
		if ( childEditPart instanceof GateEditPart && childFigure instanceof GateFigure  ) {
			GateFigure gateFigure = (GateFigure) childFigure; 
			GatedFigure gatedFigure = (GatedFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			IFigure boundaryFig = getGateBoundryFigure();
			if( gateFigure.getLocator() != null ) {
				gatedFigure.addGate(gateFigure, gateFigure.getLocator());
			} else {
				gatedFigure.addGate(gateFigure, new GateFigure.GateLocator(gateFigure, boundaryFig));
			}
		}
		else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) childEditPart);
			fig.add(childFigure, index);
		}
	}

	/**
	 * Return the figure on which the gate elements will be drawn.
	 * @return {@link #getMainFigure()}
	 */
	protected IFigure getGateBoundryFigure() {
		return getMainFigure();
	}
	
	/**
	 * Remove the supplied child editpart's figure from this editpart's figure.
	 */
	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart)child).getFigure();
		if ( child instanceof GateEditPart && childFigure instanceof GateFigure ) {
			GateFigure gateFigure = (GateFigure)childFigure;
			GatedFigure gatedFigure = (GatedFigure) getContentPaneFor((IGraphicalEditPart) child);
			gatedFigure.removeGate(gateFigure);
		}
		else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) child);
			fig.remove(childFigure);
		}
	}

	/**
	 * Installs the desired EditPolicies for this.
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTOR_LABELS,	new ConnectorLabelsEditPolicy());

	}
	/**
	 * Returns a {@link GatedPaneFigure}that will <i>wrap </i> this editpart's
	 * main figure.
	 * 
	 * @see #createMainFigure()
	 */
	protected final NodeFigure createNodeFigure() {
		return new GatedPaneFigure(createMainFigure());
	}

	/**
	 * Creates this editpart's main figure.
	 * @return the created <code>NodeFigure</code>
	 */
	protected abstract NodeFigure createMainFigure();
}