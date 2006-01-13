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
package org.eclipse.gmf.runtime.diagram.ui.properties.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesServiceAdapterFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.util.SectionUpdateRequestCollapser;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class DiagramPropertiesPlugin
	extends AbstractUIPlugin {

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
	 * Starts up this plug-in.
	 */
	public void start(BundleContext context)
		throws Exception {
		super.start(context);
		configurePropertiesAdapter();
		updateRequestCollapser = new SectionUpdateRequestCollapser();
		updateRequestCollapser.start();

	}

	/**
	 * Shuts down this plug-in and discards all plug-in state.
	 */
	public void stop(BundleContext context)
		throws Exception {
		super.stop(context);
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