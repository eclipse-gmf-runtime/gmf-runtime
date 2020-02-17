/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Property tester for {@link EObject}s. Currently supported properties are
 * <dl>
 * <dt>resourceURI</dt>
 * <dd>string-valued property denoting the URI of the resource in which the
 * EObjec is found.</dd>
 * </dl>
 * 
 * @author ldamus
 */
public class ResourceTester extends PropertyTester {

	private static final String RESOURCE_URI_PROPERTY = "resourceURI"; //$NON-NLS-1$

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		EObject eObject = (EObject) receiver;

		if (property.equals(RESOURCE_URI_PROPERTY)) {
			Resource resource = eObject.eResource();

			if (resource != null) {
				URI uri = resource.getURI();
				return uri.toString().equals(expectedValue);
			}
		}

		return false;
	}
}
