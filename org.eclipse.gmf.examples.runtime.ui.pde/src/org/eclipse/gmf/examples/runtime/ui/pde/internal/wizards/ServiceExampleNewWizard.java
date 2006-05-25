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

package org.eclipse.gmf.examples.runtime.ui.pde.internal.wizards;

import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n.ResourceManager;


/**
 * Service Example wizard page.
 * 
 * @author jcorchis
 *
 */
public class ServiceExampleNewWizard extends ProjectUnzipperNewWizard {
	
	
	/**
	 * Wizard page title
	 */
	private static final String WIZARD_CREATEPROJECTPAGE_TITLE = ResourceManager
		.getI18NString("ServiceExample.wizard.createProjectPage.title"); //$NON-NLS-1$

	/**
	 * Wizard page description
	 */
	private static final String WIZARD_CREATEPROJECTPAGE_DESC = ResourceManager
		.getI18NString("ServiceExample.wizard.createProjectPage.desc"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public ServiceExampleNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			WIZARD_CREATEPROJECTPAGE_TITLE,
			WIZARD_CREATEPROJECTPAGE_DESC,
            "org.eclipse.gmf.examples.runtime.common.service", //$NON-NLS-1$ 
            GmfExamplesPlugin
				.getDefault().find(new Path("examples/serviceExample.zip"))); //$NON-NLS-1$
	}	

}
