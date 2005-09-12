/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ResizableCompartmentEditPart;
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
