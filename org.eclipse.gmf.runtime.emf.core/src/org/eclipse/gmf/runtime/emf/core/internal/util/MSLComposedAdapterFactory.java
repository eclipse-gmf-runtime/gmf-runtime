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

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * An MSL composed adapter factory exposes its contents.
 * 
 * @author rafikj
 */
public class MSLComposedAdapterFactory
	extends ComposedAdapterFactory {

	/**
	 * Constructor.
	 */
	public MSLComposedAdapterFactory() {
		super(Collections.EMPTY_LIST);
	}

	/**
	 * Get the child factories.
	 */
	public List getFactories() {
		return adapterFactories;
	}
}