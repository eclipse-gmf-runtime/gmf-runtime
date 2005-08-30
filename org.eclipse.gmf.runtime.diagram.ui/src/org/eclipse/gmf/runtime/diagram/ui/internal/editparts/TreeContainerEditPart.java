/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import com.ibm.xtools.notation.Diagram;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TreeContainerEditPart extends TreeEditPart {

	/**
	 * @param model
	 */
	public TreeContainerEditPart(Object model) {
		super(model);
	}

	/**
	 * Returns the children of this from the model,
	 * as this is capable enough of holding EditParts.
	 *
	 * @return  List of children.
	 */
	protected List getModelChildren() {
		if (getModel() instanceof Diagram)
			return ((Diagram) getModel()).getChildren();
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TreeEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Properties.ID_PERSISTED_CHILDREN)||
			event.getPropertyName().equals(Properties.ID_TRANSIENT_CHILDREN))
			refreshChildren();
		else
			super.handlePropertyChangeEvent(event);
	}

}
