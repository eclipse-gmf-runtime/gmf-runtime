/******************************************************************************
 * Copyright (c) 2002, 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.preferences.AppearancePreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.ConnectionsPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramsPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.PrintingPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.RulerGridPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The DiagramUI  plugin defines all the artifacts needed for the
 * visualization of modelling diagrams
 * 
 * @author melaasar
 */
public class DiagramUIPlugin
	extends AbstractUIPlugin {

	/**
	 * the plugin singleton
	 */
	private static Plugin singleton;

	/**
	 * gets the plugin singleton
	 * 
	 * @return the plugin singleton
	 */
	public static DiagramUIPlugin getInstance() {
		return (DiagramUIPlugin) singleton;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string and is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getInstance().getBundle().getSymbolicName();
	}

	/**
	 * Creates the Diagram plugin instance
	 * 
	 * @see org.eclipse.core.runtime.Plugin#Plugin()
	 */
	public DiagramUIPlugin() {
		super();
		if (singleton == null) {
			singleton = this;
		}
	}

	public void start(BundleContext context)
	throws Exception {
		super.start(context);
		
		initializeDefaultDiagramPreferenceStore();
		
		DiagramEventBroker.registerDiagramEventBrokerFactory(new DiagramEventBroker.DiagramEventBrokerFactory() {
        	public DiagramEventBroker createDiagramEventBroker(TransactionalEditingDomain editingDomain) {
        		return new DiagramEventBrokerThreadSafe(editingDomain);
        	}
        });
	}

	/**
	 * Initializes the preference store to be used as default values when a
	 * diagram editor does not have any preferences hooked up. This is the
	 * preference store registered with {@link PreferencesHint#USE_DEFAULTS}.
	 */
	private void initializeDefaultDiagramPreferenceStore() {
		IPreferenceStore defaultStore = new PreferenceStore();

		DiagramsPreferencePage.initDefaults(defaultStore);
		RulerGridPreferencePage.initDefaults(defaultStore);
		AppearancePreferencePage.initDefaults(defaultStore);
		ConnectionsPreferencePage.initDefaults(defaultStore);
		PrintingPreferencePage.initDefaults(defaultStore);

		PreferencesHint.registerPreferenceStore(
			PreferencesHint.USE_DEFAULTS, defaultStore);
	}
}