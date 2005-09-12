/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
