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
 * A specialized registry listener protocol that notifies clients when element
 * types are dynamically removed from the registry and when advice bindings are
 * added or removed.
 * 
 * @since 1.9
 */
public interface IElementTypeRegistryListener2 extends IElementTypeRegistryListener {

	/**
	 * Notifies me that an element type has been dynamically removed from the
	 * system and is, therefore, no longer a valid usable type.
	 * 
	 * @param event
	 *            the element type removal event
	 */
	void elementTypeRemoved(ElementTypeRemovedEvent event);

	/**
	 * Notifies me that a new advice binding has been added to the element type
	 * registry.
	 * 
	 * @param event
	 *            the advice binding addition event
	 */
	void adviceBindingAdded(AdviceBindingAddedEvent event);

	/**
	 * Notifies me that an advice binding has been dynamically removed from the
	 * system and is, therefore, no longer in effect.
	 * 
	 * @param event
	 *            the advice binding removal event
	 */
	void adviceBindingRemoved(AdviceBindingRemovedEvent event);
}
