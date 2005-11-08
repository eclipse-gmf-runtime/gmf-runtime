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

package org.eclipse.gmf.runtime.diagram.ui.services.editpart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Concrete operation to create a <code>RootEditPart</code> (or
 * subclass) element.
 * 
 * @author cmahoney
 */
public class CreateRootEditPartOperation extends EditPartOperation {

	/**
	 * Constructor
	 * @param diagram <code>Diagram</code> notation object that is the context for the operation.
	 */
	public CreateRootEditPartOperation(Diagram diagram) {
		super(diagram);
	}

	/**
	 * Creates the editpart.
	 * 
	 * @param provider
	 *            the provider capable of honoring this operation.
	 * @return the created editpart instance.
	 */
	public Object execute(IProvider provider) {
		return ((IEditPartProvider) provider).createRootEditPart((Diagram)getView());
	}
}