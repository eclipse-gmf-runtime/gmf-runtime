/*
 * Copyright (c) 2015 Christian W. Damus and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Christian W. Damus - initial API and implementation 
 */

package org.eclipse.gmf.runtime.emf.type.core;

/**
 * A notification that an {@linkplain #getAdviceBinding() advice binding} has
 * been removed from the {@linkplain ElementTypeRegistry registry}.
 * 
 * @since 1.9
 */
public class AdviceBindingRemovedEvent {

	private IAdviceBindingDescriptor adviceBinding;

	/**
	 * Initializes me with the advice binding that was removed.
	 * 
	 * @param elementType
	 *            the removed element type
	 */
	AdviceBindingRemovedEvent(IAdviceBindingDescriptor adviceBinding) {
		super();

		this.adviceBinding = adviceBinding;
	}

	/**
	 * Queries the identifier of the advice binding that was removed. Note that,
	 * because the advice binding is no longer actually available in the
	 * registry to be looked up, it is {@linkplain #getAdviceBinding() provided}
	 * by this event object.
	 * 
	 * @return the removed advice binding's identifier
	 * 
	 * @see #getAdviceBinding()
	 */
	public String getAdviceBindingId() {
		return adviceBinding.getId();
	}

	/**
	 * Queries the advice binding that was removed.
	 * 
	 * @return the removed advice binding
	 */
	public IAdviceBindingDescriptor getAdviceBinding() {
		return adviceBinding;
	}
}
