/******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation, Christian W. Damus, and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Christian W. Damus - bug 457888
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;
import org.osgi.framework.FrameworkUtil;

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
	private final Set<String> typeIdBindings = new java.util.HashSet<String>();

	// set of regex patterns that are bound to me
	private final Set<Pattern> patternBindings = new java.util.HashSet<Pattern>();

	private Boolean dynamic;
	
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

	private boolean includes(String _id) {
		boolean result = false;

		result = typeIdBindings.contains(_id);

		if (!result && !patternBindings.isEmpty()) {
			// look for a bound pattern
			result = hasPatternBindingFor(_id);

			if (result) {
				// cache the result for this type
				bindId(_id);
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
	 * Removes a specific element-type/advice binding by ID. Note that patterns
	 * may still match element types and/or advices and implictly re-bind them.
	 * 
	 * @param typeId
	 *            the element-type or advice ID to remove from the context
	 * 
	 * @see #unbindPattern(Pattern)
	 * 
	 * @since 1.9
	 */
	public void unbindId(String typeId) {
		typeIdBindings.remove(typeId);
	}

	/**
	 * Removes a specific element-type/advice binding by regular expression
	 * {@code pattern}. This also implicitly unbinds any specific IDs that match
	 * the pattern.
	 * 
	 * @param pattern
	 *            the element-type or advice ID pattern to remove from the
	 *            context
	 * 
	 * @see #unbindId(String)
	 * 
	 * @since 1.9
	 */
	public void unbindPattern(Pattern pattern) {
		if (patternBindings.remove(pattern)) {
			// Remove all matching IDs
			Matcher m = pattern.matcher(""); //$NON-NLS-1$
			for (Iterator<String> iter = typeIdBindings.iterator(); iter.hasNext();) {
				m.reset(iter.next());
				if (m.matches()) {
					iter.remove();
				}
			}
		}
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
	 * Queries whether I am defined dynamically, not statically. In practice,
	 * this means that I was
	 * {@linkplain ClientContextManager#registerClientContext(IClientContext)
	 * added} to the system at run-time, not via the extension point.
	 * 
	 * @return whether I am a dynamically-defined client context
	 * 
	 * @since 1.9
	 */
	public final boolean isDynamic() {
		if (dynamic == null) {
			if (FrameworkUtil.getBundle(getClass()) != EMFTypePlugin.getPlugin().getBundle()) {
				// The computeDynamic() method must not be overridden outside of
				// the core framework, so don't even call it
				dynamic = Boolean.TRUE;
			} else {
				dynamic = Boolean.valueOf(computeDynamic());
			}
		}

		return dynamic.booleanValue();
	}

	/**
	 * Overridden in subclasses to compute whether I am a dynamic contribution.
	 * May not be overridden outside of the core framework.
	 * 
	 * @return whether I am a dynamic contribution
	 * 
	 * @since 1.9
	 * 
	 * @nooverride This method is not intended to be re-implemented or extended
	 *             by clients.
	 */
	protected boolean computeDynamic() {
		return true;
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
