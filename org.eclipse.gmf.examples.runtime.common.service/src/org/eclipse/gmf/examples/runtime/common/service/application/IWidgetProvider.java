/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.common.service.application;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for Widget providers.  Defines the messages between the WidgetService and 
 * Widget providers.
 * 
 */
public interface IWidgetProvider
	extends IProvider {
	
	Object createWidget(int orderSize);	

}
