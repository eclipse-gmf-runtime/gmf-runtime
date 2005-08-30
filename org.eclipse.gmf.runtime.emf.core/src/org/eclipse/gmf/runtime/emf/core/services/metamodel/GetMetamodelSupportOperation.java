/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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