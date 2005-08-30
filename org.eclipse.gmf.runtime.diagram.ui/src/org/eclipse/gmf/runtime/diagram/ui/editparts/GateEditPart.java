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

import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GateNonResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedPaneFigure;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The class which controls the behavior of a gate. It determines the
 * connections coming in and out. Created On: Jul 8, 2003
 * 
 * @author tisrar
 * @author jbruck
 */
public class GateEditPart extends ShapeNodeEditPart {  // inherit from GatedShapeEditPart eventually

	/** 
	 * Create an instance.
	 * @param view the editpart's model.
	 */
	public GateEditPart(View view) {
		super(view);
	}
	

	/**
	 * Refresh the bounds using a <tt>locator</tt> if this editpart's figure
	 * is a {@link GateFigure}instance; otherwise, the <tt>super</tt>
	 * implementation is used. Locators are used since a <tt>gate element</tt>
	 * 's position and extent properties are not persisted.
	 */
	protected void refreshBounds() {
		if ( getFigure() instanceof GateFigure ) {
		 	GateFigure.GateLocator locator = (GateFigure.GateLocator) getLocator();
			if (locator != null) {
				int x = ((Integer) getPropertyValue(Properties.ID_POSITIONX)).intValue();
		    	int y = ((Integer) getPropertyValue(Properties.ID_POSITIONY)).intValue();
		    	Point loc = new Point(x, y);
				locator.resetPosition(new Rectangle(loc, getFigure().getPreferredSize()));
			} else {
				super.refreshBounds();
			}
		}
		else {
			super.refreshBounds();
		}
	}

	
	/** Create a gate figure. */
	protected NodeFigure createNodeFigure() {
		return new GateFigure(DrawConstant.EAST);
	}
	
	
	/**
	 * Creates this editpart's main figure.
	 * @return the created <code>NodeFigure</code>
	 */
	protected NodeFigure createMainFigure()	{
		return new GateFigure(DrawConstant.EAST);
	}

	/**
	 * sets the locator for the figure. This locator locates the figure when
	 * ever the validation is called on the shape.
	 * 
	 * @param locator The locator which will be called when the parent figure is
	 *        validated.
	 */
	
	public void setLocator(Locator locator) {
		((GateFigure) getFigure()).setLocator(locator);
	}
	

	/**
	 * Convenience method to return this gate figure's locator.
	 * @return the <code>Locator</code>
	 */
	public Locator getLocator() {
		return ((GateFigure) getFigure()).getLocator();
	}
	

	/**
	 * Return the editpolicy to be installed as an <code>EditPolicy#PRIMARY_DRAG_ROLE</code>
	 * role.  This method is typically called by <code>LayoutEditPolicy#createChildEditPolicy()</code>
	 * @return <code>EditPolicy</code>
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		return new GateNonResizableEditPolicy();
	}
	

	/**
	 * get this edit part's main figure
	 * @return the <code>IFigure</code>
	 */
	public final IFigure getMainFigure() {
		if(getFigure() instanceof GatedPaneFigure )	{
			return ((GatedPaneFigure)getFigure()).getElementPane();
		}
		return getFigure();
	}
	
		
	/**
	 * gets this editpart's gated pane figure.
	 * @return the <codE>GatedPaneFigure</code>
	 */
	protected final GatedPaneFigure getGatedPaneFigure() {
		return (GatedPaneFigure)getFigure();
	}
	
		
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof GateEditPart) {
			return getGatedPaneFigure().getGatePane();
		} else {
			return getMainFigure();
		}
	}
	
	
	
	/**
	 * Adds the supplied child to the editpart's gate figure if it is 
	 * an instanceof {@link GateEditPart} and its figure is an instanceof {@link GateFigure}.
	 */
	
	protected void addChildVisual(EditPart childEditPart, int index) {
		
		IFigure childFigure = ((GraphicalEditPart)childEditPart).getFigure();
		if ( childEditPart instanceof GateEditPart && childFigure instanceof GateFigure ) {
			GateFigure gateFigure = (GateFigure) childFigure; 
			GatedFigure gatedFigure = (GatedFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			if (gateFigure.getLocator() != null) {
				gatedFigure.addGate(gateFigure, gateFigure.getLocator());
			} else {
				gatedFigure.addGate(gateFigure, new GateFigure.GateLocator(gateFigure, getMainFigure()));	
			}
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

	/** Include the gates's parent's parent to the list. */
	Collection disableCanonicalFor( final Request request ) {
		Collection disabled = super.disableCanonicalFor(request);
		if ((request instanceof CreateConnectorViewRequest) ) {
			CreateConnectorViewRequest ccvr = (CreateConnectorViewRequest)request;
			if ( ccvr.getSourceEditPart() instanceof GateEditPart ) {
				disabled.add( ccvr.getSourceEditPart().getParent().getParent() );
			}
			if ( ccvr.getTargetEditPart() instanceof GateEditPart ) {
				disabled.add( ccvr.getTargetEditPart().getParent().getParent() );
			}
		}
		return disabled;
	}
	
	/**
	 * Sets the supplied constraint on the <tt>childFigure</tt>.
	 * @see org.eclipse.gef.GraphicalEditPart#setLayoutConstraint(EditPart,
	 *      IFigure, Object)
	 */
	public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint) {
		getContentPaneFor((IGraphicalEditPart) child).setConstraint(childFigure, constraint);
	}
	
	/**
	 * Installs the desired EditPolicies for this.
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTOR_LABELS,	new ConnectorLabelsEditPolicy()); // enable the +/- for floating labels.

	}
	
	 /**
	  * this method will return the primary child EditPart  inside this edit part
	  * @return the primary child view inside this edit part
	  */
	 public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.NAME);
	 }
	
	/** Return a {@link DragTracker} instance. */
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTrackerEx(this) {
			protected boolean isMove() {
				return true;
			}
		};
	}
	
	
}