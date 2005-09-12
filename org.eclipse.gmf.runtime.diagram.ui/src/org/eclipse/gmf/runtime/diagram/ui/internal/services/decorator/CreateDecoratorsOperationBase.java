/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator;

import org.eclipse.jface.util.Assert;

/**
 * The operation used with the Decoration Service.
 * The CreateDecoratorsOperation is instantiated with an IDecoratorTarget.
 * 
 * The decorator target is the object that is to be decorated with an image
 * or figure.
 * 
 * @author cmahoney
 * @canBeSeenBy %level1
 */
public /*final*/ class CreateDecoratorsOperationBase {

	/** the decorator target */
	protected final IDecoratorTargetBase decoratorTarget;

	/**
	 * Constructor for <code>CreateDecoratorsOperation</code>.
	 * 
	 * @param decoratorTarget
	 *            the object to be decorated
	 */
	public CreateDecoratorsOperationBase(IDecoratorTargetBase decoratorTarget) {
		Assert.isNotNull(decoratorTarget);
		this.decoratorTarget = decoratorTarget;
	}

}
