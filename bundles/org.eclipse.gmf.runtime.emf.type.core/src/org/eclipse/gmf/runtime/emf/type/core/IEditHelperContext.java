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

package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EObject;

/**
 * The context of an editing gesture. It represents the <code>EObject</code>
 * or <code>IElementType</code> that is the subject of the editing action, and
 * the <code>IClientContext</code> in which the editing will be performed.
 * <P>
 * If the <code>IElementType</code> is specified, the <code>EObject</code>
 * will be ignored in determining the context of the editing gesture.
 * <P>
 * This interface should not be implemented by clients. The
 * {@link EditHelperContext} should be instantiated instead.
 * 
 * @author ldamus
 */
public interface IEditHelperContext {

	/**
	 * Gets the <code>EObject</code> that is the subject of the editing
	 * action.
	 * 
	 * @return the subject of the editing action
	 */
	public abstract EObject getEObject();

	/**
	 * Gets the <code>IElementType</code> that is the subject of the editing
	 * action.
	 * 
	 * @return the element type
	 */
	public abstract IElementType getElementType();

	/**
	 * Gets the client context in which the editing gesture will be performed.
	 * 
	 * @return the client context
	 */
	public abstract IClientContext getClientContext();
}
