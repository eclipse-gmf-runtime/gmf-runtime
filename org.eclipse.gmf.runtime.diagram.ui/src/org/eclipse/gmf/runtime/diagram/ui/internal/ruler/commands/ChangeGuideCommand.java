/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramGuide;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Alignment;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command is used to change a guide's attached part
 * @author Jody Schofield
 */
public class ChangeGuideCommand extends AbstractTransactionalCommand {

	private EditPartViewer editPartViewer;
	private IAdaptable adapterPart = null;
	private View attachedPart = null;

	private Guide   theNewGuide;
	private Alignment theNewAlign;

	private boolean horizontal;

    /**
     * 
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param viewer
     * @param part
     * @param horizontalGuide
     */
	public ChangeGuideCommand(TransactionalEditingDomain editingDomain, EditPartViewer viewer, IAdaptable part, boolean horizontalGuide) {
		super(editingDomain, null, null);
		editPartViewer = viewer;
		adapterPart = part;
		horizontal = horizontalGuide;
	}

    /**
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param part
     * @param horizontalGuide
     */
	public ChangeGuideCommand(TransactionalEditingDomain editingDomain, View part, boolean horizontalGuide) {
		super(editingDomain, null, null);
		attachedPart = part;
		horizontal = horizontalGuide;
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		// Detach the part from it's old guide
		Guide theOldGuide = horizontal ? DiagramGuide.getInstance().getHorizontalGuide(attachedPart) :
			DiagramGuide.getInstance().getVerticalGuide(attachedPart);

		if (theOldGuide != null) {
			theOldGuide.getNodeMap().remove(getViewNode());
		}

		// Attach the part to it's new guide
		if (theNewGuide != null) {
			theNewGuide.getNodeMap().put(getViewNode(), theNewAlign);
		}

		editPartViewer = null;
		adapterPart    = null;
		attachedPart   = null;
		theNewGuide    = null;
		theNewAlign    = null;
		
		return CommandResult.newOKCommandResult();
	}

	public void setNewGuide(Guide guide, int alignment) {
		theNewGuide = guide;
		int value = 0;
		switch( alignment ) {
			case -1:
				value = horizontal ? Alignment.TOP : Alignment.LEFT;
				break;
			case 0:
				value = Alignment.CENTER;
				break;
			case 1:
				value = horizontal ? Alignment.BOTTOM : Alignment.RIGHT;
		}
		theNewAlign = Alignment.get(value);
	}

	private Node getViewNode() {
		
		if( adapterPart != null && attachedPart != null ) {

			Map epRegistry =
				editPartViewer.getEditPartRegistry();

			IGraphicalEditPart attachedEP =
				(IGraphicalEditPart) epRegistry.get(
					adapterPart.getAdapter(View.class));
			
			attachedPart = (View)attachedEP.getModel();
		}
		
		return (Node)attachedPart;
	}
}
