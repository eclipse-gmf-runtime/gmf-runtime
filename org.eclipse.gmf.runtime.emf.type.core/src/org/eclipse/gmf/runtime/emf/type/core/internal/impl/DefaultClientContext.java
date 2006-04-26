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

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.ClientContext;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;

/**
 * The default client context which includes the types and advice specifically
 * bound to it, as well as any types and advice to which no other context has
 * been bound.
 * 
 * @author ldamus
 */
public final class DefaultClientContext extends ClientContext {

	/**
	 * Identifier for the default client context.
	 */
	public static final String ID = "org.eclipse.gmf.runtime.emf.type.core.defaultContext"; //$NON-NLS-1$

	/**
	 * The singleton instance.
	 */
	private static IClientContext _instance;

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the default client context
	 */
	public static final IClientContext getInstance() {
		if (_instance == null) {
			_instance = new DefaultClientContext();
		}
		return _instance;
	}

	/**
	 * Private constructor for the singleton.
	 */
	private DefaultClientContext() {
		super(ID, new IElementMatcher() {
			public boolean matches(EObject eObject) {
				return true;
			}
		});
	}

	/**
	 * Includes the <code>elementTypeDescriptor</code> if it is explicitly
	 * bound to me, or there is no other context bound to it.
	 */
	public boolean includes(IElementTypeDescriptor elementTypeDescriptor) {
		boolean result = super.includes(elementTypeDescriptor);

		if (!result) {
			result = ClientContextManager.getInstance().getBinding(
					elementTypeDescriptor) == _instance;
		}
		return result;
	}

	/**
	 * Includes the <code>advice</code> if it is explicitly bound to me, or
	 * there is no other context bound to it.
	 */
	public boolean includes(IEditHelperAdviceDescriptor advice) {
		boolean result = super.includes(advice);

		if (!result) {
			result = ClientContextManager.getInstance().getBinding(advice) == _instance;
		}
		return result;
	}

	/**
	 * Includes the <code>elementTyper</code> if it is explicitly bound to me,
	 * or there is no other context bound to it.
	 */
	public boolean includes(IElementType elementType) {
		boolean result = super.includes(elementType);

		if (!result) {
			result = ClientContextManager.getInstance().getBinding(elementType) == _instance;
		}
		return result;
	}

}
