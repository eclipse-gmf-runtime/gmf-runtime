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

package org.eclipse.gmf.runtime.emf.type.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;

/**
 * The implementation of the client context interface.
 * <p>
 * This class may be instantiated or subclassed by clients.
 * 
 * @author ldamus
 */
public class ClientContext implements IClientContext {

	private final String id;

	private final IElementMatcher matcher;

	// set of String type and advice IDs that are bound to me
	private final Set typeIdBindings = new java.util.HashSet();

	// set of regex patterns that are bound to me
	private final Set patternBindings = new java.util.HashSet();

	/**
	 * Initializes me with my ID and my element matcher.
	 * 
	 * @param id
	 *            my unique identifier
	 * @param matcher
	 *            my element matcher
	 */
	public ClientContext(String id, IElementMatcher matcher) {
		this.id = id;
		this.matcher = matcher;
	}

	public final String getId() {
		return id;
	}

	public final IElementMatcher getMatcher() {
		return matcher;
	}

	public boolean includes(IEditHelperAdviceDescriptor adviceDescriptor) {
		return includes(adviceDescriptor.getId());
	}

	public boolean includes(IElementTypeDescriptor elementTypeDescriptor) {
		return includes(elementTypeDescriptor.getId());
	}

	public boolean includes(IElementType elementType) {
		return includes(elementType.getId());
	}

	private boolean includes(String id) {
		boolean result = false;

		result = typeIdBindings.contains(id);

		if (!result && !patternBindings.isEmpty()) {
			// look for a bound pattern
			result = hasPatternBindingFor(id);

			if (result) {
				// cache the result for this type
				bindId(id);
			}
		}

		return result;
	}

	/**
	 * Determines whether any of my pattern bindings matches the
	 * <code>toMatch</code> string.
	 * 
	 * @param toMatch
	 *            the string to be matched
	 * @return <code>true</code> if any of my pattern bindings matches the
	 *         <code>toMatch</code> string; <code>false</code>, otherwise
	 */
	private boolean hasPatternBindingFor(String toMatch) {

		for (Iterator iter = patternBindings.iterator(); iter.hasNext();) {
			Pattern pattern = (Pattern) iter.next();

			if (pattern.matcher(toMatch).matches()) {
				return true;
			}
		}

		return false;
	}

	public void bindId(String typeId) {
		typeIdBindings.add(typeId);
	}

	public void bindPattern(Pattern pattern) {
		patternBindings.add(pattern);
	}

	/**
	 * Has no children.
	 */
	public Collection getChildren() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Not a multi-context.
	 */
	public boolean isMultiClientContext() {
		return false;
	}

	/**
	 * The context ID fully determines equality.
	 */
	public boolean equals(Object obj) {
		return (obj instanceof ClientContext)
				&& ((ClientContext) obj).getId().equals(getId());
	}

	/**
	 * The context ID fully determines equality.
	 */
	public int hashCode() {
		return getId().hashCode();
	}

	public String toString() {
		return "ClientContext[" + getId() + ']'; //$NON-NLS-1$
	}
}
