/*
 * Copyright (c) 2015 Christian W. Damus and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian W. Damus - initial API and implementation 
 */

package org.eclipse.gmf.runtime.emf.type.core;

/**
 * A listener protocol that notifies clients when {@linkplain IClientContext
 * client contexts} are dynamically removed from the
 * {@linkplain ClientContextManager context manager}.
 * 
 * @since 1.9
 */
public interface IClientContextManagerListener {

	/**
	 * Notifies me that a new client context has been added to the context
	 * manager.
	 * 
	 * @param event
	 *            the client context addition event
	 */
	void clientContextAdded(ClientContextAddedEvent event);

	/**
	 * Notifies me that a client context has been dynamically removed from the
	 * system and is, therefore, no longer in effect.
	 * 
	 * @param event
	 *            the client context removal event
	 */
	void clientContextRemoved(ClientContextRemovedEvent event);
}
