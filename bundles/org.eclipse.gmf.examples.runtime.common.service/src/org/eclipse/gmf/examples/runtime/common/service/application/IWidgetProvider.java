/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


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
