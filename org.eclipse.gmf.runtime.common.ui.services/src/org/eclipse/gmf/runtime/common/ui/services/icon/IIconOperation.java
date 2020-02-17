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

package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * The operation used by the {@link IconService}.
 * 
 * @author ldamus
 */
public interface IIconOperation extends IOperation {

	/**
	 * Gets the adaptable hint.
	 * 
	 * @return the hint
	 */
	public abstract IAdaptable getHint();
}
