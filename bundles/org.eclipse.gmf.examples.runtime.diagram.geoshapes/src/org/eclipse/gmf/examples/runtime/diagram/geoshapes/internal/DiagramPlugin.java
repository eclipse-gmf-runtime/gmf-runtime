/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * Geoshapes Diagram Example Plug-in
 * 
 * @author qili
 */
public class DiagramPlugin extends AbstractUIPlugin {
    
    /**
     * The geo shapes diagram editor ID. This matches the id used in this
     * plugin's XML for the editor extension point.
     */
    public static final String EDITOR_ID = "GeoshapeEditor"; //$NON-NLS-1$

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

    public void start(BundleContext context)
        throws Exception {
        super.start(context);
         PreferencesHint.registerPreferenceStore(
             new PreferencesHint(EDITOR_ID), getPreferenceStore());
    }
    

}
