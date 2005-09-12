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

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal;

import org.eclipse.core.runtime.IPluginDescriptor;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLMetaModelManager;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;


/**
 * Geoshapes Diagram Example Plug-in
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 */
public class DiagramPlugin extends XToolsUIPlugin {

	/**
	 * The shared instance.
	 */
	private static DiagramPlugin plugin;

	/**
	 * The constructor.
	 *
	 * @see org.eclipse.core.runtime.Plugin#Plugin(IPluginDescriptor)
	 */
	public DiagramPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
	}
	
	/**
	 * The constructor.
	 *
	 * @see XToolsUIPlugin#XToolsUIPlugin()
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
	public void doStartup() {
		super.doStartup();
		
		MSLAdapterFactoryManager.register(new NotationItemProviderAdapterFactory());
		MSLMetaModelManager.register(NotationPackage.eINSTANCE, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return DiagramResourceManager.getInstance();
	}
}
