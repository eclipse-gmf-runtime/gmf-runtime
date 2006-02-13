/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * @author ldamus
 */
public class NullElementTypeAdvice
	extends AbstractEditHelperAdvice {

	protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
		EObject manager = (EObject) request.getParameter("MANAGER"); //$NON-NLS-1$
        
		SetRequest setRequest = new SetRequest(request.getEditingDomain(), request.getContainer(),
			EmployeePackage.eINSTANCE.getDepartment_Manager(), manager);
		return new SetValueCommand(setRequest);
	}

	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		IElementType nullSpecialization = ElementTypeRegistry
			.getInstance()
			.getType(
				"org.eclipse.gmf.tests.runtime.emf.type.core.nullSpecialization"); //$NON-NLS-1$

		GetEditContextCommand result = new GetEditContextCommand(request);
		result.setEditContext(nullSpecialization);
		return result;
	}
}
