package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;


public class IDEDiagramDocumentEditor
	extends DiagramDocumentEditor {

	public IDEDiagramDocumentEditor(MEditingDomain domain, boolean hasFlyoutPalette) {
		super(domain, hasFlyoutPalette);
	}

	public IDEDiagramDocumentEditor(boolean hasFlyoutPalette) {
		super(hasFlyoutPalette);
	}

	/**
	 * Performs a save as and reports the result state back to the
	 * given progress monitor. This default implementation does nothing.
	 * Subclasses may reimplement.
	 *
	 * @param progressMonitor the progress monitor for communicating result state or <code>null</code>
	 */
	
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

		// Check if the editor is already open
		IEditorMatchingStrategy matchingStrategy = getEditorDescriptor().getEditorMatchingStrategy();
		
		IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		for (int i=0; i < editorRefs.length; i++) {
			if (matchingStrategy.matches(editorRefs[i],newInput)) {
				MessageDialog.openWarning(shell, ResourceManager.getI18NString("FileSaveAs.DialogTitle"), ResourceManager.getI18NString("FileSaveAs.DialogMessageText"));	//$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
		}
		
		
		
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
