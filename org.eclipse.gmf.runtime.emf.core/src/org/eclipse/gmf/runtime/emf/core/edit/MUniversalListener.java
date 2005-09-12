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

package org.eclipse.gmf.runtime.emf.core.edit;

/**
 * @author rafikj
 * 
 * A universal listener listens to changes that span all the currently present
 * resource sets.
 */
public abstract class MUniversalListener
	extends MListener {

	/**
	 * Constructor.
	 */
	public MUniversalListener() {
		super(null, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param filter
	 *            The filter.
	 */
	public MUniversalListener(MFilter filter) {
		super(null, filter);
	}
}