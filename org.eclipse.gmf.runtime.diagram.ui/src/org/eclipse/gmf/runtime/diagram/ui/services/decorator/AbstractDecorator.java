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

import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy.DecoratorTarget;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.Decoration;

/**
 * Abstract Decorator class. Clients can have their decorator subclass this to
 * inherit utility methods for adding decoration figures.
 * 
 * @see IDecorator
 * 
 * @author cmahoney
 */
public abstract class AbstractDecorator
	implements IDecorator {

	/** the object to be decorated */
	private DecoratorTarget decoratorTarget;

	/** the decoration being displayed */
	private Decoration decoration;

	/**
	 * Creates a new <code>AbstractDecorator</code> for the decorator target
	 * passed in.
	 * 
	 * @param decoratorTarget
	 *            the object to be decorated
	 */
	public AbstractDecorator(IDecoratorTarget decoratorTarget) {
		Assert.isTrue(decoratorTarget instanceof DecoratorTarget);
		this.decoratorTarget = (DecoratorTarget) decoratorTarget;
	}

	/**
	 * Gets the object to be decorated.
	 * 
	 * @return Returns the object to be decorated
	 */
	protected DecoratorTarget getDecoratorTarget() {
		return decoratorTarget;
	}

	/**
	 * @return Returns the decoration.
	 */
	public Decoration getDecoration() {
		return decoration;
	}

	/**
	 * @param decoration
	 *            The decoration to set.
	 */
	public void setDecoration(IDecoration decoration) {
		Assert.isTrue(decoration instanceof Decoration);
		this.decoration = (Decoration) decoration;
	}

	/**
	 * Removes the decoration if it exists and sets it to null.
	 */
	protected void removeDecoration() {
		if (decoration != null) {
			decoratorTarget.removeDecoration(decoration);
			decoration = null;
		}
	}

	/**
	 * Removes the decoration.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#deactivate()
	 */
	public void deactivate() {
		removeDecoration();
	}

}