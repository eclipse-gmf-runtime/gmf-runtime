/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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
