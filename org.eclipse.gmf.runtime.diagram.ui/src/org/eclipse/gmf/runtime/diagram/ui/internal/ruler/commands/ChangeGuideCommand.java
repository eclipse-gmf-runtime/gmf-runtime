/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPartViewer;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramGuide;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Alignment;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command is used to change a guide's attached part
 * @author Jody Schofield
 */
public class ChangeGuideCommand extends AbstractModelCommand {

	private EditPartViewer editPartViewer;
	private IAdaptable adapterPart = null;
	private View attachedPart = null;

	private Guide   theNewGuide;
	private Alignment theNewAlign;

	private boolean horizontal;

	public ChangeGuideCommand(EditPartViewer viewer, IAdaptable part, boolean horizontalGuide) {
		super(null, null);
		editPartViewer = viewer;
		adapterPart = part;
		horizontal = horizontalGuide;
	}

	public ChangeGuideCommand(View part, boolean horizontalGuide) {
		super(null, null);
		attachedPart = part;
		horizontal = horizontalGuide;
	}

	public CommandResult doExecute(IProgressMonitor progressMonitor) {

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
		
		return newOKCommandResult();
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
