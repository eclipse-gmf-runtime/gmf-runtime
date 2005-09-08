/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.internal.commands;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.ui.util.CustomData;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ClipboardCommand;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.AWTClipboardHelper;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw
 * @canBeSeenBy %level1
 * 
 * Command for copying an image to the clipboard
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
		this(null, viewContext, source, diagramEP);
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
		super(label);

		Assert.isNotNull(source);
		Assert.isNotNull(viewContext);

		this.source = source;
		this.viewContext = viewContext;
		this.diagramEP = diagramEP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
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
			String eMsg = ResourceManager
				.getI18NString("CopyAction.UnableToCopyImageMessage");//$NON-NLS-1$
			Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR, eMsg,
				error);
			MessageDialog.openInformation(null, ResourceManager
				.getI18NString("CopyAction.ErrorDialogTitle"), //$NON-NLS-1$
				eMsg);
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
		return newOKCommandResult();
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
}