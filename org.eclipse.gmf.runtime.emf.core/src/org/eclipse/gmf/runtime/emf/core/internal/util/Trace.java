/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.internal.util;

import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;


public class Trace {
	/** Cannot be instantiated. */
	private Trace() {
		super();
	}

	/**
	 * Queries whether the specified trace <code>option</code> is enabled.
	 * 
	 * @param option a trace option
	 * @return whether if it is enabled
	 */
	public static boolean isEnabled(String option) {
		return org.eclipse.gmf.runtime.common.core.util.Trace.shouldTrace(
			MSLPlugin.getDefault(),
			option);
	}
	
	/**
	 * Traces the specified <code>message</code> under the given
	 * <code>option</code>.
	 * 
	 * @param option the trace option
	 * @param message the message to trace
	 * 
	 * @see #isEnabled(String)
	 */
	public static void trace(String option, String message) {
		org.eclipse.gmf.runtime.common.core.util.Trace.trace(
			MSLPlugin.getDefault(),
			option,
			message);
	}
	
	/**
	 * Traces a caught exception.
	 * 
	 * @param clazz the class that caught the exception
	 * @param methodName the method in which it was caught
	 * @param t the exception
	 */
	public static void catching(Class clazz, String methodName, Throwable t) {
		org.eclipse.gmf.runtime.common.core.util.Trace.catching(
			MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_CATCHING,
			clazz,
			methodName,
			t);
	}
	
	/**
	 * Traces a thrown exception.
	 * 
	 * @param clazz the class that is throwing the exception
	 * @param methodName the method from which it is being thrown
	 * @param t the exception
	 */
	public static void throwing(Class clazz, String methodName, Throwable t) {
		org.eclipse.gmf.runtime.common.core.util.Trace.throwing(
			MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING,
			clazz,
			methodName,
			t);
	}
}
