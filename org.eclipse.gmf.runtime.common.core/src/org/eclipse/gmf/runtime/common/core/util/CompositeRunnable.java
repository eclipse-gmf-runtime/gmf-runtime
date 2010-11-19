/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;

/**
 * A utility class that is a composite of runnables
 * 
 * @author Yasser Lulu
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
