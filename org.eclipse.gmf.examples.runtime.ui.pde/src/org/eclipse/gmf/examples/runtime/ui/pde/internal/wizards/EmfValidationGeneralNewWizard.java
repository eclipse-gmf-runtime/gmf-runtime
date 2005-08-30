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

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n.ResourceManager;

/**
 * This wizard generates the EMF Validation API plug-in sample.
 */
public class EmfValidationGeneralNewWizard
	extends ProjectUnzipperNewWizard {

	/**
	 * Constructor
	 */
	public EmfValidationGeneralNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			ResourceManager.getI18NString("emf.validation.general.title"), //$NON-NLS-1$
			ResourceManager.getI18NString("emf.validation.general.desc"), //$NON-NLS-1$
			new URL[] {
				GmfExamplesPlugin.getDefault().find(new Path("examples/emf-validation-general.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/rmp-library.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/rmp-library-edit.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(new Path("examples/rmp-library-editor.zip")) //$NON-NLS-1$
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
