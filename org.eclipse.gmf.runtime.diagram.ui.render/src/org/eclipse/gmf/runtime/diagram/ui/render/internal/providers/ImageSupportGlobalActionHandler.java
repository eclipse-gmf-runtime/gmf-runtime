/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.providers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.commands.CopyImageCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.PasteViewRequest;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Concrete class that implements the <code>IGlobalActionHandler</code>
 * interface and provides a command for <code>GlobalActionId.CUT</code>,
 * <code>GlobalActionId.COPY</code>, and <code>GlobalActionId.PASTE</code>.
 * 
 * @author cmahoney
 */
public class ImageSupportGlobalActionHandler
	extends DiagramGlobalActionHandler {

	/**
	 * Constructor for CopyWithImageSupportGlobalActionHandler.
	 */
	public ImageSupportGlobalActionHandler() {
		super();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractGlobalActionHandler#canHandle(IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext cntxt) {

		/* Check if the active part is a IDiagramWorkbenchPart */
		IWorkbenchPart part = cntxt.getActivePart();
		if (!(part instanceof IDiagramWorkbenchPart)) {
			return false;
		}

		/* Check if the selection is a structured selection */
		if (!(cntxt.getSelection() instanceof IStructuredSelection)) {
			return false;
		}

		/* Check the action id */
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.COPY)) {
			return canCopy(cntxt);
		} else if (actionId.equals(GlobalActionId.CUT)) {
			return canCut(cntxt);
		} else if (actionId.equals(GlobalActionId.PASTE)) {
			return canPaste(cntxt);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#canPaste(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canPaste(IGlobalActionContext cntxt) {
		if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
			return super.canPaste(cntxt);
		}
		
		/* Check if the clipboard has data for the drawing surface */
		return AWTClipboardHelper.getInstance().hasCustomData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#getCopyCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext,
	 *      org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart,
	 *      boolean)
	 */
	protected ICommand getCopyCommand(IGlobalActionContext cntxt,
			IDiagramWorkbenchPart diagramPart, final boolean isUndoable) {
		if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
			return super.getCopyCommand(cntxt, diagramPart, isUndoable);
		}
		
		return new CopyImageCommand(cntxt.getLabel(), diagramPart.getDiagram(),
			getSelectedViews(cntxt.getSelection()), diagramPart
				.getDiagramEditPart()) {

			public boolean canUndo() {
				return isUndoable;
			}

			public boolean canRedo() {
				return isUndoable;
			}

			protected CommandResult doUndoWithResult(
                    IProgressMonitor progressMonitor, IAdaptable info)
                throws ExecutionException {
                
				return isUndoable ? CommandResult.newOKCommandResult()
					: super.doUndoWithResult(progressMonitor, info);
			}

			protected CommandResult doRedoWithResult(
                    IProgressMonitor progressMonitor, IAdaptable info)
                throws ExecutionException {
                
				return isUndoable ? CommandResult.newOKCommandResult()
					: super.doRedoWithResult(progressMonitor, info);
			}
            
            public void addContext(IUndoContext context) {
                if (isUndoable) {
                    super.addContext(context);
                }
            }

            public void removeContext(IUndoContext context) {
               if (isUndoable) {
                   super.removeContext(context);
               }
            }
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#createPasteViewRequest()
	 */
	protected PasteViewRequest createPasteViewRequest() {
		if (!AWTClipboardHelper.getInstance().isImageCopySupported()) {
			return super.createPasteViewRequest();
		}
		
		CustomData data = AWTClipboardHelper.getInstance().getCustomData();

		return new PasteViewRequest(new ICustomData[] {data});
	}
}
