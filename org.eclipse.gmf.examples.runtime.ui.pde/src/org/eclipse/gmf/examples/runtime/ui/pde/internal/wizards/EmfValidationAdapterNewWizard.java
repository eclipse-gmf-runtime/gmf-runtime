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

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n.ResourceManager;

/**
 * This wizard generates the EMF Validation Adapter plug-in sample.
 */
public class EmfValidationAdapterNewWizard
	extends ProjectUnzipperNewWizard {

	/**
	 * Constructor
	 */
	public EmfValidationAdapterNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			ResourceManager.getI18NString("emf.validation.adapter.title"), //$NON-NLS-1$
			ResourceManager.getI18NString("emf.validation.adapter.desc"), //$NON-NLS-1$
			new URL[] {
				GmfExamplesPlugin.getDefault().find(new Path("examples/emf-validation-adapter.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/ext-library.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/ext-library-edit.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/ext-library-editor.zip")) //$NON-NLS-1$
			},
			new String[] {
				"{0}", //$NON-NLS-1$
				"org.eclipse.emf.examples.library", //$NON-NLS-1$
				"org.eclipse.emf.examples.library.edit", //$NON-NLS-1$
				"org.eclipse.emf.examples.library.editor", //$NON-NLS-1$
			}
		);
	}
}
