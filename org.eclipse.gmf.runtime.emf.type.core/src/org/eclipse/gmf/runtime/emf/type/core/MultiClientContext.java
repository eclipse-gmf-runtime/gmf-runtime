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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;

/**
 * Implements a multi-context, which is a compound context representing a group
 * of client contexts.
 * <P>
 * It's matcher matches an <code>EObject</code> if all of child contexts match
 * that <code>EObject</code>.
 * <P>
 * It includes an <code>IElementType</code> or an
 * <code>IEditHelperAdviceDescriptor</code> if all of the child contexts
 * include that <code>IElementType</code> or
 * <code>IEditHelperAdviceDescriptor</code>.
 * 
 * @author ldamus
 */
public class MultiClientContext implements IClientContext {

	/**
	 * The ID of all multi-contexts.
	 */
	public static final String MULTI_CLIENT_CONTEXT_ID = "multi_client_context_ID"; //$NON-NLS-1$

	private final Set children;

	private IElementMatcher matcher;

	/**
	 * Initializes me with my child contexts.
	 * 
	 * @param children
	 *            the {@link IClientContext}s that I represent
	 */
	public MultiClientContext(Collection childContexts) {

		children = new HashSet(childContexts.size());

		// reduce any MultiClientContexts to their child contexts
		for (Iterator i = childContexts.iterator(); i.hasNext();) {
			IClientContext next = (IClientContext) i.next();

			if (next.isMultiClientContext()) {
				Collection nested = ((MultiClientContext) next).getChildren();
				children.addAll(nested);

			} else {
				children.add(next);
			}
		}
	}

	/**
	 * I am a multi-clientContext.
	 */
	public boolean isMultiClientContext() {
		return true;
	}

	/**
	 * Adds the given context to this multi-context. If <code>context</code>
	 * is a multi-context, adds its children instead of itself.
	 * 
	 * @param status
	 *            the new child context
	 */
	public void add(IClientContext context) {
		if (context.isMultiClientContext()) {
			children.addAll(context.getChildren());
		} else {
			children.add(context);
		}
	}

	public final String getId() {
		return MULTI_CLIENT_CONTEXT_ID;
	}

	public Collection getChildren() {
		return children;
	}

	/**
	 * Matches an <code>EObject</code> if all of my children match that
	 * <code>EObject</code>.
	 */
	public final IElementMatcher getMatcher() {

		if (matcher == null) {
			matcher = new IElementMatcher() {
				public boolean matches(EObject eObject) {

					boolean result = !getChildren().isEmpty();
					
					for (Iterator i = getChildren().iterator(); result && i.hasNext();) {
						IClientContext next = (IClientContext) i.next();

						if (!next.getMatcher().matches(eObject)) {
							result = false;
						}
					}
					return result;
				}
			};
		}
		return matcher;
	}

	/**
	 * Binds the <code>typeId</code> to each of my children.
	 */
	public void bindId(String typeId) {
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			IClientContext next = (IClientContext) i.next();
			next.bindId(typeId);
		}
	}

	/**
	 * Binds the <code>pattern</code> to each of my children.
	 */
	public void bindPattern(Pattern pattern) {
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			IClientContext next = (IClientContext) i.next();
			next.bindPattern(pattern);
		}
	}

	/**
	 * Removes a specific element-type/advice binding by ID from each of my
	 * children that are {@link ClientContext}s or {@link MultiClientContext}s.
	 * Note that patterns may still match element types and/or advices and
	 * implictly re-bind them.
	 * 
	 * @param typeId
	 *            the element-type or advice ID to remove from the context
	 * 
	 * @see #unbindPattern(Pattern)
	 * 
	 * @since 1.9
	 */
	public void unbindId(String typeId) {
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			Object next = i.next();
			if (next instanceof ClientContext) {
				((ClientContext) next).unbindId(typeId);
			} else if (next instanceof MultiClientContext) {
				((MultiClientContext) next).unbindId(typeId);
			}
		}
	}

	/**
	 * Removes a specific element-type/advice binding by regular expression
	 * {@code pattern} from each of my children that are {@link ClientContext}s
	 * or {@link MultiClientContext}s. This also implicitly unbinds any specific
	 * IDs that match the pattern.
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
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			Object next = i.next();
			if (next instanceof ClientContext) {
				((ClientContext) next).unbindPattern(pattern);
			} else if (next instanceof MultiClientContext) {
				((MultiClientContext) next).unbindPattern(pattern);
			}
		}
	}

	/**
	 * Includes the <code>elementTypeDescriptor</code> if all of my child
	 * contexts include the <code>elementTypeDescriptor</code>.
	 */
	public boolean includes(IElementTypeDescriptor elementTypeDescriptor) {

		boolean result = !getChildren().isEmpty();
		
		for (Iterator i = getChildren().iterator(); result && i.hasNext();) {
			IClientContext next = (IClientContext) i.next();
			if (!next.includes(elementTypeDescriptor)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Includes the <code>elementType</code> if all of my child contexts
	 * include the <code>elementType</code>.
	 */
	public boolean includes(IElementType elementType) {

		boolean result = !getChildren().isEmpty();
		
		for (Iterator i = getChildren().iterator(); result && i.hasNext();) {
			IClientContext next = (IClientContext) i.next();
			if (!next.includes(elementType)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Includes the <code>advice</code> if all of my child contexts include
	 * the <code>advice</code>.
	 */
	public boolean includes(IEditHelperAdviceDescriptor advice) {
		
		boolean result = !getChildren().isEmpty();
		
		for (Iterator i = getChildren().iterator(); result && i.hasNext();) {
			IClientContext next = (IClientContext) i.next();
			if (!next.includes(advice)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * The children fully determine equality.
	 */
	public boolean equals(Object obj) {

		if (obj instanceof MultiClientContext) {
			return getChildren().equals(((MultiClientContext) obj).getChildren());
		}
		return false;
	}

	/**
	 * The children fully determine equality.
	 */
	public int hashCode() {
		return getChildren().hashCode();
	}

	public String toString() {
		StringBuffer b = new StringBuffer();

		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			b.append(((IClientContext) i.next()).getId());

			if (i.hasNext()) {
				b.append(StringStatics.COMMA);
			}
		}
		return "ClientMultiContext[" + b + ']'; //$NON-NLS-1$
	}
}
