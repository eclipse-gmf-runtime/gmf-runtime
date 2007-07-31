/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.dialogs.CopyToImageDialog;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.DiagramUIRenderMessages;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToHTMLImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Action to copy the selected shapes in the diagram or the entire diagram to an
 * image file.
 * 
 * @author Anthony Hunter, cmahoney
 */
public class CopyToImageAction
	extends DiagramAction {

	/**
	 * the copy diagram to image file dialog used by the action.
	 */
	private CopyToImageDialog dialog = null;

	/**
	 * Constructor for CopyToImageAction.
	 * 
	 * @param page
	 *            the page of the workbench for the action
	 */
	public CopyToImageAction(IWorkbenchPage page) {
		super(page);
	}

	/**
	 * Initialize with the correct text label, action id, and images.
	 */
	public void init() {
		super.init();

		/* set the label for the action */
		setText(DiagramUIRenderMessages.CopyToImageAction_Label);

		/* set the id */
		setId(ActionIds.ACTION_COPY_TO_IMAGE);

		/* set the image */
		ISharedImages sharedImages = PlatformUI.getWorkbench()
			.getSharedImages();
		setImageDescriptor(sharedImages
			.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setHoverImageDescriptor(sharedImages
			.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages
			.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
	}

	public void run() {
		IPath path = null;
		String fileName = null;

		if (getWorkbenchPart() instanceof IEditorPart) {
			IEditorPart editor = (IEditorPart) getWorkbenchPart();

			// The editor's input may provide us with an IContainer where
			// we should store items related to it.
			IContainer container = (IContainer) editor.getEditorInput()
				.getAdapter(IContainer.class);

			// If there is a container in the workspace and it exists then
			// we will use its path to store the image.
			if (container != null && container.exists()) {
				// The path has to be an absolute filesystem path for this
				// use case rather than just the path relative to the workspace
				// root.
				path = container.getLocation();
			}

			// Otherwise, we will try to adapt the input to the IFile that
			// represents the place where the editor's input file resides.
			// We can extrapolate a destination path from this file.
			if (path == null) {
				IFile file = (IFile) editor.getEditorInput().getAdapter(
					IFile.class);

				// We can't necessarily assume that the editor input is a file.
				if (file != null) {
					path = file.getLocation().removeLastSegments(1);
					fileName = file.getLocation().removeFileExtension()
						.lastSegment();
				}
			}
		}
		dialog = new CopyToImageDialog(Display.getCurrent().getActiveShell(),
				path, fileName);
		runCopyToImageUI(dialog);
	}
	
	/**
	 * Displays the dialog and performs <code>OutOfMemoryError</code> checking
	 * 
	 * @param dialog the copy to image dialog
	 */
	private void runCopyToImageUI(CopyToImageDialog dialog) {
		if (dialog.open() == CopyToImageDialog.CANCEL) {
			return;
		}

		if (!overwriteExisting()) {
			return;
		}

		Trace
				.trace(
						DiagramUIRenderPlugin.getInstance(),
						"Copy Diagram to " + dialog.getDestination().toOSString() + " as " + dialog.getImageFormat().toString()); //$NON-NLS-1$ //$NON-NLS-2$

		final MultiStatus status = new MultiStatus(DiagramUIRenderPlugin
				.getPluginId(), DiagramUIRenderStatusCodes.OK,
				DiagramUIRenderMessages.CopyToImageAction_Label, null);

		IRunnableWithProgress runnable = createRunnable(status);

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
				Display.getCurrent().getActiveShell());
		try {
			progressMonitorDialog.run(false, true, runnable);
		} catch (InvocationTargetException e) {
			Log.warning(DiagramUIRenderPlugin.getInstance(),
					DiagramUIRenderStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getTargetException().getMessage(), e
							.getTargetException());

			if (e.getTargetException() instanceof OutOfMemoryError) {
				if (dialog.exportToHTML()) {
					openErrorDialog(DiagramUIRenderMessages.CopyToImageAction_outOfMemoryMessage);
				} else {
					if (MessageDialog
							.openQuestion(
									Display.getDefault().getActiveShell(),
									DiagramUIRenderMessages.CopyToImageOutOfMemoryDialog_title,
									DiagramUIRenderMessages.CopyToImageOutOfMemoryDialog_message)) {
						runCopyToImageUI(dialog);
					}
				}					
			} else if (e.getTargetException() instanceof SWTError) {
				/**
				 * SWT returns an out of handles error when processing large
				 * diagrams
				 */
				if (dialog.exportToHTML()) {
					openErrorDialog(DiagramUIRenderMessages.CopyToImageAction_outOfMemoryMessage);
				} else {
					if (MessageDialog
							.openQuestion(
									Display.getDefault().getActiveShell(),
									DiagramUIRenderMessages.CopyToImageOutOfMemoryDialog_title,
									DiagramUIRenderMessages.CopyToImageOutOfMemoryDialog_message)) {
						runCopyToImageUI(dialog);
					}
				}					
			} else {
				openErrorDialog(e.getTargetException().getMessage());
			}
			return;
		} catch (InterruptedException e) {
			/* the user pressed cancel */
			Log.warning(DiagramUIRenderPlugin.getInstance(),
					DiagramUIRenderStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getMessage(), e);
		}

		if (!status.isOK()) {
			openErrorDialog(status.getChildren()[0].getMessage());
		}
	}

	/**
	 * copy the selected shapes in the diagram to an image file.
	 * 
	 * @param diagramEditPart
	 *            the diagram editor
	 * @param list
	 *            list of selected shapes in the diagram
	 * @param destination
	 *            path to the new image file
	 * @param imageFormat
	 *            image format to create
	 * @return the runnable with a progress monitor
	 */
	private IRunnableWithProgress createRunnable(final MultiStatus status) {
		return new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				try {
					List editparts = getOperationSet();

					CopyToImageUtil copyToImageUtil = null;
					if (dialog.exportToHTML()) {
						copyToImageUtil = new CopyToHTMLImageUtil();
					} else {
						copyToImageUtil = new CopyToImageUtil();
					}
					if (editparts.size() == 1
						&& editparts.get(0) instanceof DiagramEditPart) {
						monitor.beginTask("", 6); //$NON-NLS-1$
						monitor.worked(1);
						monitor
							.setTaskName(NLS
								.bind(
									DiagramUIRenderMessages.CopyToImageAction_copyingDiagramToImageFileMessage,
									dialog.getDestination().toOSString()));
						copyToImageUtil.copyToImage(
							(DiagramEditPart) editparts.get(0), dialog
								.getDestination(), dialog.getImageFormat(),
							monitor);
					} else {
						monitor.beginTask("", 6); //$NON-NLS-1$
						monitor.worked(1);
						monitor
							.setTaskName(NLS
								.bind(
									DiagramUIRenderMessages.CopyToImageAction_copyingSelectedElementsToImageFileMessage,
									dialog.getDestination().toOSString()));
						copyToImageUtil.copyToImage(getDiagramEditPart(),
							editparts, dialog.getDestination(), dialog
								.getImageFormat(), monitor);
					}
				} catch (CoreException e) {
					Log.warning(DiagramUIRenderPlugin.getInstance(),
						DiagramUIRenderStatusCodes.IGNORED_EXCEPTION_WARNING, e
							.getMessage(), e);
					status.add(e.getStatus());
				} finally {
					monitor.done();
				}
			}
		};
	}

	protected boolean calculateEnabled() {
		return !getOperationSet().isEmpty();
	}

	/**
	 * display an error dialog
	 * 
	 * @param message
	 *            cause of the error
	 */
	private void openErrorDialog(String message) {
		MessageDialog
			.openError(
				Display.getCurrent().getActiveShell(),
				DiagramUIRenderMessages.CopyToImageAction_copyToImageErrorDialogTitle,
				NLS
					.bind(
						DiagramUIRenderMessages.CopyToImageAction_copyToImageErrorDialogMessage,
						message));
	}

	/**
	 * Warn the user with a question dialog if an existing file is going to be
	 * overwritten and the user has not selected overwrite existing.
	 * 
	 * @return true of it is ok to continue with the copy to image.
	 */
	private boolean overwriteExisting() {
		if (dialog.overwriteExisting()) {
			/**
			 * the user has selected to overwrite existing
			 */
			return true;
		}

		if (!dialog.getDestination().toFile().exists()) {
			/**
			 * the file does not already exist
			 */
			return true;
		}

		/**
		 * ask the user to confirm to overwrite existing file.
		 */
		return MessageDialog
			.openQuestion(
				Display.getCurrent().getActiveShell(),
				DiagramUIRenderMessages.CopyToImageAction_overwriteExistingConfirmDialogTitle,
				NLS
					.bind(
						DiagramUIRenderMessages.CopyToImageAction_overwriteExistingConfirmDialogMessage,
						dialog.getDestination().toOSString()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createOperationSet()
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();

		if (selection.size() == 1) {
			Object editpart = selection.get(0);
			if (editpart instanceof DiagramEditPart) {
				return selection;
			}
			if (editpart instanceof ISurfaceEditPart) {
				selection = ((ISurfaceEditPart) editpart).getPrimaryEditParts();
			}
		}

		// must contain at least one shape
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object editpart = iter.next();
			if (editpart instanceof ShapeEditPart) {
				return selection;
			}
		}
		return Collections.EMPTY_LIST;
	}

	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * This action is not really a <code>DiagramAction</code> as it doesn't
	 * have a request. The doRun() and calculatedEnabled() have been overwritten
	 * appropriately.
	 */
	protected Request createTargetRequest() {
		return null;
	}

	protected void doRun(IProgressMonitor progressMonitor) {
		try {
			// whatever we are copying belongs to the same editing domain as
			// the Diagram
			getDiagramEditPart().getEditingDomain().runExclusive(
				new Runnable() {

					public void run() {
						CopyToImageAction.this.run();
					}
				});
		} catch (Exception e) {
			Trace.catching(DiagramUIRenderPlugin.getInstance(),
				DiagramUIRenderDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"doRun()", //$NON-NLS-1$
				e);
		}
	}

	/**
	 * Subclasses may override to specialize the rendering to an image file.
	 * 
	 * @return the <code>CopyToImageUtil</code> class to be used.
	 */
	protected CopyToImageUtil getCopyToImageUtil() {
		return new CopyToImageUtil();
	}

}
