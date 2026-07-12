/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for <code>EMFCoreUtil</code>.
 *
 * @author ldamus
 */
public class EMFCoreUtilTests extends BaseTests {

	public static String EMF_CORE_UTIL_TESTS_PROXY_ID = "EMFCoreUtilTests_proxyID";

	/**
	 * Tests that when a resource factory override is registered for a proxy in the
	 * <code>extensionToFactoryMap</code>, it will be used to provide the proxy ID.
	 */
	@Test
	public void test_getProxyID_288303() {

		// Make the class element a proxy
		EObject clazz = find(ecoreRoot, "class"); //$NON-NLS-1$
		((InternalEObject) clazz).eSetProxyURI(EcoreUtil.getURI(clazz));

		// Check the default proxy ID
		String result = EMFCoreUtil.getProxyID(clazz);
		assertEquals(result, "//class", "Unexpected Proxy ID");

		// Register a custom resource factory
		ResourceSet resourceSet = domain.getResourceSet();
		Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		Object original = extensionToFactoryMap.get(Resource.Factory.Registry.DEFAULT_EXTENSION);
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EMFCoreUtilTestsResourceFactory());

		// Check the custom proxy ID
		result = EMFCoreUtil.getProxyID(clazz);
		assertEquals(EMF_CORE_UTIL_TESTS_PROXY_ID, result, "Unexpected Proxy ID");

		// Restore the original resource factory
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, original);
	}

	protected class EMFCoreUtilTestsResourceFactory extends ResourceFactoryImpl implements IExtendedResourceFactory {

		@Override
		public String getProxyClassID(EObject proxy) {
			return null;
		}

		@Override
		public String getProxyID(EObject proxy) {
			return EMF_CORE_UTIL_TESTS_PROXY_ID;
		}

		@Override
		public String getProxyName(EObject proxy) {
			return null;
		}

		@Override
		public String getProxyQualifiedName(EObject proxy) {
			return null;
		}

		@Override
		public EObject resolve(TransactionalEditingDomain domain, EObject proxy) {
			return null;
		}
	}
}
