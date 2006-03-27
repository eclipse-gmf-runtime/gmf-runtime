/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal;

import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.ui.util.UIModificationValidator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The common UI plug-in.
 * 
 * @author khussey
 * 
 */
public class CommonUIPlugin
	extends AbstractUIPlugin {

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonUIPlugin plugin;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Override to initialize the modification validator.
	 */
	public void start(BundleContext context)
		throws Exception {
		super.start(context);
		
		// Make sure validateEdit for resources affected by GMF operations is
		// done with UI context.
		UIModificationValidator uiValidator = new UIModificationValidator();
		FileModificationValidator.setModificationValidator(uiValidator);
	}

}