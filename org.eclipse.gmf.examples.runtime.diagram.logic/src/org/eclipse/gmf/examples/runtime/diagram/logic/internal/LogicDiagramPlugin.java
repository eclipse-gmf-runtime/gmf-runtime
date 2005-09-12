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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal;

import org.eclipse.core.runtime.IPluginDescriptor;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.LogicResourceManager;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.provider.SemanticItemProviderAdapterFactory;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLMetaModelManager;

/**
 * Logic Diagram Plug-in
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicDiagramPlugin
	extends XToolsUIPlugin {

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
	 *
	 * @see org.eclipse.core.runtime.Plugin#Plugin(IPluginDescriptor)
	 */
	public LogicDiagramPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
	}
	
	/**
	 * The constructor.
	 * 
	 * @see XToolsUIPlugin#XToolsUIPlugin()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin#getResourceManager()
	 */
	public AbstractResourceManager getResourceManager() {
		return LogicResourceManager.getInstance();
	}

	/**
	 * Starts up this wizards plug-in.
	 */
	public void doStartup() {
		super.doStartup();

		MSLAdapterFactoryManager
			.register(new SemanticItemProviderAdapterFactory());
		MSLMetaModelManager.register(SemanticPackage.eINSTANCE, null);
		
		PreferencesHint.registerPreferenceStore(
			new PreferencesHint(EDITOR_ID), getPreferenceStore());
	}
}