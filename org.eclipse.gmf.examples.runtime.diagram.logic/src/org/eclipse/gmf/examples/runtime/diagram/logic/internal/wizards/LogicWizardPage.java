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


package org.eclipse.gmf.examples.runtime.diagram.logic.internal.wizards;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.ExampleDiagramLogicMessages;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicDiagramFileCreator;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.LogicEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;


/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 * Create Logic Diagram Wizard Page
 */
public class LogicWizardPage extends EditorWizardPage{

	private Button separateSemantics;
	private Text semanticResource;

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
		this.setTitle(ExampleDiagramLogicMessages.LogicWizardPage_Title);
		this.setDescription(ExampleDiagramLogicMessages.LogicWizardPage_Description);
	}
	
	public IFile createAndOpenDiagram(
			IPath containerPath,
			String fileName,
			InputStream initialContents,
			String kind,
			IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor,
			boolean saveDiagram) {
		
		String semanticResourcePath = null;
		
		if (separateSemantics.getSelection()
				&& semanticResource.getText().length() > 0) {
			
			semanticResourcePath = semanticResource.getText();
		}
		
		return LogicEditorUtil.createAndOpenDiagram(
				getDiagramFileCreator(),
				containerPath,
				fileName,
				initialContents,
				kind,
				dWindow,
				progressMonitor,
				isOpenNewlyCreatedDiagramEditor(),
				saveDiagram,
				semanticResourcePath);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDefaultFileName()
	 */
	protected String getDefaultFileName() {
		return ExampleDiagramLogicMessages.LogicVisualizer_DefaultLogicDiagramFileName;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDiagramFileCreator()
	 */
	public DiagramFileCreator getDiagramFileCreator() {
		return LogicDiagramFileCreator.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage#getDiagramKind()
	 */
	protected String getDiagramKind() {
		return "logic"; //$NON-NLS-1$
	}
	
	protected void createAdvancedControls(Composite parent) {
		super.createAdvancedControls(parent);
		
		separateSemantics = new Button(parent,SWT.CHECK);
		separateSemantics.setText(ExampleDiagramLogicMessages.LogicWizardPage_StoreSemanticsSeparately);
		separateSemantics.setSelection(false);
		
        Composite separateSemanticsGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        separateSemanticsGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        separateSemanticsGroup.setLayoutData(data);
		
        semanticResource = new Text(separateSemanticsGroup, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
        data.horizontalSpan = 2;
        semanticResource.setLayoutData(data);
        semanticResource.setEnabled(false);
        
        // browse button
        final Button browseButton = new Button(separateSemanticsGroup, SWT.PUSH);      
        browseButton.setText(ExampleDiagramLogicMessages.LogicWizardPage_BrowseSemanticResource);
        browseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleBrowseButtonPressed();
            }
        });
        browseButton.setEnabled(false);
        setButtonLayoutData(browseButton);
        
		separateSemantics.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				semanticResource.setEnabled(!semanticResource.getEnabled());
				browseButton.setEnabled(!browseButton.getEnabled());
				semanticResource.setText(getContainerFullPath().append(getFileName()).removeFileExtension().addFileExtension("logic2semantic").toString()); //$NON-NLS-1$
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		});
	}

	protected void handleBrowseButtonPressed() {
		ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(),ResourcesPlugin.getWorkspace().getRoot(), ExampleDiagramLogicMessages.LogicWizardPage_BrowseSemanticDialogTitle);
		
		if (dialog.open() == ResourceSelectionDialog.OK) {
			if (dialog.getResult().length == 0)
				return;
			
			IResource r = (IResource)dialog.getResult()[0];
			semanticResource.setText(r.getFullPath().toString());
		}
	}
}
