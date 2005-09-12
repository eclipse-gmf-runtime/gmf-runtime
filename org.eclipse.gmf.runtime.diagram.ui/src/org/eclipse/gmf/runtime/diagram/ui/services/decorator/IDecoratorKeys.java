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

/**
 * A list of keys defining decorators that could potentially be overridden. A
 * decorator is installed on a decorator target using a key (a String
 * identifier), if another decorator is installed on the same target with the
 * same key then it will override the previous one installed. This is similar to
 * how EditPolicy roles work. If a decorator is installed that may need to be
 * overridden, add its key here.
 * 
 * @author cmahoney
 */
public interface IDecoratorKeys {

	/** The key used for the cross model reference decoration. */
	public static final String CROSS_MODEL_REFERENCE = "CrossModelReference"; //$NON-NLS-1$

	/** The key used for the unresolved view decoration. */
	public static final String UNRESOLVED_VIEW = "UnresolvedView"; //$NON-NLS-1$

	/** The key used for the bookmark decoration. */
	public static final String BOOKMARK = "Bookmark"; //$NON-NLS-1$

}