/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorCreationWizard;


/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 *
 * Create Diagram Wizard
 */
public class DiagramCreationWizard extends EditorCreationWizard{
	
	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		super.addPages();

		if (page == null)
			page = new DiagramWizardPage(getWorkbench(), getSelection());

		addPage(page);
	}
	
	public void init(IWorkbench workbench, IStructuredSelection sel) {
		super.init(workbench, sel);

		setWindowTitle(DiagramResourceManager
				.getI18NString("CreationWizard.New_Geoshape_Diagram")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(DiagramResourceManager.getInstance()
				.getImageDescriptor("wizards/geoshapes_wiz.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

}
