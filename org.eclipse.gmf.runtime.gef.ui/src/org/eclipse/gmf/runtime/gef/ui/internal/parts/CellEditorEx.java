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

/**
 * @since 1.7
 */
public interface CellEditorEx {

	public void setValueAndProcessEditOccured(Object value);

	public boolean hasValueChanged();

	public boolean isDeactivationLocked();

	public void setDeactivationLock(boolean deactivationLock);
}
