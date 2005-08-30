/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator;

import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;

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
 * @canBeSeenBy %level1
 */
public interface IDecoratorBase {

	/**
	 * Activates this decorator. The decorator might need to hook listeners.
	 * These listeners should be unhooked in <code>deactivate()</code>.
	 * 
	 * @see #deactivate()
	 */
	public void activate();

	/**
	 * Deactivates this decorator, the inverse of {@link #activate()}.
	 * Deactivate is called when the host decorator target is deactivated.
	 * Deactivate unhooks any listeners, and removes decoration figures that
	 * have been added.
	 * 
	 * @see #activate()
	 */
	public void deactivate();

	/**
	 * Refreshes the decorations. This is called when the host decorator target
	 * is refreshed.
	 */
	public void refresh();

}
