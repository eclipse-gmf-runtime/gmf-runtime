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
 * This wizard generates the EMF OCL Interpreter plug-in sample.
 */
public class EmfOclInterpreterNewWizard
	extends ProjectUnzipperNewWizard {

	/**
	 * Constructor
	 */
	public EmfOclInterpreterNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			ResourceManager.getI18NString("emf.ocl.interpreter.title"), //$NON-NLS-1$
			ResourceManager.getI18NString("emf.ocl.interpreter.desc"), //$NON-NLS-1$
			new URL[] {
				GmfExamplesPlugin.getDefault().find(new Path("examples/emf-ocl-interpreter.zip")), //$NON-NLS-1$
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
