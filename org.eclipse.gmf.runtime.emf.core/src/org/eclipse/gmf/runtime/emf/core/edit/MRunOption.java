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
 * Enumeration that describes options that can be passed to run methods.
 * 
 * @see MEditingDomain#runWithOptions(MRunnable, int)
 * 
 * @author rafikj
 */
public final class MRunOption {

	/**
	 * Indicates that the runnable should be run without causing events to be
	 * sent.
	 * 
	 * @see MEditingDomain#runSilent(MRunnable)
	 */
	public static final int SILENT = 1;

	/**
	 * Indicates that the runnable should be run without being validated.
	 * 
	 * @see MEditingDomain#runUnvalidated(MRunnable)
	 */
	public static final int UNVALIDATED = 2;

	/**
	 * Indicates that the runnable should be run without causing semantic
	 * procedures to be run.
	 * 
	 * @see MEditingDomain#runWithNoSemProcs(MRunnable)
	 */
	public static final int NO_SEM_PROCS = 4;

	/**
	 * Indicates that the runnable should be run in an unchecked action and will
	 * take care of starting and completing the unchecked action.
	 * 
	 * @see MEditingDomain#runAsUnchecked(MRunnable)
	 */
	public static final int UNCHECKED = 8;

	/**
	 * Indicates that the runnable should be run without building reverse
	 * reference map.
	 * 
	 * @deprecated This option is discontinued.
	 */
	public static final int NO_REFERENCE_MANAGER = 16;

	/**
	 * Indicates that the runnable should build reverse reference map.
	 * 
	 * @deprecated This option is discontinued.
	 */
	public static final int REFERENCE_MANAGER = 32;

	private MRunOption() {
		// private
	}
}