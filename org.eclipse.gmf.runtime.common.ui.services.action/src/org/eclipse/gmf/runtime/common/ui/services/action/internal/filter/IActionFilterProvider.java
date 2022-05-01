/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.internal.filter;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.ui.IActionFilter;

/**
 * The interface for providers of action filters, implementations of
 * <code>IActionFilter</code>.
 * 
 * @author khussey
 * 
 * @see org.eclipse.ui.IActionFilter
 *
 */
public interface IActionFilterProvider extends IActionFilter, IProvider {
	 /* no interface body */
}
