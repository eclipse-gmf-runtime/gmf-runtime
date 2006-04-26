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

package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.jface.util.Assert;

/**
 * The operation used with the Decoration Service.
 * The CreateDecoratorsOperation is instantiated with an IDecoratorTarget.
 * 
 * The decorator target is the object that is to be decorated with an image
 * or figure.
 * 
 * @author cmahoney
 */
public /*final*/ class CreateDecoratorsOperation
	implements IOperation {

    /** the decorator target */
    protected final IDecoratorTarget decoratorTarget;

    /**
     * Constructor for <code>CreateDecoratorsOperation</code>.
     * 
     * @param decoratorTarget
     *            the object to be decorated
     */
    public CreateDecoratorsOperation(IDecoratorTarget decoratorTarget) {
        Assert.isNotNull(decoratorTarget);
        this.decoratorTarget = decoratorTarget;
    }

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
	 */
	public Object execute(IProvider provider) {
		((IDecoratorProvider) provider).createDecorators(getDecoratorTarget());
		return null;
	}
	
	/**
	 * Returns the object to be decorated.
	 * 
	 * @return the decorator target
	 */
	public final IDecoratorTarget getDecoratorTarget() {
		return decoratorTarget;
	}
}
