/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecorationBase;

/**
 * This represents a decoration object returned by a decorator target. The
 * client holds onto this so that it can be deleted later.
 * 
 * @author cmahoney
 */
public interface IDecoration
	extends IDecorationBase {

	/**
	 * If this flag is set to true, then the decoration's visibility is not to
	 * be affected by the parent's visibility. If this flag is set to false, if
	 * the any figures in the parent hierarchy of this decoration is not
	 * visible, then the decoration will be hidden.
	 * 
	 * @param ignoreParentVisibility
	 *            True if the decoration's visibility is not to be affected by
	 *            the parent's visibility; false otherwise.
	 */
	public void setIgnoreParentVisibility(boolean ignoreParentVisibility);

	/**
	 * Should this decoration's visibility be affected by the parent's
	 * visibility?
	 * 
	 * @return True if the decoration's visibility is not to be affected by the
	 *         parent's visibility; false otherwise.
	 */
	public boolean shouldIgnoreParentVisibility();

}
