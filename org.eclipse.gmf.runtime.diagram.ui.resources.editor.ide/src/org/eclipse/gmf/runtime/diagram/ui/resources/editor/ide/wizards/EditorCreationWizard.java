/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards;


import org.eclipse.core.resources.IFile;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * Create Editor Diagram Wizard
 *
 * @author qili
 * @canBeSeenBy %level1
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
