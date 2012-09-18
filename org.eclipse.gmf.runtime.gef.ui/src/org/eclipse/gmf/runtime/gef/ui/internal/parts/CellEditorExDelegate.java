/******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Svyatoslav Kovalsky (Montages) - contribution for bugzilla 371888
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.parts;

import org.eclipse.jface.viewers.CellEditor;

/**
 * @since 1.6
 */
public class CellEditorExDelegate {

	private final CellEditor myOwner;

	private Object originalValue;

	private boolean deactivationLock = false;

	public CellEditorExDelegate(CellEditor owner) {
		myOwner = owner;
	}

	public void setOriginalValue(Object value) {
		if (originalValue == null)
			originalValue = value;
	}

	/**
	 * @return boolean value specifying whether or not the value has been
	 *         changed
	 */
	public boolean hasValueChanged() {
		if (myOwner.getValue() == null)
			return originalValue != null;
		return !myOwner.getValue().equals(originalValue);
	}

	/**
	 * Returns true if deactivation has been locked
	 * 
	 * @return
	 */
	public boolean isDeactivationLocked() {
		return deactivationLock;
	}

	/**
	 * Sets deactivation lock so that the cell editor does not perform
	 * deactivate
	 * 
	 * @param deactivationLock
	 */
	public void setDeactivationLock(boolean deactivationLock) {
		this.deactivationLock = deactivationLock;
	}

	/*
	 * unlocks deactivation if it has been locked
	 */
	public boolean unlockDeactivation() {
		if (isDeactivationLocked()) {
			setDeactivationLock(false);
			return true;
		} else {
			return false;
		}
	}
}
