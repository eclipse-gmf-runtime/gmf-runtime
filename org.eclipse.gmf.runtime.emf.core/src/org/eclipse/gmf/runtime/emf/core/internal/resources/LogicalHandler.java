/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Map;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;

import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;

/**
 * The SAX handler for logical resources.  Ensures that XML namespace EPackages
 * are loaded in the same resource set as the logical resource, rather than in
 * the logical resource's internal resource set (for the physical resources).
 * 
 * @author Christian W. Damus (cdamus)
 */
public class LogicalHandler
	extends SAXXMIHandler {
	
	boolean isAutoLoading;
	
	/**
	 * Initializes me.
	 * 
	 * @param resource the logical resource that I am handling
	 * @param helper the <code>resource</code>'s helper
	 * @param options the load options
	 */
	public LogicalHandler(LogicalResourceUnit resource, XMLHelper helper, Map options) {
		
		super(resource, helper, options);
		
		// use the logical resource's resource set for finding and
		//    loading EPackages (for XML namespaces)
		resourceSet = resource.getLogicalResource().getResourceSet();
		
		isAutoLoading =
			Boolean.FALSE.equals(
				options.get(ILogicalResource.OPTION_LOAD_ALL_UNITS))
			&&
			Boolean.TRUE.equals(
				options.get(ILogicalResource.OPTION_AUTO_LOAD_UNITS));
	}

	protected EFactory getFactoryForPrefix(String prefix) {
		EFactory factory = (EFactory) prefixesToFactories.get(prefix);
		
		if (factory == null) {
			factory = super.getFactoryForPrefix(prefix);
			
			if (isAutoLoading && (factory != null)) {
				// substitute with our alternative
				EFactory alt = LogicalResourcePolicyManager.getInstance().getEFactory(
					factory.getEPackage());
				if ((alt != null) && (alt != factory)) {
					prefixesToFactories.put(prefix, alt);
					factory = alt;
				}
			}
		}

		return factory;
	}
	
	protected EObject createObjectFromFactory(EFactory factory, String typeName) {
		if (isAutoLoading && (factory != null)) {
			EFactory alt = LogicalResourcePolicyManager.getInstance().getEFactory(
				factory.getEPackage());
			
			if ((alt != null) && (alt != factory)) {
				factory = alt;
			}
		}
		
		return super.createObjectFromFactory(factory, typeName);
	}
}