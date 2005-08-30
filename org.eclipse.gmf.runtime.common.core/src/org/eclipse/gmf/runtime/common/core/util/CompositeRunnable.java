/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;

/**
 * A utility class that is a composite of runnables
 * 
 * @author Yasser Lulu
 * @canBeSeenBy %partners
 */
public class CompositeRunnable
	implements Runnable {

	/**
	 * The iterator to use to get runnables
	 */
	private Runnable[] runnables;

	/**
	 * Constructor for CompositeRunnable.
	 * 
	 * @param runnables list of runnable to be composed into a <code>CompositeRunnable</code>
	 */
	public CompositeRunnable(Runnable[] runnables) {
		this.runnables = runnables;
	}

	/**
	 * The body of the runnable that runs the composite list of rannables.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			for (int i = 0; i < runnables.length; i++) {
				runnables[i].run();
			}
		} catch (Exception ex) {
			Trace.catching(CommonCorePlugin.getDefault(),
				CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(), "run", //$NON-NLS-1$
				ex);
		} finally {
			runnables = null;
		}
	}

}