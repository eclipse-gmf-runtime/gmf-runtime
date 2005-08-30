/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.notation.View;

/**
 * An editpart to show a default compartment view.
 * 
 * @author cmahoney
 * 
 */
public class DefaultCompartmentEditPart
	extends ResizableCompartmentEditPart {

	/**
	 * Constructs a new instance.
	 * 
	 * @param view
	 */
	public DefaultCompartmentEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#refresh()
	 */
	public void refresh() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ResizableCompartmentEditPart#getTitleName()
	 */
	protected String getTitleName() {
		return PresentationResourceManager.getI18NString("InvalidView"); //$NON-NLS-1$
	}
}
