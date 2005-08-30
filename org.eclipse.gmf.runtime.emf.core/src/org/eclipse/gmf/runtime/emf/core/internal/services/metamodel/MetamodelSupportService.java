/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.services.metamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.GetMetamodelSupportOperation;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupportProvider;

/**
 * The meta-model service.
 * 
 * @author rafikj
 */
public class MetamodelSupportService
	extends Service
	implements IMetamodelSupportProvider {

	// the one instance of the service.
	public final static MetamodelSupportService instance = new MetamodelSupportService(
		true);

	// cache the operations.
	public final static Map operations = new HashMap();

	/**
	 * Constructor.
	 */
	public MetamodelSupportService(boolean optimized) {
		super(optimized);
	}

	/**
	 * Gets the one instance of the service.
	 */
	public static MetamodelSupportService getInstance() {
		return instance;
	}

	/**
	 * Gets a cached operation.
	 */
	public IOperation getOperation(EPackage ePackage) {

		IOperation operation = (IOperation) operations.get(ePackage);

		if (operation == null) {

			operation = new GetMetamodelSupportOperation(ePackage);
			operations.put(ePackage, operation);
		}

		return operation;
	}

	/**
	 * Configures the meta-model service.
	 */
	public static void configure(IConfigurationElement[] elements) {
		instance.configureProviders(elements);
	}

	/**
	 * Executes a service request.
	 */
	private Object execute(IOperation operation) {

		List results = execute(ExecutionStrategy.FIRST, operation);

		return results.isEmpty() ? null
			: results.get(0);
	}

	public IMetamodelSupport getMetamodelSupport(EPackage ePackage) {
		return (IMetamodelSupport) execute(getOperation(ePackage));
	}
}