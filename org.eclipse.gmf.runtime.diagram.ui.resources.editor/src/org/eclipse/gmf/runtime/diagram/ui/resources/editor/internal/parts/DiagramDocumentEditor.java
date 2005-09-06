/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorWithFlyOutPalette;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.PropertiesBrowserPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentEditor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IElementStateListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry.IDocumentProviderSelector;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.palette.EditorInputPaletteContent;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.osgi.framework.Bundle;


/**
 * A Diagram Editor with optional flyout palette.
 * 
 * @author mgoyal
 *
 */
public class DiagramDocumentEditor
	extends DiagramEditorWithFlyOutPalette implements IDocumentEditor, IReusableEditor {
	private MEditingDomain fEditingDomain;
	
	/**
	 * Constructs a diagram editor with optional flyout palette.
	 * 
	 * @param domain Editing Domain for the diagram.
	 * @param hasFlyoutPalette creates a palette if true, else no palette
	 */
	public DiagramDocumentEditor(MEditingDomain domain, boolean hasFlyoutPalette) {
		super(hasFlyoutPalette);
		fEditingDomain = domain;
	}

	/**
	 * Constructs a diagram editor with optional flyout palette.
	 * This uses default editing domain.
	 * @param hasFlyoutPalette creates a palette if true, else no palette
	 */
	public DiagramDocumentEditor(boolean hasFlyoutPalette) {
		this(MEditingDomain.INSTANCE, hasFlyoutPalette);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == IPropertySheetPage.class) {
			return new PropertiesBrowserPage(this);
		}
		return super.getAdapter(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init(final IEditorSite site, final IEditorInput input)
		throws PartInitException {
		super.init(site, input);

		IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				getDocumentProvider().setProgressMonitor(monitor);
			}
		};

		try {
//			When using the progress service always a modal dialog pops up. The site should be asked for a runnable context
//			which could be the workbench window or the progress service, depending on what the site represents.
//			getSite().getWorkbenchWindow().getWorkbench().getProgressService().run(false, true, runnable);

			getSite().getWorkbenchWindow().run(false, true, runnable);

		} catch (InterruptedException x) {
			// ignore
		} catch (InvocationTargetException x) {
			Throwable t= x.getTargetException();
			if (t instanceof CoreException) {
                /*
                /* XXX: Remove unpacking of CoreException once the following bug is
                 *		fixed: https://bugs.eclipse.org/bugs/show_bug.cgi?id=81640
                 */
                CoreException e= (CoreException)t;
                IStatus status= e.getStatus();
                if (status.getException() != null)
                    throw new PartInitException(status);
               	throw new PartInitException(new Status(status.getSeverity(), status.getPlugin(), status.getCode(), status.getMessage(), t));
            }
			throw new PartInitException(new Status(IStatus.ERROR, EditorPlugin.getPluginId(), IStatus.OK, EditorMessages.Editor_error_init, t));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setSite(org.eclipse.ui.IWorkbenchPartSite)
	 */
	protected final void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		fActivationListener= new ActivationListener(site.getWorkbenchWindow().getPartService());
		fActivationListener.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		if (fActivationListener != null) {
			fActivationListener.deactivate();
			fActivationListener.dispose();
			fActivationListener= null;
		}

		if (fTitleImage != null) {
			fTitleImage.dispose();
			fTitleImage= null;
		}

		disposeDocumentProvider();
		super.setInput(null);
		
		super.dispose();
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart#getDiagram()
	 */
	public Diagram getDiagram() {
		if (getDocumentProvider() != null) {
			IDiagramDocument document = ((IDiagramDocument) getDocumentProvider()
				.getDocument(getEditorInput()));
			if (document != null)
				return document.getDiagram();
		}
		return null;
	}
	
	public IDiagramDocument getDiagramDocument() {
		return (IDiagramDocument)getDocumentProvider().getDocument(getEditorInput());
	}

	// Save Overwrite Dialog (Yes/No) true for OK and false for otherwise
	public boolean askUserSaveOverwrite() {
		String title = PresentationResourceManager.getI18NString("DiagramEditor.save.outofsync.dialog.title"); //$NON-NLS-1$
		String msg = PresentationResourceManager.getI18NString("DiagramEditor.save.outofsync.dialog.message"); //$NON-NLS-1$
		Shell shell = getSite().getShell();
		return MessageDialog.openQuestion(shell, title, msg);
	}
	
	// Save/Close Dialog
	// 0 -- Save, 1 -- Close, -1 -- Cancel
	public int askUserSaveClose() {
		String title = PresentationResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.title"); //$NON-NLS-1$
		String message = PresentationResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.message"); //$NON-NLS-1$
		String[] buttons = { PresentationResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.button.save"), //$NON-NLS-1$
			PresentationResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.button.close")}; //$NON-NLS-1$
		MessageDialog dialog =
			new MessageDialog(
				getSite().getShell(),
				title,
				null,
				message,
				MessageDialog.QUESTION,
				buttons,
				0);
		return dialog.open();
	}
	
	// Refresh Contents Dialog (Yes/No) true for OK and false for otherwise
	public boolean askUserReload() {
		String title = PresentationResourceManager.getI18NString("DiagramEditor.activated.outofsync.dialog.title"); //$NON-NLS-1$
		String msg = PresentationResourceManager.getI18NString("DiagramEditor.activated.outofsync.dialog.message"); //$NON-NLS-1$
		Shell shell = getSite().getShell();
		return MessageDialog.openQuestion(shell, title, msg);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor#configureDiagramEditDomain()
	 */
	protected void configureDiagramEditDomain() {
		super.configureDiagramEditDomain();
		DiagramEditDomain editDomain =
			(DiagramEditDomain) getDiagramEditDomain();
		editDomain.setActionManager(createActionManager());
	}
	
	/**
	 * @overridable
	 */
	protected ActionManager createActionManager() {
		return new ActionManager(createCommandManager());
	}
	
	/**
	 * @overridable
	 */
	protected CommandManager createCommandManager() {
		return CommandManager.getDefault();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorWithFlyOutPalette#getDefaultPaletteContent()
	 */
	protected Object getDefaultPaletteContent() {
		EditorInputPaletteContent defPaletteContent = null;
		if(getDiagram() != null) {
			defPaletteContent = new EditorInputPaletteContent(getEditorInput(), getDiagramDocument());
		}
		return defPaletteContent;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);
//		updateStatusLine();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDiagramEditor#getDocumentProvider()
	 */
	public IDocumentProvider getDocumentProvider() {
		return fExplicitDocumentProvider;
	}

	/**
	 * Sets this editor's document provider. This method must be
	 * called before the editor's control is created.
	 *
	 * @param provider the document provider
	 */
	protected void setDocumentProvider(IDocumentProvider provider) {
		assert provider != null;
		fExplicitDocumentProvider= provider;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramEditor#isEditable()
	 */
	public boolean isEditable() {
		IDocumentProvider provider= getDocumentProvider();
		return provider.isModifiable(getEditorInput());
	}

	/**
	 * Hook method for setting the document provider for the given input.
	 * This default implementation does nothing. Clients may
	 * reimplement.
	 *
	 * @param input the input of this editor.
	 * @since 3.0
	 */
	protected void setDocumentProvider(IEditorInput input) {
		IDocumentProvider provider = DocumentProviderRegistry.getDefault().getDocumentProvider(input, new IDocumentProviderSelector() {
			public boolean select(String documentType) {
				// Only checking of the interface name
				return documentType.equals(IDiagramDocument.class.getName());
			}
		});
		setDocumentProvider(provider);
	}

	/**
	 * If there is no explicit document provider set, the implicit one is
	 * re-initialized based on the given editor input.
	 *
	 * @param input the editor input.
	 */
	private void updateDocumentProvider(IEditorInput input) {

		IProgressMonitor rememberedProgressMonitor= null;

		IDocumentProvider provider= getDocumentProvider();
		if (provider != null) {
			provider.removeElementStateListener(fElementStateListener);
			rememberedProgressMonitor= provider.getProgressMonitor();
			provider.setProgressMonitor(null);
		}

		setDocumentProvider(input);

		provider= getDocumentProvider();
		if (provider != null) {
			provider.addElementStateListener(fElementStateListener);
			provider.setProgressMonitor(rememberedProgressMonitor);
		}
	}

	/**
	 * refreshes the editor with the given file by: 0 - resets resource
	 * modification stamp 1- Flushing the command stack 2- Clearing the
	 * graphical viewer's contents 3- Setting the new File input 4- Setting the
	 * new graphical viewer's contents 5- Clearing the graphical viewer's
	 * selection
	 * 
	 * @param file
	 *            The new file editor input
	 */
	protected void releaseInput() {
		getCommandStack().flush();
		clearGraphicalViewerContents();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	public void setInput(IEditorInput input) {

		try {
			doSetInput(input, true);
		} catch (CoreException x) {
			String title= EditorMessages.Editor_error_setinput_title;
			String msg= EditorMessages.Editor_error_setinput_message;
			Shell shell= getSite().getShell();
			ErrorDialog.openError(shell, title, msg, x.getStatus());
		}
	}
	/*
	 * @see EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	public void doSetInput(IEditorInput input, boolean releaseEditorContents) throws CoreException {
		if (input == null)

			close(isSaveOnCloseNeeded());

		else {

			IEditorInput oldInput= getEditorInput();
			if (oldInput != null) {
				getDocumentProvider().disconnect(oldInput);
				if(releaseEditorContents)
					releaseInput();
			}
			
			updateDocumentProvider(input);

			IDocumentProvider provider= getDocumentProvider();
			if (provider == null || !(provider instanceof IDiagramDocumentProvider)) {
				IStatus s= new Status(IStatus.ERROR, EditorPlugin.getPluginId(), IStatus.OK, EditorMessages.Editor_error_no_provider, null);
				throw new CoreException(s);
			}

			if(!(input instanceof MEditingDomainElement))
				input = ((IDiagramDocumentProvider)provider).createInputWithEditingDomain(input, fEditingDomain);

			provider.connect(input);

			try {
				super.setInput(input);
			} catch(Throwable e) {
				if(getDiagram() == null) {
					IStatus status = provider.getStatus(input);
					if(status != null)
						throw new CoreException(status);
					else {
						IStatus s= new Status(IStatus.ERROR, EditorPlugin.getPluginId(), IStatus.OK, EditorMessages.Editor_error_init, null);
						throw new CoreException(s);
					}
				}
			}

			initializeTitle(input);
			
			if(oldInput != null && releaseEditorContents)
				initializeGraphicalViewerContents();
		}
		/*
		 * The following bugs explain why we fire this property change:
		 * 	https://bugs.eclipse.org/bugs/show_bug.cgi?id=90283
		 * 	https://bugs.eclipse.org/bugs/show_bug.cgi?id=92049
		 * 	https://bugs.eclipse.org/bugs/show_bug.cgi?id=92286
		 */
		firePropertyChange(IEditorPart.PROP_INPUT);
	}

	public void close(final boolean save) {

		enableSanityChecking(false);

		Display display= getSite().getShell().getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				if (getGraphicalViewer() != null)
					getSite().getPage().closeEditor(DiagramDocumentEditor.this, save);
			}
		});
	}

	/**
	 * Disposes of the connection with the document provider. Subclasses
	 * may extend.
	 *
	 * @since 3.0
	 */
	protected void disposeDocumentProvider() {
		IDocumentProvider provider= getDocumentProvider();
		if (provider != null) {

			IEditorInput input= getEditorInput();
			if (input != null)
				provider.disconnect(input);

			if (fElementStateListener != null) {
				provider.removeElementStateListener(fElementStateListener);
				fElementStateListener= null;
			}

			fExplicitDocumentProvider= null;
		}
	}

	/**
	 * Returns the progress monitor related to this editor. It should not be
	 * necessary to extend this method.
	 *
	 * @return the progress monitor related to this editor
	 * @since 2.1
	 */
	protected IProgressMonitor getProgressMonitor() {

		IProgressMonitor pm= null;
		IStatusLineManager manager= getStatusLineManager();
		if (manager != null)
			pm= manager.getProgressMonitor();

		return pm != null ? pm : new NullProgressMonitor();
	}

	/**
	 * Handles an external change of the editor's input element. Subclasses may
	 * extend.
	 */
	protected void handleEditorInputChanged() {

		String title;
		String msg;
		Shell shell= getSite().getShell();

		final IDocumentProvider provider= getDocumentProvider();
		if (provider == null) {
			// fix for http://dev.eclipse.org/bugs/show_bug.cgi?id=15066
			close(false);
			return;
		}

		final IEditorInput input= getEditorInput();
		if (provider.isDeleted(input)) {

			if (isSaveAsAllowed()) {

				title= EditorMessages.Editor_error_activated_deleted_save_title;
				msg= EditorMessages.Editor_error_activated_deleted_save_message;

				String[] buttons= {
					EditorMessages.Editor_error_activated_deleted_save_button_save,
					EditorMessages.Editor_error_activated_deleted_save_button_close,
				};

				MessageDialog dialog= new MessageDialog(shell, title, null, msg, MessageDialog.QUESTION, buttons, 0);

				if (dialog.open() == 0) {
					IProgressMonitor pm= getProgressMonitor();
					performSaveAs(pm);
					if (pm.isCanceled())
						handleEditorInputChanged();
				} else {
					close(false);
				}

			} else {

				title= EditorMessages.Editor_error_activated_deleted_close_title;
				msg= EditorMessages.Editor_error_activated_deleted_close_message;
				if (MessageDialog.openConfirm(shell, title, msg))
					close(false);
			}

		} else {

			title= EditorMessages.Editor_error_activated_outofsync_title;
			msg= EditorMessages.Editor_error_activated_outofsync_message;

			if (MessageDialog.openQuestion(shell, title, msg)) {


				try {
					provider.synchronize(input);
//					if (provider instanceof IDocumentProviderExtension) {
//						IDocumentProviderExtension extension= (IDocumentProviderExtension) provider;
//						extension.synchronize(input);
//					} else {
//						setInput(input);
//					}
				} catch (CoreException x) {
					IStatus status= x.getStatus();
					if (status == null || status.getSeverity() != IStatus.CANCEL) {
						title= EditorMessages.Editor_error_refresh_outofsync_title;
						msg= EditorMessages.Editor_error_refresh_outofsync_message;
						ErrorDialog.openError(shell, title, msg, x.getStatus());
					}
				}
			}
		}
	}

	/**
	 * The <code>AbstractDiagramEditor</code> implementation of this
	 * <code>IEditorPart</code> method calls <code>performSaveAs</code>.
	 * Subclasses may reimplement.
	 */
	public void doSaveAs() {
		/*
		 * 1GEUSSR: ITPUI:ALL - User should never loose changes made in the editors.
		 * Changed Behavior to make sure that if called inside a regular save (because
		 * of deletion of input element) there is a way to report back to the caller.
		 */
		performSaveAs(getProgressMonitor());
	}

	/**
	 * Performs a save as and reports the result state back to the
	 * given progress monitor. This default implementation does nothing.
	 * Subclasses may reimplement.
	 *
	 * @param progressMonitor the progress monitor for communicating result state or <code>null</code>
	 */
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		// for subclasses
	}

	/**
	 * The <code>AbstractDiagramEditor</code> implementation of this
	 * <code>IEditorPart</code> method may be extended by subclasses.
	 *
	 * @param progressMonitor the progress monitor for communicating result state or <code>null</code>
	 */
	public void doSave(IProgressMonitor progressMonitor) {

		IDocumentProvider p= getDocumentProvider();
		if (p == null)
			return;

		if (p.isDeleted(getEditorInput())) {

			if (isSaveAsAllowed()) {

				/*
				 * 1GEUSSR: ITPUI:ALL - User should never loose changes made in the editors.
				 * Changed Behavior to make sure that if called inside a regular save (because
				 * of deletion of input element) there is a way to report back to the caller.
				 */
				performSaveAs(progressMonitor);

			} else {

				Shell shell= getSite().getShell();
				String title= EditorMessages.Editor_error_save_deleted_title;
				String msg= EditorMessages.Editor_error_save_deleted_message;
				MessageDialog.openError(shell, title, msg);
			}

		} else {
			updateState(getEditorInput());
			validateState(getEditorInput());
			performSave(false, progressMonitor);
		}
	}

	/**
	 * Enables/disables sanity checking.
	 * @param enable <code>true</code> if sanity checking should be enabled, <code>false</code> otherwise
	 * @since 2.0
	 */
	protected void enableSanityChecking(boolean enable) {
		synchronized (this) {
			fIsSanityCheckEnabled= enable;
		}
	}

	/**
	 * Checks the state of the given editor input if sanity checking is enabled.
	 * @param input the editor input whose state is to be checked
	 * @since 2.0
	 */
	protected void safelySanityCheckState(IEditorInput input) {
		boolean enabled= false;

		synchronized (this) {
			enabled= fIsSanityCheckEnabled;
		}

		if (enabled)
			sanityCheckState(input);
	}

	/**
	 * Checks the state of the given editor input.
	 * @param input the editor input whose state is to be checked
	 * @since 2.0
	 */
	protected void sanityCheckState(IEditorInput input) {

		IDocumentProvider p= getDocumentProvider();
		if (p == null)
			return;

		long stamp= p.getModificationStamp(input);
		if (stamp != fModificationStamp) {
			fModificationStamp= stamp;
			if (!p.isSynchronized(input))
				handleEditorInputChanged();
		}
		updateState(getEditorInput());
	}

	/**
	 * Enables/disables state validation.
	 * @param enable <code>true</code> if state validation should be enabled, <code>false</code> otherwise
	 * @since 2.1
	 */
	protected void enableStateValidation(boolean enable) {
		synchronized (this) {
			fIsStateValidationEnabled= enable;
		}
	}

	/**
	 * Validates the state of the given editor input. The predominate intent
	 * of this method is to take any action probably necessary to ensure that
	 * the input can persistently be changed.
	 *
	 * @param input the input to be validated
	 * @since 2.0
	 */
	protected void validateState(IEditorInput input) {

		IDocumentProvider provider= getDocumentProvider();

		try {

			provider.validateState(input, getSite().getShell());

		} catch (CoreException x) {
			IStatus status= x.getStatus();
			if (status == null || status.getSeverity() != IStatus.CANCEL) {
				Bundle bundle= Platform.getBundle(PlatformUI.PLUGIN_ID);
				ILog log= Platform.getLog(bundle);
				log.log(x.getStatus());

				Shell shell= getSite().getShell();
				String title= EditorMessages.Editor_error_validateEdit_title;
				String msg= EditorMessages.Editor_error_validateEdit_message;
				ErrorDialog.openError(shell, title, msg, x.getStatus());
			}
			return;
		}

		
		if (getDiagramEditPart() != null) {
			if(isEditable())
				getDiagramEditPart().enableEditMode();
			else
				getDiagramEditPart().disableEditMode();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramEditor#validateEditorInputState()
	 */
	public boolean validateEditorInputState() {

		boolean enabled= false;

		synchronized (this) {
			enabled= fIsStateValidationEnabled;
		}

		if (enabled) {

			GraphicalViewer viewer= getGraphicalViewer();
			if (viewer == null)
				return false;

			final IEditorInput input= getEditorInput();
			BusyIndicator.showWhile(getSite().getShell().getDisplay(), new Runnable() {
				/*
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					validateState(input);
				}
			});
			sanityCheckState(input);
			return !isEditorInputReadOnly();
		}

		return !isEditorInputReadOnly();
	}

	/**
	 * Updates the state of the given editor input such as read-only flag.
	 *
	 * @param input the input to be validated
	 * @since 2.0
	 */
	protected void updateState(IEditorInput input) {
		IDocumentProvider provider= getDocumentProvider();
		try {

//			boolean wasReadOnly= isEditorInputReadOnly();
			provider.updateStateCache(input);

			if (getDiagramEditPart() != null) {
				if(isEditable())
					getDiagramEditPart().enableEditMode();
				else
					getDiagramEditPart().disableEditMode();
			}

		} catch (CoreException x) {
			Bundle bundle= Platform.getBundle(PlatformUI.PLUGIN_ID);
			ILog log= Platform.getLog(bundle);
			log.log(x.getStatus());
		}
	}

	/**
	 * Performs the save and handles errors appropriately.
	 *
	 * @param overwrite indicates whether or not overwriting is allowed
	 * @param progressMonitor the monitor in which to run the operation
	 * @since 3.0
	 */
	protected void performSave(boolean overwrite, IProgressMonitor progressMonitor) {

		IDocumentProvider provider= getDocumentProvider();
		if (provider == null)
			return;

		try {

			provider.aboutToChange(getEditorInput());
			IEditorInput input= getEditorInput();
			provider.saveDocument(progressMonitor, input, getDocumentProvider().getDocument(input), overwrite);
			editorSaved();

		} catch (CoreException x) {
			IStatus status= x.getStatus();
			if (status == null || status.getSeverity() != IStatus.CANCEL)
				handleExceptionOnSave(x, progressMonitor);
		} finally {
			provider.changed(getEditorInput());
		}
	}

	/**
	 * The number of re-entrances into error correction code while saving.
	 * @since 2.0
	 */
	private int fErrorCorrectionOnSave;

	/**
	 * Handles the given exception. If the exception reports an out-of-sync
	 * situation, this is reported to the user. Otherwise, the exception
	 * is generically reported.
	 *
	 * @param exception the exception to handle
	 * @param progressMonitor the progress monitor
	 */
	protected void handleExceptionOnSave(CoreException exception, IProgressMonitor progressMonitor) {

		try {
			++ fErrorCorrectionOnSave;

			Shell shell= getSite().getShell();

			boolean isSynchronized= false;
			IDocumentProvider p= getDocumentProvider();

			isSynchronized= p.isSynchronized(getEditorInput());

			if (isNotSynchronizedException(exception) && fErrorCorrectionOnSave == 1 && !isSynchronized) {
				String title= EditorMessages.Editor_error_save_outofsync_title;
				String msg= EditorMessages.Editor_error_save_outofsync_message;

				if (MessageDialog.openQuestion(shell, title, msg))
					performSave(true, progressMonitor);
				else {
					/*
					 * 1GEUPKR: ITPJUI:ALL - Loosing work with simultaneous edits
					 * Set progress monitor to canceled in order to report back
					 * to enclosing operations.
					 */
					if (progressMonitor != null)
						progressMonitor.setCanceled(true);
				}
			} else {
				String title= EditorMessages.Editor_error_save_title;
				String msg= EditorMessages.Editor_error_save_message;
				ErrorDialog.openError(shell, title, msg, exception.getStatus());

				/*
				 * 1GEUPKR: ITPJUI:ALL - Loosing work with simultaneous edits
				 * Set progress monitor to canceled in order to report back
				 * to enclosing operations.
				 */
				if (progressMonitor != null)
					progressMonitor.setCanceled(true);
			}
		} finally {
			-- fErrorCorrectionOnSave;
		}
	}
	
	/**
	 * Tells whether the given core exception is exactly the
	 * exception which is thrown for a non-synchronized element.
	 * <p>
	 * XXX: After 3.1 this method must be delegated to the document provider
	 * 		see 
	 * </p>
	 * 
	 * @param ex the core exception
	 * @return <code>true</code> iff the given core exception is exactly the
	 *			exception which is thrown for a non-synchronized element
	 * @since 3.1
	 */
	private boolean isNotSynchronizedException(CoreException ex) {
		if (ex == null)
			return false;
		
		IStatus status= ex.getStatus(); 
		if (status == null || status instanceof MultiStatus)
			return false;
		
		if (status.getException() != null)
			return false;
		
		// Can't access IResourceStatus.OUT_OF_SYNC_LOCAL, using value: 274
		return status.getCode() == 274;
	}

	/**
	 * The <code>AbstractDiagramEditor</code> implementation of this
	 * <code>IEditorPart</code> method returns <code>false</code>.
	 * Subclasses may override.
	 *
	 * @return <code>false</code>
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * @see EditorPart#isDirty()
	 */
	public boolean isDirty() {
		IDocumentProvider p= getDocumentProvider();
		return p == null ? false : p.canSaveDocument(getEditorInput());
	}

	/**
	 * Performs any additional action necessary to perform after the input
	 * document's content has been replaced.
	 * <p>
	 * Clients may extended this method.
	 *
	 * @since 3.0
	 */
	protected void handleElementContentReplaced() {
		initializeGraphicalViewerContents();
	}

	/**
	 * Performs any additional action necessary to perform after the input
	 * document's content has been replaced.
	 * <p>
	 * Clients may extended this method.
	 *
	 * @since 3.0
	 */
	protected void handleElementContentAboutToBeReplaced() {
		releaseInput();
	}

	/**
	 * Returns the status line manager of this editor.
	 * @return the status line manager of this editor
	 * @since 2.0
	 */
	private IStatusLineManager getStatusLineManager() {

		IEditorActionBarContributor contributor= getEditorSite().getActionBarContributor();
		if (!(contributor instanceof EditorActionBarContributor))
			return null;

		IActionBars actionBars= ((EditorActionBarContributor) contributor).getActionBars();
		if (actionBars == null)
			return null;

		return actionBars.getStatusLineManager();
	}
	/**
	 * Hook which gets called when the editor has been saved.
	 * Subclasses may extend.
	 * @since 2.1
	 */
	protected void editorSaved() {
		// nothing to do
	}

	/*
	 * @see WorkbenchPart#firePropertyChange(int)
	 */
	protected void firePropertyChange(int property) {
		super.firePropertyChange(property);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramEditor#isEditorInputReadOnly()
	 */
	public boolean isEditorInputReadOnly() {
		IDocumentProvider provider= getDocumentProvider();
		return provider.isReadOnly(getEditorInput());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramEditor#isEditorInputModifiable()
	 */
	public boolean isEditorInputModifiable() {
		IDocumentProvider provider= getDocumentProvider();
		return provider.isModifiable(getEditorInput());
	}

	/**
	 * The editor's activation listener.
	 * @since 2.0
	 */
	private ActivationListener fActivationListener;
//	private final String fReadOnlyLabel= EditorMessages.Editor_statusline_state_readonly_label;
//	private final String fWritableLabel= EditorMessages.Editor_statusline_state_writable_label;
	/** The error message shown in the status line in case of failed information look up. */
	protected final String fErrorLabel= EditorMessages.Editor_statusline_error_label;
	/** The editor's element state listener. */
	private IElementStateListener fElementStateListener= new ElementStateListener();
	/** The editor's explicit document provider. */
	private IDocumentProvider fExplicitDocumentProvider;
	/**
	 * Indicates whether sanity checking in enabled.
	 * @since 2.0
	 */
	private boolean fIsSanityCheckEnabled= true;
	/**
	 * Indicates whether state validation is enabled.
	 * @since 2.1
	 */
	private boolean fIsStateValidationEnabled= true;
	/**
	 * Cached modification stamp of the editor's input.
	 * @since 2.0
	 */
	private long fModificationStamp= IResource.NULL_STAMP;
	/**
	 * Internal part and shell activation listener for triggering state validation.
	 * @since 2.0
	 */
	class ActivationListener implements IPartListener, IWindowListener {

		/** Cache of the active workbench part. */
		private IWorkbenchPart fActivePart;
		/** Indicates whether activation handling is currently be done. */
		private boolean fIsHandlingActivation= false;
		/**
		 * The part service.
		 * @since 3.1
		 */
		private IPartService fPartService;

		/**
		 * Creates this activation listener.
		 *
		 * @param partService the part service on which to add the part listener
		 * @since 3.1
		 */
		public ActivationListener(IPartService partService) {
			fPartService= partService;
		}
		
		/**
		 * Disposes this activation listener.
		 *
		 * @since 3.1
		 */
		public void dispose() {
			fPartService= null;
		}
		
		public void activate() {
			fPartService.addPartListener(this);
			PlatformUI.getWorkbench().addWindowListener(this);
		}
		
		public void deactivate() {
			fPartService.removePartListener(this);
			PlatformUI.getWorkbench().removeWindowListener(this);
		}

		/*
		 * @see IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partActivated(IWorkbenchPart part) {
			fActivePart= part;
			handleActivation();
		}

		/*
		 * @see IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partBroughtToTop(IWorkbenchPart part) {
			// nothing to do
		}

		/*
		 * @see IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partClosed(IWorkbenchPart part) {
			// nothing to do
		}

		/*
		 * @see IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partDeactivated(IWorkbenchPart part) {
			fActivePart= null;
		}

		/*
		 * @see IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partOpened(IWorkbenchPart part) {
			// nothing to do
		}

		/**
		 * Handles the activation triggering a element state check in the editor.
		 */
		private void handleActivation() {
			if (fIsHandlingActivation)
				return;

			if (fActivePart == DiagramDocumentEditor.this) {
				fIsHandlingActivation= true;
				try {
					safelySanityCheckState(getEditorInput());
				} finally {
					fIsHandlingActivation= false;
				}
			}
		}

		/*
		 * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
		 * @since 3.1
		 */
		public void windowActivated(IWorkbenchWindow window) {
			if (window == getEditorSite().getWorkbenchWindow()) {
				/*
				 * Workaround for problem described in
				 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11731
				 * Will be removed when SWT has solved the problem.
				 */
				window.getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						handleActivation();
					}
				});
			}
		}

		/*
		 * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
		 * @since 3.1
		 */
		public void windowDeactivated(IWorkbenchWindow window) {
			// nothing to do
		}

		/*
		 * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
		 * @since 3.1
		 */
		public void windowClosed(IWorkbenchWindow window) {
			// nothing to do
		}

		/*
		 * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
		 * @since 3.1
		 */
		public void windowOpened(IWorkbenchWindow window) {
			// nothing to do
		}
	}

	/**
	 * Internal element state listener.
	 */
	class ElementStateListener implements IElementStateListener {

		/**
		 * The display used for posting runnable into the UI thread.
		 * @since 3.0
		 */
		private Display fDisplay;

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.document.IElementStateListener#elementStateValidationChanged(java.lang.Object, boolean)
		 */
		public void elementStateValidationChanged(final Object element, final boolean isStateValidated) {
			if (element != null && element.equals(getEditorInput())) {
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);
						if (isStateValidated) {
							GraphicalViewer viewer= getGraphicalViewer();
							if (viewer != null) {
								enableStateValidation(false);
							}
						} else {
							GraphicalViewer viewer= getGraphicalViewer();
							if (viewer != null) {
								enableStateValidation(true);
							}
						}
					}
				};
				execute(r, false);
			}
		}


		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.document.IElementStateListener#elementDirtyStateChanged(java.lang.Object, boolean)
		 */
		public void elementDirtyStateChanged(Object element, boolean isDirty) {
			if (element != null && element.equals(getEditorInput())) {
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);
						firePropertyChange(PROP_DIRTY);
					}
				};
				execute(r, false);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.document.IElementStateListener#elementContentAboutToBeReplaced(java.lang.Object)
		 */
		public void elementContentAboutToBeReplaced(Object element) {
			if (element != null && element.equals(getEditorInput())) {
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);
						handleElementContentAboutToBeReplaced();
					}
				};
				execute(r, false);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.document.IElementStateListener#elementContentReplaced(java.lang.Object)
		 */
		public void elementContentReplaced(Object element) {
			if (element != null && element.equals(getEditorInput())) {
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);
						firePropertyChange(PROP_DIRTY);
						handleElementContentReplaced();
					}
				};
				execute(r, false);
			}
		}

		/*
		 * @see IElementStateListener#elementDeleted(Object)
		 */
		public void elementDeleted(Object deletedElement) {
			if (deletedElement != null && deletedElement.equals(getEditorInput())) {
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);
						close(false);
					}
				};
				execute(r, false);
			}
		}

		/*
		 * @see IElementStateListener#elementMoved(Object, Object)
		 */
		public void elementMoved(final Object originalElement, final Object movedElement) {
			if (originalElement != null && originalElement.equals(getEditorInput())) {
				final boolean doValidationAsync= Display.getCurrent() != null;
				Runnable r= new Runnable() {
					public void run() {
						enableSanityChecking(true);

						if (getGraphicalViewer() == null)
							return;

						if (!canHandleMove((IEditorInput) originalElement, (IEditorInput) movedElement)) {
							close(true);
							return;
						}

						if (movedElement == null || movedElement instanceof IEditorInput) {
							final IDocumentProvider d= getDocumentProvider();
							final Object previousContent;
							IDocument changed= null;
							IEditorInput oldInput= getEditorInput();
							if (isDirty()) {
								changed= d.getDocument(oldInput);
								if (changed != null) {
									if(changed instanceof IDiagramDocument)
										previousContent = ((IDiagramDocument)changed).detachDiagram();
									else
										previousContent= changed.getContent();
								} else
									previousContent= null;
							} else
								previousContent= null;

							try {
								doSetInput((IEditorInput) movedElement, !(changed != null));
							} catch (CoreException e) {
								String title= EditorMessages.Editor_error_setinput_title;
								String msg= EditorMessages.Editor_error_setinput_message;
								Shell shell= getSite().getShell();
								ErrorDialog.openError(shell, title, msg, e.getStatus());
							}

							if (changed != null) {
								Runnable r2= new Runnable() {
									public void run() {
										validateState(getEditorInput());
										getDocumentProvider().getDocument(getEditorInput()).setContent(previousContent);
									}
								};
								execute(r2, doValidationAsync);
							}

						}
					}
				};
				execute(r, false);
			}
		}

		/**
		 * Returns whether this editor can handle the move of the original element
		 * so that it ends up being the moved element. By default this method
		 * returns <code>true</code>. Subclasses may reimplement.
		 *
		 * @param originalElement the original element
		 * @param movedElement the moved element
		 * @return whether this editor can handle the move of the original element
		 *         so that it ends up being the moved element
		 * @since 2.0
		 */
		protected boolean canHandleMove(IEditorInput originalElement, IEditorInput movedElement) {
			return true;
		}

		/*
		 * @see IElementStateListenerExtension#elementStateChanging(Object)
		 * @since 2.0
		 */
		public void elementStateChanging(Object element) {
			if (element != null && element.equals(getEditorInput()))
				enableSanityChecking(false);
		}

		/*
		 * @see IElementStateListenerExtension#elementStateChangeFailed(Object)
		 * @since 2.0
		 */
		public void elementStateChangeFailed(Object element) {
			if (element != null && element.equals(getEditorInput()))
				enableSanityChecking(true);
		}

		/**
		 * Executes the given runnable in the UI thread.
		 * <p>
		 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=76765 for details
		 * about why the parameter <code>postAsync</code> has been
		 * introduced in the course of 3.1.
		 *
		 * @param runnable runnable to be executed
		 * @param postAsync <code>true</code> if the runnable must be posted asynchronous, <code>false</code> otherwise
		 * @since 3.0
		 */
		private void execute(Runnable runnable, boolean postAsync) {
			if (postAsync || Display.getCurrent() == null) {
				if (fDisplay == null)
					fDisplay= getSite().getShell().getDisplay();
				fDisplay.asyncExec(runnable);
			} else
				runnable.run();
		}
	}
	/** Title image to be disposed. */
	private Image fTitleImage;
	/**
	 * Initializes the editor's title based on the given editor input.
	 *
	 * @param input the editor input to be used
	 */
	private void initializeTitle(IEditorInput input) {

		Image oldImage= fTitleImage;
		fTitleImage= null;
		String title= ""; //$NON-NLS-1$

		if (input != null) {
			IEditorRegistry editorRegistry= PlatformUI.getWorkbench().getEditorRegistry();
			IEditorDescriptor editorDesc= editorRegistry.findEditor(getSite().getId());
			ImageDescriptor imageDesc= editorDesc != null ? editorDesc.getImageDescriptor() : null;

			fTitleImage= imageDesc != null ? imageDesc.createImage() : null;
			title= input.getName();
		}

		setTitleImage(fTitleImage);
		setPartName(title);

		firePropertyChange(PROP_DIRTY);

		if (oldImage != null && !oldImage.isDisposed())
			oldImage.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		IDocumentProvider provider = getDocumentProvider();
		IStatus status = provider.getStatus(getEditorInput());
		if(status != null && !status.isOK())
			throw new RuntimeException(new CoreException(status));
		super.createPartControl(parent);
	}
}
