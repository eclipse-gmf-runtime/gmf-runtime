/******************************************************************************
 * Copyright (c) 2012 Montages.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Svyatoslav Kovalsky (Montages) - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.parts;

/**
 * @since 1.6
 */
public interface CellEditorEx {

	public void setValueAndProcessEditOccured(Object value);

	public boolean hasValueChanged();

	public boolean isDeactivationLocked();

	public void setDeactivationLock(boolean deactivationLock);
}
