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

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n;

import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.DiagramPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly.
 *  
 * @author cmahoney
 */
public class ExampleDiagramGeoshapePluginImages {

	// Prefixes
	private static final String PREFIX_WIZARD = "icons/wizard/"; //$NON-NLS-1$

	// Image descriptors.
	public static final ImageDescriptor DESC_GEOSHAPES_WIZARD = create(PREFIX_WIZARD
		+ "geoshapes_wiz.gif"); //$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(DiagramPlugin
			.getPluginId(), imageName);
	}
}
