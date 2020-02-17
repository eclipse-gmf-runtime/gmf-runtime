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

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

/**
 * Locking mechanism used by {@link GlobalUndoAction} and {@link GlobalRedoAction}
 * to prevent nested undo / redo actions from occuring.
 */
final class GlobalUndoRedoLock {

	/**
	 * Lock instance.
	 */
	public static final GlobalUndoRedoLock INSTANCE = new GlobalUndoRedoLock();

	/**
	 * The locking object.
	 */
	private Object owner = null;
	
	/**
	 * Private constructor.
	 */
	private GlobalUndoRedoLock() {
		// private
	}

	/**
	 * Acquires the lock if the lock is free and returns true, otherwise false.
	 * 
	 * @param key the key Object which can release the lock.
	 * @return <code>true</code> if lock acquired, otherwise <code>false</code>
	 */
	synchronized boolean acquire(Object key) {
		if (owner == null) {
			owner = key;
			return true;
		}
		return false;
	}
	
	/**
	 * Releases the lock if the key is correct. If the key is incorrect,
	 * then an IllegalArgumentException is thrown.
	 * 
	 * @param key the key which acquired the lock
	 */
	synchronized void release(Object key) {
		if (owner == key) {
			owner = null;
			return;
		}
		if (owner == null) {
			return;
		}
		throw new IllegalArgumentException("Unable to release lock, incorrect key."); //$NON-NLS-1$
	}
}
