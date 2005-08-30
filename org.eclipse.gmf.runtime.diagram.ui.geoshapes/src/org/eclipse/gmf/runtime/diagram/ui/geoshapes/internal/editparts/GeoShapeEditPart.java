/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers.GeoshapeConstants;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import com.ibm.xtools.notation.View;

/**
 * This is a base class for all Geometric Shapes
 * 
 * @author jschofie
 */
public abstract class GeoShapeEditPart extends ShapeNodeEditPart {

	/**
	 * @param view
	 */
	public GeoShapeEditPart(View view) {
		super(view);
	}

	/**
	 * Implement to return the NodeFigre.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected abstract NodeFigure createNodeFigure();

	/**
	 * Adds support for diagram links.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		
		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// Add diagram link support to all Geo shapes
		// The following two edit policies support the links.
		installEditPolicy(
			EditPolicyRoles.DRAG_DROP_ROLE,
			new DiagramLinkDragDropEditPolicy());
		
		installEditPolicy(
			EditPolicyRoles.OPEN_ROLE,
			new OpenDiagramEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {

		if (accessibleEP == null)
			accessibleEP = new AccessibleGraphicalEditPart() {

				public void getName(AccessibleEvent e) {
					View view = (View)getModel();
					e.result =
						GeoshapeConstants.getShapeLocalizedType(view.getType());
				}
			};
			
		return accessibleEP;
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}
}
