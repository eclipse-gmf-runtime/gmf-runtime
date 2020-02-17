/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.DiagramEventBrokerThreadSafe;
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
		
		PreferencesHint.registerPreferenceStore(PreferencesHint.USE_DEFAULTS,
				new PreferenceStore());
		
		DiagramEventBroker.registerDiagramEventBrokerFactory(new DiagramEventBroker.DiagramEventBrokerFactory() {
        	public DiagramEventBroker createDiagramEventBroker(TransactionalEditingDomain editingDomain) {
        		return new DiagramEventBrokerThreadSafe(editingDomain);
        	}
        });
	}

}