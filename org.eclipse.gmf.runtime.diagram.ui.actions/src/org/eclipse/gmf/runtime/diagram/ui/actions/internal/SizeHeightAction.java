/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This action is used to change the size of the selected shapes
 */
/*
 * @canBeSeenBy %level1
 */
public class SizeHeightAction extends PresentationAction {

	private static final String ACTION_LABEL   = "SameSizeAction.MakeSameSizeHeight.ActionLabelText"; //$NON-NLS-1$
	private static final String ACTION_TOOLTIP = "SameSizeAction.MakeSameSizeHeight.ActionToolTipText"; //$NON-NLS-1$

	/**
	 * Creates the Make Same Size Both Action
	 * @param workbenchPage
	 */
	public SizeHeightAction(IWorkbenchPage workbenchPage) {

		super(workbenchPage);
	}

	/**
	 * Initializes this actions text and tooltip
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#init()
	 */
	public void init() {
		super.init();
		
		setId(ActionIds.ACTION_MAKE_SAME_SIZE_HEIGHT);
		setText(Messages.getString( ACTION_LABEL ) );
		setToolTipText(Messages.getString( ACTION_TOOLTIP ) );
		setImageDescriptor(Images.DESC_ACTION_MAKE_SAME_SIZE_HEIGHT);
		setHoverImageDescriptor(Images.DESC_ACTION_MAKE_SAME_SIZE_HEIGHT);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.internal.ui.actions.PresentationAction#createTargetRequest()
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
	 * @see com.ibm.xtools.presentation.internal.ui.actions.PresentationAction#getCommand()
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
		Integer width = (Integer) ViewUtil.getPropertyValue(primaryView,Properties.ID_EXTENTX);
		Integer height = (Integer)ViewUtil.getPropertyValue(primaryView,Properties.ID_EXTENTY);

		Dimension primarySize;
		if( width.intValue() == -1 || height.intValue() == -1 )
			primarySize = primary.getFigure().getSize().getCopy();
		else
			primarySize = new Dimension(width.intValue(),height.intValue());
		
		while( iter.hasNext() ) {
			IGraphicalEditPart toResize = (IGraphicalEditPart)iter.next();
			View resizeView = (View)toResize.getModel();

			// Make a copy of the primary view so the width doesn't change
			Dimension size = primarySize.getCopy();
			size.width = ((Integer)ViewUtil.getPropertyValue(resizeView,Properties.ID_EXTENTX)).intValue();

			doResizeCmd.add( 
				new EtoolsProxyCommand(
					new SetBoundsCommand( "", new EObjectAdapter(resizeView), size ) ) ); //$NON-NLS-1$
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
