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
package org.eclipse.gmf.runtime.diagram.ui.properties.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesServiceAdapterFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.util.SectionUpdateRequestCollapser;

/**
 * The main plugin class to be used in the desktop.
 */
public class DiagramPropertiesPlugin
	extends XToolsUIPlugin {

	// The shared instance.
	private static DiagramPropertiesPlugin plugin;

	/*
	 * The event collapser. Used to collapse mutiple update requests when
	 * multi-deleting or multi-selecting
	 */
	private SectionUpdateRequestCollapser updateRequestCollapser;

	/**
	 * The constructor.
	 */
	public DiagramPropertiesPlugin() {
		super();
		plugin = this;

	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance of <code>DiagramPropertiesPlugin</code>
	 */
	public static DiagramPropertiesPlugin getDefault() {
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
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.plugin.XToolsPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		configurePropertiesAdapter();
		updateRequestCollapser = new SectionUpdateRequestCollapser();
		updateRequestCollapser.start();

	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 */
	protected void doShutdown() {

		updateRequestCollapser.stop();

	}

	/**
	 * @return Returns the updateRequestCollapser.
	 */
	public SectionUpdateRequestCollapser getUpdateRequestCollapser() {
		return updateRequestCollapser;
	}

	/**
	 * Configures properties providers based on properties provider extension
	 * configurations.
	 * 
	 */
	private void configurePropertiesAdapter() {
		Platform.getAdapterManager().registerAdapters(
			new PropertiesServiceAdapterFactory(), IGraphicalEditPart.class);
	}
}