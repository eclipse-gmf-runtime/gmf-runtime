/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Bundle of all images used by this plugin. Image descriptors can be retrieved
 * by referencing the public image descriptor variable directly.
 * 
 * @author cmahoney
 */
public class ExampleDiagramLogicPluginImages {

	// Prefixes
	private static final String PREFIX_WIZARD = "icons/wizards/"; //$NON-NLS-1$

	// Image descriptors.
	public static final ImageDescriptor DESC_LOGIC_WIZARD = create(PREFIX_WIZARD
		+ "logic_wiz.gif"); //$NON-NLS-1$

	/**
	 * Creates the image descriptor from the filename given.
	 * 
	 * @param imageName
	 *            the full filename of the image
	 * @return the new image descriptor
	 */
	private static ImageDescriptor create(String imageName) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(LogicDiagramPlugin
			.getPluginId(), imageName);
	}
}
