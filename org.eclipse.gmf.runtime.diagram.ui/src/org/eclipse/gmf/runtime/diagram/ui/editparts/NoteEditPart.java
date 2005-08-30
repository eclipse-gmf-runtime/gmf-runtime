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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.NoteFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import com.ibm.xtools.notation.View;

/**
 * Note shape which provides textual annotations for diagram elements.  Notes are 
 * attachable. 
 * <p>
 * Notes support diagram links.
 * <p>
 * @author jcorchis
 */
public class NoteEditPart extends ShapeNodeEditPart {

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public NoteEditPart(View view) {
		super(view);
	}

	/**
	 * Creates a note figure.
	 */
	protected NodeFigure createNodeFigure() {
		NoteFigure noteFigure = new NoteFigure();
		Object model = getModel();
		if (model!=null && model instanceof View){
			View notationView = (View)model;
			if ( notationView!=null && 
				 (notationView.getEAnnotation(Properties.DIAGRAMLINK_ANNOTATION)!=null ||
				  notationView.getType() == null ||
				  notationView.getType().length() == 0))
				noteFigure.setDiagramLinkMode(true);
		}
		return noteFigure;
	}

	/** Adds support for diagram links. */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// Add Note support for diagram links
		// The following two edit policies support the links.		
		installEditPolicy(
			EditPolicyRoles.DRAG_DROP_ROLE,
			new DiagramLinkDragDropEditPolicy());
		
		installEditPolicy(
			EditPolicyRoles.OPEN_ROLE,
			new OpenDiagramEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}
}
