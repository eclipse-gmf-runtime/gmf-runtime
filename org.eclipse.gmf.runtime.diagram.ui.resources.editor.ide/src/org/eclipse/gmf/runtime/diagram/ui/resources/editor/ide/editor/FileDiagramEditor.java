/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * FileDiagramEditor with optional flyout palette.
 * 
 * @author mgoyal
 */
public class FileDiagramEditor
	extends DiagramDocumentEditor {

	/**
	 * Constructs a file diagram editor, with flyout palette if
	 * <code>hasFlyoutPalette</code> is true and without flyout palette
	 * if <code>hasFlyoutPalette</code> is false.
	 * 
	 * This uses the default editing domain.
	 * 
	 * @param hasFlyoutPalette true if flyoutPalette is required.
	 */
	public FileDiagramEditor(boolean hasFlyoutPalette) {
		this(MEditingDomain.INSTANCE, hasFlyoutPalette);
	}

	/**
	 * Constructs a file diagram editor, with flyout palette if
	 * <code>hasFlyoutPalette</code> is true and without flyout palette
	 * if <code>hasFlyoutPalette</code> is false.
	 * 
	 * @param domain Editing Domain to be used for create the diagram resource.
	 * @param hasFlyoutPalette true if flyoutPalette is required.
	 */
	public FileDiagramEditor(MEditingDomain domain, boolean hasFlyoutPalette) {
		super(domain, hasFlyoutPalette);
	}

	/**
	 * Constructs a file diagram editor without flyout palette and default editing domain.
	 * 
	 */
	public FileDiagramEditor() {
		this(false);
	}
	
	public void doSaveAs() {
		performSaveAs(new NullProgressMonitor());
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void performSaveAs(IProgressMonitor progressMonitor) {

		Shell shell= getSite().getShell();
		IEditorInput input = getEditorInput();

		SaveAsDialog dialog= new SaveAsDialog(shell);

		IFile original= (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
		if (original != null)
			dialog.setOriginalFile(original);

		dialog.create();

		IDocumentProvider provider= getDocumentProvider();
		if (provider == null) {
			// editor has been programmatically closed while the dialog was open
			return;
		}

		if (provider.isDeleted(input) && original != null) {
			String message= MessageFormat.format(EditorMessages.Editor_warning_save_delete, new Object[] { original.getName() });
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
		}

		if (dialog.open() == Window.CANCEL) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}

		IPath filePath= dialog.getResult();
		if (filePath == null) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return;
		}

		IWorkspaceRoot workspaceRoot= ResourcesPlugin.getWorkspace().getRoot();
		IFile file= workspaceRoot.getFile(filePath);
		final IEditorInput newInput= new FileEditorInput(file);

		boolean success= false;
		try {

			provider.aboutToChange(newInput);
			getDocumentProvider().saveDocument(progressMonitor, newInput, getDocumentProvider().getDocument(getEditorInput()), true);
			success= true;

		} catch (CoreException x) {
			IStatus status= x.getStatus();
			if (status == null || status.getSeverity() != IStatus.CANCEL)
				ErrorDialog.openError(shell, EditorMessages.Editor_error_saving_title2, EditorMessages.Editor_error_saving_message2, x.getStatus());
		} finally {
			provider.changed(newInput);
			if (success)
				setInput(newInput);
		}

		if (progressMonitor != null)
			progressMonitor.setCanceled(!success);
	}
}
