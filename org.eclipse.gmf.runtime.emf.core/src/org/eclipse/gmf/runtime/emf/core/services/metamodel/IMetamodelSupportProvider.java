/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.services.metamodel;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The meta-model provider interface.
 * 
 * @author rafikj
 */
public interface IMetamodelSupportProvider
	extends IProvider {

	/**
	 * meta-model providers must implement this method.
	 */
	public IMetamodelSupport getMetamodelSupport(EPackage ePackage);
}