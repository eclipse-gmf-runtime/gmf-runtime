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


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards;


import org.eclipse.core.resources.IFile;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * Create Editor Diagram Wizard
 *
 * @author qili
 * 
 */
public class EditorCreationWizard
	extends BasicNewResourceWizard {

	/**
	 * the wizard page
	 */
	protected EditorWizardPage page;

	/**
	 * the diagram file
	 */
	protected IFile diagramFile;
	
	/**
	 * 
	 */
	public EditorCreationWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean retVal = page.finish();
		diagramFile = page.getDiagramFile();
		return retVal;
	}

	/**
	 * returns the diagram file 
	 * @return IFile the diagram file
	 */
	public final IFile getDiagramFile() {
		return diagramFile;
	}
}
