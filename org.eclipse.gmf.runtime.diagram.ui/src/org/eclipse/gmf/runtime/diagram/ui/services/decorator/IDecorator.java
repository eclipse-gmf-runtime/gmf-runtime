/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
