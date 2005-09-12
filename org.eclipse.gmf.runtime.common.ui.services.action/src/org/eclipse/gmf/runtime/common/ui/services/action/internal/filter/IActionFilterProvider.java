/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.internal.filter;

import org.eclipse.ui.IActionFilter;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

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
