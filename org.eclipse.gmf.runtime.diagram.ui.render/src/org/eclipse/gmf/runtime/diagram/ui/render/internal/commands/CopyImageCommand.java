/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.commands;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ClipboardCommand;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.DiagramUIRenderMessages;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Command for copying an image to the clipboard
 * 
 * @author sshaw
 * @canBeSeenBy %level1
 */
public class CopyImageCommand
	extends AbstractCommand {

	/**
	 * The list of <code>IView</code> used for the copy operation
	 */
	private final List source;

	private DiagramEditPart diagramEP;

	/**
	 * String constant for the clipboard format
	 */
	public static final String DRAWING_SURFACE = "Drawing Surface"; //$NON-NLS-1$

	/**
	 * The target <code>IView</code> used as a context for the clipboard
	 * operations. The cut and copy will use this to retrieve the view model.
	 * The paste will use this as the target view.
	 */
	private final View viewContext;

	/**
	 * Constructor for CopyImageCommand.
	 * 
	 * @param context
	 * @param viewContext
	 * @param source
	 */
	public CopyImageCommand(View viewContext, List source,
			DiagramEditPart diagramEP) {
		this(StringStatics.BLANK, viewContext, source, diagramEP);
	}

	/**
	 * Constructor for CopyImageCommand.
	 * 
	 * @param label
	 * @param context
	 * @param viewContext
	 * @param source
	 */
	public CopyImageCommand(String label, View viewContext, List source,
			DiagramEditPart diagramEP) {
		super(label, null);

		Assert.isNotNull(source);
		Assert.isNotNull(viewContext);

		this.source = source;
		this.viewContext = viewContext;
		this.diagramEP = diagramEP;
	}

    // Documentation copied from interface
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		/* Check if the source has elements */
		boolean imageCopyDiagram = false;
		if (getSource() == null || getSource().size() == 0) {
			imageCopyDiagram = true;
		}

		Map epRegistry = diagramEP.getViewer().getEditPartRegistry();
		ArrayList editParts = new ArrayList(getSource().size());
		ListIterator li = getSource().listIterator();
		while (li.hasNext()) {
			editParts.add(epRegistry.get(li.next()));
		}

		DiagramImageGenerator imageGenerator = new DiagramImageGenerator(
			getDiagramEditPart());

		Image image = null;
		try {
			if (imageCopyDiagram)
				image = imageGenerator.createAWTImageForDiagram();
			else
				image = imageGenerator.createAWTImageForParts(editParts);
		} catch (OutOfMemoryError error) {
			String eMsg = DiagramUIRenderMessages.CopyAction_UnableToCopyImageMessage;
			Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR, eMsg,
				error);
			MessageDialog.openInformation(null,
				DiagramUIRenderMessages.CopyAction_ErrorDialogTitle, eMsg);
		}

		/* Get the view model from the view context */
		CustomData data = null;
		if (!imageCopyDiagram) {
			/* Copy the views */
			data = (getViewContext() != null) ? new CustomData(DRAWING_SURFACE,
				ClipboardCommand.copyViewsToString(getSource()).getBytes())
				: null;
		}

		AWTClipboardHelper.getInstance().copyToClipboard(data, image);
		diagramEP = null; // we don't want this to end up on the undo stack
		return CommandResult.newOKCommandResult();
	}
    
    /**
     * @throws UnsupportedOperationException because redo not supported
     */
    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        UnsupportedOperationException uoe = new UnsupportedOperationException();
        Trace.throwing(DiagramUIRenderPlugin.getInstance(),
            DiagramUIRenderDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doRedoWithResult", uoe); //$NON-NLS-1$
        throw uoe;
    }
    
    /**
     * @throws UnsupportedOperationException
     *             undo not supported
     */
    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        UnsupportedOperationException uoe = new UnsupportedOperationException();
        Trace.throwing(DiagramUIRenderPlugin.getInstance(),
            DiagramUIRenderDebugOptions.EXCEPTIONS_THROWING, getClass(),
            "doUndoWithResult", uoe); //$NON-NLS-1$
        throw uoe;
    }

	/**
	 * @return Returns the diagramEP.
	 */
	private DiagramEditPart getDiagramEditPart() {
		return diagramEP;
	}

	/**
	 * Returns the source.
	 * 
	 * @return List
	 */
	private List getSource() {
		return source;
	}

	/**
	 * @return Returns the viewContext.
	 */
	private View getViewContext() {
		return viewContext;
	}
    
    public boolean canRedo() {
        return false;
    }
    
    public boolean canUndo() {
        return false;
    }
}