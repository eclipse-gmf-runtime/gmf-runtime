/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
