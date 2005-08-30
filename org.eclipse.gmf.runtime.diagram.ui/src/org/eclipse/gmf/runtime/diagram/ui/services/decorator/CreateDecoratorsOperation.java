/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.CreateDecoratorsOperationBase;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorTargetBase;

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
	extends CreateDecoratorsOperationBase implements IOperation {

	/**
	 * Constructor for <code>CreateDecoratorsOperation</code>.
	 * 
	 * @param decoratorTarget
	 *            the object to be decorated
	 */
	public CreateDecoratorsOperation(IDecoratorTargetBase decoratorTarget) {
		super(decoratorTarget);
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
		return (IDecoratorTarget) decoratorTarget;
	}
}
