/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Logic Diagram Plug-in
 * 
 * @author qili
 */
public class LogicDiagramPlugin
	extends AbstractUIPlugin {

	/**
	 * The logic diagram editor ID. This matches the id used in this plugin's
	 * XML for the editor extension point.
	 */
	public static final String EDITOR_ID = "LogicEditor"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static LogicDiagramPlugin plugin;

	/**
	 * The constructor.
	 */
	public LogicDiagramPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static LogicDiagramPlugin getInstance() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

	/**
	 * Starts up this wizards plug-in.
	 */
	public void start(BundleContext context)
		throws Exception {
		super.start(context);
		
		PreferencesHint.registerPreferenceStore(
			new PreferencesHint(EDITOR_ID), getPreferenceStore());
	}

}