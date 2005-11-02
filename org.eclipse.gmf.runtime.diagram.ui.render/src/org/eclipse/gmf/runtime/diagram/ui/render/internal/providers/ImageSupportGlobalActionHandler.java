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

package org.eclipse.gmf.runtime.diagram.ui.render.internal.providers;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.PasteViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.commands.CopyImageCommand;

/**
 * Concrete class that implements the <code>IGlobalActionHandler</code>
 * interface and provides a command for <code>GlobalActionId.CUT</code>,
 * <code>GlobalActionId.COPY</code>, and <code>GlobalActionId.PASTE</code>.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.render.*
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#canCopy(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canCopy(IGlobalActionContext cntxt) {
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& super.canCopy(cntxt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#canCut(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canCut(IGlobalActionContext cntxt) {
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& super.canCut(cntxt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#canPaste(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canPaste(IGlobalActionContext cntxt) {
		/* Check if the clipboard has data for the drawing surface */
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& AWTClipboardHelper.getInstance().hasCustomData();
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
		return new CopyImageCommand(cntxt.getLabel(), diagramPart.getDiagram(),
			getSelectedViews(cntxt.getSelection()), diagramPart
				.getDiagramEditPart()) {

			public boolean isUndoable() {
				return isUndoable;
			}

			public boolean isRedoable() {
				return isUndoable;
			}

			protected CommandResult doUndo() {
				return isUndoable ? newOKCommandResult()
					: super.doUndo();
			}

			protected CommandResult doRedo() {
				return isUndoable ? newOKCommandResult()
					: super.doRedo();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler#createPasteViewRequest()
	 */
	protected PasteViewRequest createPasteViewRequest() {
		CustomData data = AWTClipboardHelper.getInstance().getCustomData();

		return new PasteViewRequest(new ICustomData[] {data});
	}
}