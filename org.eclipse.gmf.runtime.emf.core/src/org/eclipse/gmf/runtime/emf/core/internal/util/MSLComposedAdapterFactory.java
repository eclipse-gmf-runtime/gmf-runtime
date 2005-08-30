/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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