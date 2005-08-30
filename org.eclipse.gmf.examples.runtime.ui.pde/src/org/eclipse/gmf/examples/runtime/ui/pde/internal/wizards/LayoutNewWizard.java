/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
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
 * This wizard generates the Layout provider plug-in sample.
 */
public class LayoutNewWizard
	extends ProjectUnzipperNewWizard {

	/** Wizard page title. */
	private static final String LAYOUTSERVICE_WIZARD_CREATEPROJECTPAGE_TITLE = ResourceManager
		.getI18NString("LayoutService.wizard.createProjectPage.title"); //$NON-NLS-1$

	/** Wizard page description. */
	private static final String LAYOUTSERVICE_WIZARD_CREATEPROJECTPAGE_DESC = ResourceManager
		.getI18NString("LayoutService.wizard.createProjectPage.desc"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public LayoutNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			LAYOUTSERVICE_WIZARD_CREATEPROJECTPAGE_TITLE,
			LAYOUTSERVICE_WIZARD_CREATEPROJECTPAGE_DESC, GmfExamplesPlugin
				.getDefault().find(new Path("examples/servicesLayout.zip"))); //$NON-NLS-1$
	}
}
