/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorBase;

/**
 * Clients providing an extension to the DecoratorService need to create  
 * a decorator class that implements the IDecorator interface.
 * 
 * This is the interface that a decorator must implement. A decorator is
 * installed on a decorator target to which it will add decoration figures.
 * 
 * @see IDecoratorProvider
 * 
 * @author cmahoney
 */
public interface IDecorator extends IDecoratorBase {
	// empty interface
}
