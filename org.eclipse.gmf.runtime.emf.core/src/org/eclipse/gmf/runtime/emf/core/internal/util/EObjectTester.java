/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

/**
 * Property tester for {@link EObject}s. Currently supported properties are
 * <dl>
 * <dt>editingDomain</dt>
 * <dd>string-valued property denoting the unique identifier of a
 * <code>TransactionalEditingDomain</code> registered in the
 * {@link TransactionalEditingDomain.Registry#INSTANCE}.</dd>
 * </dl>
 * 
 * @author ldamus
 */
public class EObjectTester extends PropertyTester {

	private static final String EDITING_DOMAIN_PROPERTY = "editingDomain"; //$NON-NLS-1$

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		EObject eObject = (EObject) receiver;

		if (property.equals(EDITING_DOMAIN_PROPERTY)) {
			String expectedID = (String) expectedValue;

			if (expectedID != null) {
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(eObject);
				
				if (domain != null) {
					return expectedID.equals(domain.getID());
				}
			}
		}

		return false;
	}
}
