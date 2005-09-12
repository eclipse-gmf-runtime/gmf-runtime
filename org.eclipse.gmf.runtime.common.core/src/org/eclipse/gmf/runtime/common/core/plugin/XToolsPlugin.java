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

package org.eclipse.gmf.runtime.common.core.plugin;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * The abstract parent of all XTools plug-ins.
 * 
 * @author khussey
 * @canBeSeenBy %partners
 */
public abstract class XToolsPlugin
	extends Plugin {

	/**
	 * Error message used when startup of a plug-in fails.
	 */
	protected static final String STARTUP_ERROR_MESSAGE = ResourceManager
		.getI18NString("XToolsPlugin._ERROR_.startupErrorMessage"); //$NON-NLS-1$

	/**
	 * Error message used when shutdown of a plug-in fails.
	 */
	protected static final String SHUTDOWN_ERROR_MESSAGE = ResourceManager
		.getI18NString("XToolsPlugin._ERROR_.shutdownErrorMessage"); //$NON-NLS-1$

	/**
	 * Creates a new plug-in runtime object.
	 */
	protected XToolsPlugin() {
		super();
	}

	/**
	 * Creates a new plug-in runtime object for the given plug-in descriptor.
	 * 
	 * @param descriptor
	 *            The plug-in descriptor.
	 * @deprecated replace with constructor with no parameters since
	 *             IPluginDescriptor is deprecated.
	 */
	protected XToolsPlugin(org.eclipse.core.runtime.IPluginDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Retrieves a resource manager instance for this plug-in.
	 * 
	 * @return A resource manager instance for this plug-in.
	 */
	public abstract AbstractResourceManager getResourceManager();

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		/* empty method body */
	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 */
	protected void doShutdown() {
		/* empty method body */
	}

	/**
	 * Retrieves an error message that indicates an error during this plug-in's
	 * startup sequence.
	 * 
	 * @return An error message string.
	 * @param pluginName
	 *            The name of this plug-in.
	 */
	protected String getStartupErrorMessage(String pluginName) {
		return MessageFormat.format(STARTUP_ERROR_MESSAGE,
			new Object[] {pluginName});
	}

	/**
	 * Retrieves an error message that indicates an error during this plug-in's
	 * shutdown sequence.
	 * 
	 * @return An error message string.
	 * @param pluginName
	 *            The name of this plug-in.
	 */
	protected String getShutdownErrorMessage(String pluginName) {
		return MessageFormat.format(SHUTDOWN_ERROR_MESSAGE,
			new Object[] {pluginName});
	}

	/**
	 * Retrieves the symbolic name of the plug-in (reverse domain name naming convention).
	 * @return the symbolic name of the plug-in.
	 */
	public final String getSymbolicName() {
		String name = null;

		try {
			name = getBundle().getSymbolicName();
		} catch (Exception e) {
			// ignore the exception
		}

		return String.valueOf(name);
	}

	/**
	 * Retrieves the name of the plug-in.
	 * @return the name of the plug-in.
	 */
	public final String getPluginName() {
		Object name;

		try {
			name = getBundle().getHeaders().get(Constants.BUNDLE_NAME);
		} catch (Exception e) {
			name = getSymbolicName();
		}

		return String.valueOf(name);
	}

	/**
	 * Starts up this plug-in.
	 * 
	 * @exception Exception
	 *                If this plug-in did not start up properly.
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public final void start(BundleContext context)
		throws Exception {
		super.start(context);

		try {
			Log.info(this, CommonCoreStatusCodes.OK, getPluginName()
				+ " plug-in starting up..."); //$NON-NLS-1$

			long startTime = System.currentTimeMillis();
			doStartup();
			long duration = System.currentTimeMillis() - startTime;

			Trace
				.trace(
					this,
					"Plug-in '" + getSymbolicName() + "' started up: " + duration + " msec"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			Trace.catching(this, CommonCoreDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "startup", e); //$NON-NLS-1$
			Log.error(CommonCorePlugin.getDefault(),
				CommonCoreStatusCodes.PLUGIN_STARTUP_FAILURE, "startup", e); //$NON-NLS-1$
			CoreException ce = new CoreException(new Status(IStatus.ERROR,
				getSymbolicName(),
				CommonCoreStatusCodes.PLUGIN_STARTUP_FAILURE,
				getStartupErrorMessage(getPluginName()), e));
			Trace.throwing(this, CommonCoreDebugOptions.EXCEPTIONS_THROWING,
				getClass(), "startup", ce); //$NON-NLS-1$
			throw ce;
		}
	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 * 
	 * @exception CoreException
	 *                If this method fails to shut down this plug-in.
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public final void stop(BundleContext context)
		throws Exception {
		try {
			Log.info(this, CommonCoreStatusCodes.OK, getPluginName()
				+ " plug-in shutting down..."); //$NON-NLS-1$

			doShutdown();

			Trace.trace(this, "Plug-in '" + getSymbolicName() + "' shut down."); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			Trace.catching(this, CommonCoreDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "shutdown", e); //$NON-NLS-1$
			Log.error(CommonCorePlugin.getDefault(),
				CommonCoreStatusCodes.PLUGIN_SHUTDOWN_FAILURE, "shutdown", e); //$NON-NLS-1$
			CoreException ce = new CoreException(new Status(IStatus.ERROR,
				getSymbolicName(),
				CommonCoreStatusCodes.PLUGIN_SHUTDOWN_FAILURE,
				getShutdownErrorMessage(getPluginName()), e));
			Trace.throwing(this, CommonCoreDebugOptions.EXCEPTIONS_THROWING,
				getClass(), "shutdown", ce); //$NON-NLS-1$
			throw ce;
		} finally {
			super.stop(context);
		}
	}
}