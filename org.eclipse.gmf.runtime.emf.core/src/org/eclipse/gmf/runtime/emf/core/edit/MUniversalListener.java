/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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