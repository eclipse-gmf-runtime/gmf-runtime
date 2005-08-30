/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
