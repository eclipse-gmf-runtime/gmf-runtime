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

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * @author sshaw
 * @deprecated use
 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider}
 *             Will be removed on December 16th / 2005
 */
public class AbstractLayoutProvider
		extends
		org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider {

	public Command layoutEditParts(GraphicalEditPart containerEditPart,
			IAdaptable layoutHint) {
		// TODO Auto-generated method stub
		return null;
	}

	public Command layoutEditParts(List selectedObjects, IAdaptable layoutHint) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean provides(IOperation operation) {
		// TODO Auto-generated method stub
		return false;
	}

}
