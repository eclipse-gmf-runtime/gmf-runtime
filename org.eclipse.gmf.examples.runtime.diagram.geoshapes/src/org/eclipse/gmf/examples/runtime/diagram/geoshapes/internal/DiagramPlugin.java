/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal;

import org.eclipse.gmf.runtime.emf.core.internal.util.MetamodelManager;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * Geoshapes Diagram Example Plug-in
 * 
 * @author qili
 */
public class DiagramPlugin extends AbstractUIPlugin {

	/**
	 * The shared instance.
	 */
	private static DiagramPlugin plugin;

	/**
	 * The constructor.
	 */
	public DiagramPlugin() {
		super();
		plugin = this;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @return the plugin instance
	 */
	public static DiagramPlugin getInstance() {
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

		MetamodelManager.register(NotationPackage.eINSTANCE, null);
	}


}
