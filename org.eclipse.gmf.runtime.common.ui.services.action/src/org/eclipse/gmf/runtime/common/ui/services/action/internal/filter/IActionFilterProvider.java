/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
