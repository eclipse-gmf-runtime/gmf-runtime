/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.LogicResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorCreationWizard;


/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 * Create Logic Diagram Wizard
 */
public class LogicCreationWizard extends EditorCreationWizard{
	
	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		super.addPages();

		if (page == null)
			page = new LogicWizardPage(getWorkbench(), getSelection());

		addPage(page);
	}
	
	public void init(IWorkbench workbench, IStructuredSelection sel) {
		super.init(workbench, sel);

		setWindowTitle(LogicResourceManager
				.getI18NString("CreationWizard.New_Logic_Diagram")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(LogicResourceManager.getInstance()
				.getImageDescriptor("wizards/logic_wiz.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

}
