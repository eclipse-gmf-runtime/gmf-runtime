/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This action is used to change the size of the selected shapes
 */
public class SizeBothAction extends DiagramAction {

	/**
	 * Creates the Make Same Size Both Action
	 * @param workbenchPage
	 */
	public SizeBothAction(IWorkbenchPage workbenchPage) {

		super(workbenchPage);
	}

	/**
	 * Initializes this actions text and tooltip
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#init()
	 */
	public void init() {
		super.init();
		
		setId(ActionIds.ACTION_MAKE_SAME_SIZE_BOTH);
		setText(DiagramUIActionsMessages.SameSizeAction_MakeSameSizeBoth_ActionLabelText);
		setToolTipText(DiagramUIActionsMessages.SameSizeAction_MakeSameSizeBoth_ActionToolTipText);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_MAKE_SAME_SIZE_BOTH);
		setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_MAKE_SAME_SIZE_BOTH);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand()
	 */
	protected Command getCommand() {
		
		// Create a compound command to hold the resize commands
		CompoundCommand doResizeCmd = new CompoundCommand();

		// Create an iterator for the selection
		Iterator iter = getSelectedObjects().iterator();
		
		// Get the Primary Selection
		int last = getSelectedObjects().size() - 1;
		IGraphicalEditPart primary = (IGraphicalEditPart)getSelectedObjects().get(last);
		View primaryView = (View)primary.getModel();
		Integer width = (Integer) ViewUtil.getStructuralFeatureValue(
			primaryView, NotationPackage.eINSTANCE.getSize_Width());
		Integer height = (Integer) ViewUtil.getStructuralFeatureValue(
			primaryView, NotationPackage.eINSTANCE.getSize_Height());
		
		Dimension primarySize;
		if( width.intValue() == -1 || height.intValue() == -1 )
			primarySize = primary.getFigure().getSize().getCopy();
		else
			primarySize = new Dimension(width.intValue(),height.intValue());
		
		while( iter.hasNext() ) {
			IGraphicalEditPart toResize = (IGraphicalEditPart)iter.next();
			View resizeView = (View)toResize.getModel();

			doResizeCmd.add( 
				new ICommandProxy(
					new SetBoundsCommand(toResize.getEditingDomain(), "", new EObjectAdapter(resizeView), primarySize ) ) ); //$NON-NLS-1$
		}

		return doResizeCmd.unwrap();
	}

	/**
	 * Action is enabled if the operation set's parent has XYLayout 
	 * and they all share the same parent
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {

		List selection = getSelectedObjects();
		
		// If the selection list must contain 2 or more
		if( selection.size() < 2 ) {
			
			// disable this action
			return false;
		}

		// Enable this action
		return true;
	}
}
