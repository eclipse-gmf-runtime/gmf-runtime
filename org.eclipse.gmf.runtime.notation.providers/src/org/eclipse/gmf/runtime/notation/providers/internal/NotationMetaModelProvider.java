/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.notation.providers.internal;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.GetMetamodelSupportOperation;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupportProvider;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Implementation of meta-model provider.
 * 
 * @author rafikj
 */
public class NotationMetaModelProvider
	extends AbstractProvider
	implements IMetamodelSupportProvider {

	private IMetamodelSupport metaModel = null;

	public IMetamodelSupport getMetamodelSupport(EPackage ePackage) {

		if (metaModel == null)
			metaModel = new NotationMetaModel();

		return metaModel;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {

		if (operation instanceof GetMetamodelSupportOperation) {

			GetMetamodelSupportOperation getMetaModelOperation = (GetMetamodelSupportOperation) operation;

			EPackage ePackage = getMetaModelOperation.getEPackage();

			return (ePackage == NotationPackage.eINSTANCE);
		}

		return false;
	}
}