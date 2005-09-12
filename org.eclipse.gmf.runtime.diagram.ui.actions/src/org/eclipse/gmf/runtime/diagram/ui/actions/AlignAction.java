/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;

/**
 * @author melaasar
 *
 * This action is cloned from the GEF AlignmentAction
 * @see org.eclipse.gef.actions.AlignmentAction
 */
public class AlignAction extends PresentationAction {

	private String id;
	private int alignment;
	private boolean isToolbarItem = true;

	/**
	 * Constructs an AlignAction with the given part and alignment ID.  The alignment ID
	 * must by one of:
	 * <UL>
	 *   <LI>GEFActionConstants.ALIGN_LEFT
	 *   <LI>GEFActionConstants.ALIGN_RIGHT
	 *   <LI>GEFActionConstants.ALIGN_CENTER
	 *   <LI>GEFActionConstants.ALIGN_TOP
	 *   <LI>GEFActionConstants.ALIGN_BOTTOM
	 *   <LI>GEFActionConstants.ALIGN_MIDDLE
	 * </UL>  
	 * @param part the workbench part used to obtain context
	 * @param id the action ID.
	 * @param align the aligment ID.
	 */
	public AlignAction(IWorkbenchPage workbenchPage, String id, int align) {
		super(workbenchPage);
		this.id = id;
		this.alignment = align;
		initUI();
	}
	
/**
	 * Constructs an AlignAction with the given part and alignment ID.  The alignment ID
	 * must by one of:
	 * <UL>
	 *   <LI>GEFActionConstants.ALIGN_LEFT
	 *   <LI>GEFActionConstants.ALIGN_RIGHT
	 *   <LI>GEFActionConstants.ALIGN_CENTER
	 *   <LI>GEFActionConstants.ALIGN_TOP
	 *   <LI>GEFActionConstants.ALIGN_BOTTOM
	 *   <LI>GEFActionConstants.ALIGN_MIDDLE
	 * </UL>  
	 * @param part the workbench part used to obtain context
	 * @param id the action ID.
	 * @param align the aligment ID.
	 * @param isToolbarItem the indicator of whether or not this is a toolbar action
	 * -as opposed to a context-menu action.
	 */	
	public AlignAction(IWorkbenchPage workbenchPage, String id, int align, boolean isToolbarItem) {
		super(workbenchPage);
		this.id = id;
		this.alignment = align;
		this.isToolbarItem = isToolbarItem;
		initUI();
	}

	/**
	 * Initializes the actions UI presentation.
	 * Two sets of each align action are required.  One for the toolbar, 
	 * and one for other menus.  The toolbar action has explicit text, 
	 * the other menus do not.  For example: Align Left and Left.
	 * 
	 */
	protected void initUI() {
		
		setId(this.id);
		String text = null;
		String toolTipText = null;
		
		switch (alignment) {
			case PositionConstants.LEFT: {
				
				if (isToolbarItem){
					text = Messages.getString("AlignLeftToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignLeftToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignLeft");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignLeft");//$NON-NLS-1$
				}				
				setHoverImageDescriptor(InternalImages.DESC_HORZ_ALIGN_LEFT);
				setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_LEFT_DIS);				
				setDisabledImageDescriptor(InternalImages.DESC_HORZ_ALIGN_LEFT_DIS);
				break;
			}
			case PositionConstants.RIGHT: {
				if (isToolbarItem){
					text = Messages.getString("AlignRightToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignRightToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignRight");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignRight");//$NON-NLS-1$
				}					
				setHoverImageDescriptor(InternalImages.DESC_HORZ_ALIGN_RIGHT);
				setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_RIGHT_DIS);
				setDisabledImageDescriptor(InternalImages.DESC_HORZ_ALIGN_RIGHT_DIS);
				break;
			}
			case PositionConstants.TOP: {
				if (isToolbarItem){
					text = Messages.getString("AlignTopToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignTopToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignTop");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignTop");//$NON-NLS-1$
				}				
				setHoverImageDescriptor(InternalImages.DESC_VERT_ALIGN_TOP);
				setImageDescriptor(InternalImages.DESC_VERT_ALIGN_TOP_DIS);
				setDisabledImageDescriptor(InternalImages.DESC_VERT_ALIGN_TOP_DIS);
				break;
			}
			case PositionConstants.BOTTOM: {
				if (isToolbarItem){
					text = Messages.getString("AlignBottomToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignBottomToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignBottom");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignBottom");//$NON-NLS-1$
				}		
				setHoverImageDescriptor(InternalImages.DESC_VERT_ALIGN_BOTTOM);
				setImageDescriptor(InternalImages.DESC_VERT_ALIGN_BOTTOM_DIS);
				setDisabledImageDescriptor(InternalImages.DESC_VERT_ALIGN_BOTTOM_DIS);
				break;
			}
			case PositionConstants.CENTER: {
				if (isToolbarItem){
					text = Messages.getString("AlignCenterToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignCenterToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignCenter");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignCenter");//$NON-NLS-1$
				}	
				setHoverImageDescriptor(InternalImages.DESC_HORZ_ALIGN_CENTER);
				setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_CENTER_DIS);
				setDisabledImageDescriptor(InternalImages.DESC_HORZ_ALIGN_CENTER_DIS);
				break;
			}
			case PositionConstants.MIDDLE: {
				if (isToolbarItem){
					text = Messages.getString("AlignMiddleToolbarAction.Label");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignMiddleToolbarAction.Tooltip");//$NON-NLS-1$
				}
				else{
					text = Messages.getString("AlignMiddle");//$NON-NLS-1$
					toolTipText = Messages.getString("AlignMiddle");//$NON-NLS-1$
				}	
				setHoverImageDescriptor(InternalImages.DESC_VERT_ALIGN_MIDDLE);
				setImageDescriptor(InternalImages.DESC_VERT_ALIGN_MIDDLE_DIS);
				setDisabledImageDescriptor(InternalImages.DESC_VERT_ALIGN_MIDDLE_DIS);
				break;
			}			
		}
		setText(text);
		setToolTipText(toolTipText);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createOperationSet()
	 */
	protected List createOperationSet() {
		List editparts = super.createOperationSet();
		editparts = ToolUtilities.getSelectionWithoutDependants(editparts);
		if (editparts.size() < 2)
			return Collections.EMPTY_LIST;
		EditPart parent = ((EditPart)editparts.get(0)).getParent();
		for (int i = 1; i < editparts.size(); i++) {
			EditPart part = (EditPart)editparts.get(i);
			if (part.getParent() != parent)
				return Collections.EMPTY_LIST;
		}
		return editparts;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		AlignmentRequest request = new AlignmentRequest(RequestConstants.REQ_ALIGN);
		request.setAlignment(alignment);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		AlignmentRequest request = (AlignmentRequest) getTargetRequest();
		request.setAlignmentRectangle(calculateAlignmentRectangle());
		super.updateTargetRequest();
	}

	/**
	 * Returns the alignment rectangle to which all selected parts should be aligned.	 
	 * @return the alignment rectangle
	 */
	protected Rectangle calculateAlignmentRectangle() {
		List editparts = getOperationSet();
		if (editparts == null || editparts.isEmpty())
			return null;
		GraphicalEditPart part = (GraphicalEditPart)editparts.get(editparts.size() - 1);
		Rectangle rect = new PrecisionRectangle(part.getFigure().getBounds());
		part.getFigure().translateToAbsolute(rect);
		return rect;
	}

}
