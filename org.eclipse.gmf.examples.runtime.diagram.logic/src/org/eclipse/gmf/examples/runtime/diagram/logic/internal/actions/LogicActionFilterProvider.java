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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.AbstractActionFilterProvider;

/**
 * Action filter provider for the logic example context menu action enablement.
 * Required temporarily until Bugzilla 111778 is fixed.
 * 
 * @author ldamus
 */
public class LogicActionFilterProvider extends AbstractActionFilterProvider {

	private final static String IS_CONTAINER_ELEMENT = "org.eclipse.gmf.examples.runtime.diagram.logic.isContainerElement"; //$NON-NLS-1$

	public boolean testAttribute(Object target, String name, String value) {

		if (name.equals(IS_CONTAINER_ELEMENT)) {
			if (target instanceof ContainerElement) {
				return true;
			}

			if (target instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) target;
				return (adaptable.getAdapter(ContainerElement.class) != null);
			}
		}
		return false;
	}

	public boolean provides(IOperation operation) {
		// Not used
		return true;
	}
}
