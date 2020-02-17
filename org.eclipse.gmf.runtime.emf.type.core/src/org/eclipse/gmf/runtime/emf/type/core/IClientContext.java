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

import java.util.Collection;
import java.util.regex.Pattern;

import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;

/**
 * Interface of an object that defines a context to which clients of the element
 * type registry may bind element types and advice. This effectively selects the
 * types and advice that are applicable to the model defined by a client.
 * <p>
 * This interface is not intended to be implemented outside of the element type
 * framework.
 * </p>
 * 
 * @author ldamus
 */
public interface IClientContext {

	/**
	 * Returns the context ID defined by the client if this is not a
	 * multi-context. It is is a mult-context, returns the multi-context ID.
	 * 
	 * @return my context ID
	 */
	String getId();

	/**
	 * Returns whether this context is a multi-context, which is a compound
	 * context representing a group of client contexts.
	 * <p>
	 * 
	 * @return <code>true</code> for a multi-context, <code>false</code>
	 *         otherwise
	 */
	public boolean isMultiClientContext();

	/**
	 * Returns a list of {@link IClientContext}s contained in this
	 * multi-context, or an empty list if this is not a multi-context.
	 * 
	 * @return an array of client context objects
	 */
	public Collection getChildren();

	/**
	 * Obtains the element matcher that determines the elements belong to me.
	 * 
	 * @return my selector
	 */
	IElementMatcher getMatcher();

	/**
	 * Binds a specific element type or advice to me.
	 * 
	 * @param typeId
	 *            the ID of a element type or advice that is to be bound to me
	 */
	public void bindId(String typeId);

	/**
	 * Binds a pattern of element types and advice to me.
	 * 
	 * @param pattern
	 *            the pattern of element type and advice IDs that are bound to
	 *            me
	 */
	public void bindPattern(Pattern pattern);

	/**
	 * Queries whether I am bound to the specified
	 * <code>elementTypeDescriptor</code>.
	 * 
	 * @param elementTypeDescriptor
	 *            an element type descriptor
	 * @return <code>true</code> if I am bound to the
	 *         <code>elementTypeDescriptor</code>; <code>false</code>,
	 *         otherwise
	 */
	boolean includes(IElementTypeDescriptor elementTypeDescriptor);

	/**
	 * Queries whether I am bound to the specified <code>elementType</code>.
	 * 
	 * @param elementType
	 *            an element type
	 * @return <code>true</code> if I am bound to the <code>elementType</code>;
	 *         <code>false</code>, otherwise
	 */
	boolean includes(IElementType elementType);

	/**
	 * Queries whether I am bound to the specified <code>adviceDescriptor</code>.
	 * 
	 * @param advice
	 *            an edit helper advice descriptor
	 * @return <code>true</code> if I am bound to the <code>a</code>;
	 *         <code>false</code>, otherwise
	 */
	boolean includes(IEditHelperAdviceDescriptor adviceDescriptor);
}