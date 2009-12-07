/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.core.util;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.IExtendedResourceFactory;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.tests.runtime.emf.core.BaseTests;

/**
 * Unit tests for <code>EMFCoreUtil</code>.
 * 
 * @author ldamus
 */
public class EMFCoreUtilTests extends BaseTests {

	public static String EMF_CORE_UTIL_TESTS_PROXY_ID = "EMFCoreUtilTests_proxyID";

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(EMFCoreUtilTests.class,
				"EMFCoreUtilTests Test Suite"); //$NON-NLS-1$
	}

	/**
	 * Tests that when a resource factory override is registered for a proxy in
	 * the <code>extensionToFactoryMap</code>, it will be used to provide the
	 * proxy ID.
	 */
	public void test_getProxyID_288303() {

		// Make the class element a proxy
		EObject clazz = find(ecoreRoot, "class"); //$NON-NLS-1$
		((InternalEObject) clazz).eSetProxyURI(EcoreUtil.getURI(clazz));

		// Check the default proxy ID
		String result = EMFCoreUtil.getProxyID(clazz);
		assertEquals("Unexpected Proxy ID", "//class", result);

		// Register a custom resource factory
		ResourceSet resourceSet = domain.getResourceSet();
		Map<String, Object> extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();
		Object original = extensionToFactoryMap
				.get(Resource.Factory.Registry.DEFAULT_EXTENSION);
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				new EMFCoreUtilTestsResourceFactory());

		// Check the custom proxy ID
		result = EMFCoreUtil.getProxyID(clazz);
		assertEquals("Unexpected Proxy ID", EMF_CORE_UTIL_TESTS_PROXY_ID, result);

		// Restore the original resource factory
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				original);
	}

	protected class EMFCoreUtilTestsResourceFactory extends ResourceFactoryImpl
			implements IExtendedResourceFactory {

		public String getProxyClassID(EObject proxy) {
			return null;
		}

		public String getProxyID(EObject proxy) {
			return EMF_CORE_UTIL_TESTS_PROXY_ID;
		}

		public String getProxyName(EObject proxy) {
			return null;
		}

		public String getProxyQualifiedName(EObject proxy) {
			return null;
		}

		public EObject resolve(TransactionalEditingDomain domain, EObject proxy) {
			return null;
		}
	}
}
