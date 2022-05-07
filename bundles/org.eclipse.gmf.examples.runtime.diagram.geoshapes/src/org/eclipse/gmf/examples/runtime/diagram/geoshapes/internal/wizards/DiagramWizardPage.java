/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.wizards;


import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n.ExampleDiagramGeoshapeMessages;
import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.util.GeoShapeDiagramFileCreator;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;



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
		this.setTitle(ExampleDiagramGeoshapeMessages.
			GeoshapeWizardPage_Title);
		this.setDescription(ExampleDiagramGeoshapeMessages.
			GeoshapeWizardPage_Description);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDefaultFileName()
	 */
	protected String getDefaultFileName() {
		return ExampleDiagramGeoshapeMessages.GEOVisualizer_DefaultGeoshapeDiagramFileName;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDiagramFileCreator()
	 */
	public DiagramFileCreator getDiagramFileCreator() {
		return GeoShapeDiagramFileCreator.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDiagramKind()
	 */
	protected String getDiagramKind() {
		return "Geoshape"; //$NON-NLS-1$
	}

}
