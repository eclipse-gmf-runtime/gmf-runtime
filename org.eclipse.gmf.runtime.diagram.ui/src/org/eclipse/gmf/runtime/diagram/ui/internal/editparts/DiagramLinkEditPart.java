/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import com.ibm.xtools.notation.View;

/**
 * Edit Part which supports only diagram links.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DiagramLinkEditPart extends ShapeNodeEditPart {


	/**
	 * @param view
	 */
	public DiagramLinkEditPart(View view) {
		super(view);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// The following edit policy support the link open.		
		installEditPolicy(
			EditPolicyRoles.OPEN_ROLE,
			new OpenDiagramEditPolicy());	
		
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		DefaultSizeNodeFigure fig = new DefaultSizeNodeFigure();
		fig.setBorder(new MarginBorder(132, 132, 132, 132));
		fig.setLayoutManager(new ConstrainedToolbarLayout());
		fig.setOpaque(true);
		fig.setDefaultSize(new Dimension(2645, 660));
		return fig;
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}
}
