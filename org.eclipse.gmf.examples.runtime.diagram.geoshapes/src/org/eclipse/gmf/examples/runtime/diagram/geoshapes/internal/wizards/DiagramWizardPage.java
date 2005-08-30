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
import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.EditorFileCreator;


/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 *
 * Create Diagram Wizard Page
 */
public class DiagramWizardPage extends EditorWizardPage{

	/**
	 * DiagramWizardPage constructor
	 *
	 * @param aWorkbench
	 *            workbench
	 * @param selection
	 *            selection
	 */
	public DiagramWizardPage(IWorkbench aWorkbench,
			IStructuredSelection selection) {
		super("GeoshapeDiagramPage", aWorkbench, selection); //$NON-NLS-1$
		this.setTitle(DiagramResourceManager
			.getI18NString("GeoshapeWizardPage.Title")); //$NON-NLS-1$
		this.setDescription(DiagramResourceManager
			.getI18NString("GeoshapeWizardPage.Description")); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDefaultFileName()
	 */
	protected String getDefaultFileName() {
		return DiagramResourceManager
			.getI18NString("GEOVisualizer.DefaultGeoshapeDiagramFileName"); //$NON-NLS-1$;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDiagramFileCreator()
	 */
	public EditorFileCreator getDiagramFileCreator() {
		return DiagramFileCreator.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDiagramKind()
	 */
	protected String getDiagramKind() {
		return "Geoshape"; //$NON-NLS-1$
	}

}
