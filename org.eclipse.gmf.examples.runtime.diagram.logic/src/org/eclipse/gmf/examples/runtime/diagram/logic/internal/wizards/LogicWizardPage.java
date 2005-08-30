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

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.LogicResourceManager;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicDiagramFileCreator;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.EditorFileCreator;


/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 * Create Logic Diagram Wizard Page
 */
public class LogicWizardPage extends EditorWizardPage{

	/**
	 * LogicDiagramWizardPage constructor
	 *
	 * @param aWorkbench
	 *            workbench
	 * @param selection
	 *            selection
	 */
	public LogicWizardPage(IWorkbench aWorkbench,
			IStructuredSelection selection) {
		super("LogicDiagramPage", aWorkbench, selection); //$NON-NLS-1$
		this.setTitle(LogicResourceManager
			.getI18NString("LogicWizardPage.Title")); //$NON-NLS-1$
		this.setDescription(LogicResourceManager
			.getI18NString("LogicWizardPage.Description")); //$NON-NLS-1$
	}
	
	public IFile createAndOpenDiagram(
			IPath containerPath,
			String fileName,
			InputStream initialContents,
			String kind,
			IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor,
			boolean saveDiagram) {
		return LogicEditorUtil.createAndOpenDiagram(
				getDiagramFileCreator(),
				containerPath,
				fileName,
				initialContents,
				kind,
				dWindow,
				progressMonitor,
				isOpenNewlyCreatedDiagramEditor(),
				saveDiagram);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDefaultFileName()
	 */
	protected String getDefaultFileName() {
		return LogicResourceManager
			.getI18NString("LogicVisualizer.DefaultLogicDiagramFileName"); //$NON-NLS-1$;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDiagramFileCreator()
	 */
	public EditorFileCreator getDiagramFileCreator() {
		return LogicDiagramFileCreator.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.viz.ui.internal.wizards.DiagramWizardPage#getDiagramKind()
	 */
	protected String getDiagramKind() {
		return "logic"; //$NON-NLS-1$
	}

}
