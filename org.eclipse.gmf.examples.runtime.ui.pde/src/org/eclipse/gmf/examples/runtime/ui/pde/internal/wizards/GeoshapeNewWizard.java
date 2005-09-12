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


package org.eclipse.gmf.examples.runtime.ui.pde.internal.wizards;

import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n.ResourceManager;

/**
 * This wizard generates the Geoshapes plug-in sample.
 */
public class GeoshapeNewWizard
	extends ProjectUnzipperNewWizard {

	/** Wizard page title. */
	private static final String GEOSHAPES_WIZARD_CREATEPROJECTPAGE_TITLE = ResourceManager
		.getI18NString("Geoshapes.wizard.createProjectPage.title"); //$NON-NLS-1$

	/** Wizard page description. */
	private static final String GEOSHAPES_WIZARD_CREATEPROJECTPAGE_DESC = ResourceManager
		.getI18NString("Geoshapes.wizard.createProjectPage.desc"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public GeoshapeNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			GEOSHAPES_WIZARD_CREATEPROJECTPAGE_TITLE,
			GEOSHAPES_WIZARD_CREATEPROJECTPAGE_DESC, GmfExamplesPlugin
				.getDefault().find(new Path("examples/geoshapes.zip"))); //$NON-NLS-1$
	}
}
