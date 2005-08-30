/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IInsertableEditPart;
import org.eclipse.gmf.runtime.notation.View;



/**
 * EditPolicy to be installed on {@link org.eclipse.gmf.runtime.diagram.ui.editparts.ListItemCompartmentEditPart}.
 * This editpolicy will delete the selected element.
 * @author mhanner
 */
public class ListItemComponentEditPolicy
	extends ComponentEditPolicy {


	/**
	 * 
	 */
	public ListItemComponentEditPolicy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the view element to be deleted.
	 * @return the host's view element.
	 */
	protected View getView() {
		return (View) getHost().getModel();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy#getInsertableEditPart()
	 */
	protected IInsertableEditPart getInsertableEditPart() {
		// get the container of the host list item
		EditPart container = getHost().getParent();
		if (container instanceof IInsertableEditPart) {
			return (IInsertableEditPart)container;
		}
		
		return null;
	}
}
