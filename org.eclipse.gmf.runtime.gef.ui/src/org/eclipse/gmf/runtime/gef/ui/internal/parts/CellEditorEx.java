/******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Svyatoslav Kovalsky (Montages) - contribution for bugzilla 371888
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.parts;

/**
 * @since 1.7
 */
public interface CellEditorEx {

	public void setValueAndProcessEditOccured(Object value);

	public boolean hasValueChanged();

	public boolean isDeactivationLocked();

	public void setDeactivationLock(boolean deactivationLock);
}
