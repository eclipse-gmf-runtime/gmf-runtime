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


package org.eclipse.gmf.runtime.emf.core.services.metamodel;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Operation provided by {@link IMetamodelSupportProvider}s to look up the
 * {@link IMetamodelSupport} implementation for a particular EMF
 * {@link EPackage}.
 * 
 * @see IMetamodelSupportProvider
 * @see IMetamodelSupport
 * 
 * @author Christian W. Damus (cdamus)
 */
public class GetMetamodelSupportOperation
	implements IOperation {

	private EPackage ePackage = null;

	public GetMetamodelSupportOperation(EPackage ePackage) {
		this.ePackage = ePackage;
	}

	public EPackage getEPackage() {
		return ePackage;
	}

	public int hashCode() {
		return ePackage.hashCode();
	}

	public boolean equals(Object operation) {

		if (operation == null)
			return false;
		else
			return operation instanceof GetMetamodelSupportOperation
				&& (ePackage == ((GetMetamodelSupportOperation) operation)
					.getEPackage());
	}

	public Object execute(IProvider provider) {
		return ((IMetamodelSupportProvider) provider).getMetamodelSupport(ePackage);
	}
}