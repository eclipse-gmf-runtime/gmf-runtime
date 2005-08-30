/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.LogicFlowEditPolicy;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.LogicResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author qili
 *
 * Holds the EditPart signifying a ResizableCompartmentFigure
 */
public class LogicFlowCompartmentEditPart extends ListCompartmentEditPart{
	
	/**
	 * Constructor for LogicFlowCompartmentEditPart.
	 * @param view the view <code>controlled</code> by this editpart.
	 */
	public LogicFlowCompartmentEditPart(View view) {
		super(view);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		
		ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) super.createFigure();
		FlowLayout layout = new FlowLayout();
		layout.setMajorSpacing(MapMode.DPtoLP(5));
		layout.setMinorSpacing(MapMode.DPtoLP(5));
		rcf.getContentPane().setLayoutManager(layout);
		return rcf;
	}
	
	/**
	 * In LogicCreationEditPolicy overwrite "getReparentCommand(ChangeBoundsRequest)" 
	 * function and remove the assumption on SemanticElement
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicFlowEditPolicy());	
	}
	
	/**
	 * Subclasses should override to return the compartment title
	 * 
	 * @return The compartment title
	 */
	protected String getTitleName(){
		return LogicResourceManager
			.getI18NString("LogicFlowCompartmentEditPart.Title"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart#hasModelChildrenChanged(java.beans.PropertyChangeEvent)
	 */
	protected boolean hasModelChildrenChanged(PropertyChangeEvent evt) {
		return false;
	}
}

