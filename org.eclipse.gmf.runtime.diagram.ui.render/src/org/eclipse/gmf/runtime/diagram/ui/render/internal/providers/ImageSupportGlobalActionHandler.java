/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
import org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler;
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
	extends PresentationGlobalActionHandler {

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
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler#canCopy(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canCopy(IGlobalActionContext cntxt) {
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& super.canCopy(cntxt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler#canCut(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canCut(IGlobalActionContext cntxt) {
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& super.canCut(cntxt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler#canPaste(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected boolean canPaste(IGlobalActionContext cntxt) {
		/* Check if the clipboard has data for the drawing surface */
		return AWTClipboardHelper.getInstance().isImageCopySupported()
			&& AWTClipboardHelper.getInstance().hasCustomData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler#getCopyCommand(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext,
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.providers.PresentationGlobalActionHandler#createPasteViewRequest()
	 */
	protected PasteViewRequest createPasteViewRequest() {
		CustomData data = AWTClipboardHelper.getInstance().getCustomData();

		return new PasteViewRequest(new ICustomData[] {data});
	}
}