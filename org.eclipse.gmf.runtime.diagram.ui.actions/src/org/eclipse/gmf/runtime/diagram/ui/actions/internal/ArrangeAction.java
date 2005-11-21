/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The Arrange Action: arranges a container editpart or a set of selected editparts
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 */
public class ArrangeAction extends DiagramAction {

	private boolean selectionOnly;

	/**
	 * @param workbenchPage
	 */
	protected ArrangeAction(
		IWorkbenchPage workbenchPage,
		boolean selectionOnly) {
		super(workbenchPage);
		this.selectionOnly = selectionOnly;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new ArrangeRequest(getId());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		ArrangeRequest request = (ArrangeRequest) getTargetRequest();
		request.setPartsToArrange(getOperationSet());
	}

	private boolean isArrangeAll() {
		return !selectionOnly;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand()
	 */
	protected Command getCommand() {
		if (isArrangeAll()){
			CompoundCommand arrangeCC = new CompoundCommand(getLabel());
			List elements = getOperationSet();
			for (Iterator iter = elements.iterator(); iter.hasNext();) {
				EditPart element = (EditPart) iter.next();
				arrangeCC.add(element.getCommand(getTargetRequest()));
			}
			return arrangeCC;
		}
		else if (getOperationSet().size() >= 2) {
			EditPart parent = getSelectionParent(getOperationSet());
			if (parent != null)
				return parent.getCommand(getTargetRequest());
		}
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * Action is enabled if arrange all.   
	 * If arrange selection, action is enabled if the 
	 * operation set's parent has XYLayout 
	 * and there is atleast 2 siblings to arrange
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		
		//arrange all, always enable
		if( isArrangeAll() && !getOperationSet().isEmpty()){
			return true;
		}

		//arrange selection
		if (getOperationSet().size() >= 2) {
			EditPart parentEP = getSelectionParent(getOperationSet());
			if (parentEP instanceof GraphicalEditPart) {
				GraphicalEditPart parent = (GraphicalEditPart)parentEP;
				if ((parent != null) &&(parent.getContentPane().getLayoutManager() instanceof XYLayout))
					return true;
			}
		}
		return false;
	}

	/* 
	 * The operation set is the shapes, connections or both on the diagrm edit part
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createOperationSet()
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		
		if( isArrangeAll() ) {
			if( !selection.isEmpty()){
				return getElementsToArrange(selection);
			}
			if( getDiagramEditPart() != null )				
				return createOperationSet(getDiagramEditPart().getChildren());

			return Collections.EMPTY_LIST;
		}

		if (selection.isEmpty() ||
				!(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;

		selection = ToolUtilities.getSelectionWithoutDependants(selection);
		return createOperationSet(selection);
	}

	/**
	 * getSelectionParent
	 * Utility to return the logical parent of the selection list
	 * 
	 * @param editparts List to parse for a common parent.
	 * @return EditPart that is the parent or null if a common parent doesn't exist.
	 */
	private EditPart getSelectionParent(List editparts) {
		ListIterator li = editparts.listIterator();
		while (li.hasNext()) {
			Object obj = li.next();
			if (!(obj instanceof ConnectionEditPart) && obj instanceof EditPart) {
				return ((EditPart)obj).getParent();
			}
		}
		
		return null;
	}
	
	private List createOperationSet(List editparts) {
		if (editparts == null || editparts.isEmpty())
			return Collections.EMPTY_LIST;
		EditPart parent = getSelectionParent(editparts);
		if (parent == null)
			return Collections.EMPTY_LIST;
		
		for (int i = 1; i < editparts.size(); i++) {
			EditPart part = (EditPart) editparts.get(i);
			if (part instanceof ConnectionEditPart){
				continue;
			}
			if (part.getParent() != parent)
				return Collections.EMPTY_LIST;
		}
		return editparts;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * Creates the Arrange All action
	 * @param workbenchPage
	 */
	public static ArrangeAction createArrangeAllAction(IWorkbenchPage workbenchPage) {
		ArrangeAction action = new ArrangeAction(workbenchPage,false);
		action.setId(ActionIds.ACTION_ARRANGE_ALL);
		action.setText(DiagramActionsResourceManager.getI18NString("ArrangeAction.ArrangeAll.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ArrangeAction.ArrangeAll.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_ALL);
		action.setImageDescriptor(enabledImage);
		action.setDisabledImageDescriptor(DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_ALL_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}
	
	/**
	 * Creates the Arrange All action for the toolbar menu
	 * @param workbenchPage
	 */
	public static ArrangeAction createToolbarArrangeAllAction(IWorkbenchPage workbenchPage) {
		ArrangeAction action = new ArrangeAction(workbenchPage, false);
		action.setId(ActionIds.ACTION_TOOLBAR_ARRANGE_ALL);
		action.setText(DiagramActionsResourceManager.getI18NString("ArrangeAction.toolbar.ArrangeAll.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ArrangeAction.toolbar.ArrangeAll.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_ALL);
		action.setImageDescriptor(enabledImage);
		action.setDisabledImageDescriptor(DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_ALL_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}

	/**
	 * Creates the Arrange Selection Only action
	 * @param workbenchPage
	 */
	public static ArrangeAction createArrangeSelectionAction(IWorkbenchPage workbenchPage) {
		ArrangeAction action = new ArrangeAction(workbenchPage, true);
		action.setId(ActionIds.ACTION_ARRANGE_SELECTION);
		action.setText(DiagramActionsResourceManager.getI18NString("ArrangeAction.ArrangeSelection.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ArrangeAction.ArrangeSelection.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_SELECTED);
		action.setImageDescriptor(enabledImage);
		action.setDisabledImageDescriptor(DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_SELECTED_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}
	
	/**
	 * Creates the Arrange Selection Only action for the toolbar menu
	 * @param workbenchPage
	 */
	public static ArrangeAction createToolbarArrangeSelectionAction(IWorkbenchPage workbenchPage) {
		ArrangeAction action = new ArrangeAction(workbenchPage, true);
		action.setId(ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION);
		action.setText(DiagramActionsResourceManager.getI18NString("ArrangeAction.toolbar.ArrangeSelection.ActionLabelText")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("ArrangeAction.toolbar.ArrangeSelection.ActionToolTipText")); //$NON-NLS-1$
		
		ImageDescriptor enabledImage = DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_SELECTED);
		action.setImageDescriptor(enabledImage);
		action.setDisabledImageDescriptor(DiagramActionsResourceManager
			.getInstance().getImageDescriptor(
				DiagramActionsResourceManager.IMAGE_ARRANGE_SELECTED_DISABLED));
		action.setHoverImageDescriptor(enabledImage);
		return action;
	}
	
	protected void doRun(IProgressMonitor progressMonitor) {
		super.doRun(progressMonitor);
		
		IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
		boolean animatedLayout = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANIMATED_LAYOUT);
		
		if (animatedLayout) {
			List operationSet = getOperationSet();
			if (isArrangeAll()){
				for (Iterator iter = operationSet.iterator(); iter.hasNext();) {
					IGraphicalEditPart element = (IGraphicalEditPart) iter.next();
					AnimationFigureHelper.getInstance().animate(element.getFigure());
				}
			}
			else if (operationSet != null && !operationSet.isEmpty()) {
				IGraphicalEditPart container = (IGraphicalEditPart)getSelectionParent(operationSet);
				AnimationFigureHelper.getInstance().animate(container.getFigure());
			}
		}
	}
	
	/**
	 * @param selection
	 * @return
	 */
	private List getElementsToArrange(List selection) {
		Set parentsSet = new HashSet();
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof ShapeCompartmentEditPart || element instanceof DiagramEditPart){
				parentsSet.add(element);
			} else if (element instanceof EditPart){
				EditPart gEditPart = 
					(EditPart)element;
				EditPart parentEditPart = gEditPart.getParent();
				if (parentEditPart instanceof ShapeCompartmentEditPart ||
					parentEditPart instanceof DiagramEditPart){
					if (!parentsSet.contains(parentEditPart))
						parentsSet.add(parentEditPart);
				}
			}
		}
		if (parentsSet.isEmpty())
			return Collections.EMPTY_LIST;
		List elements = new ArrayList();
		elements.addAll(parentsSet);			
		return elements;
	}

}

