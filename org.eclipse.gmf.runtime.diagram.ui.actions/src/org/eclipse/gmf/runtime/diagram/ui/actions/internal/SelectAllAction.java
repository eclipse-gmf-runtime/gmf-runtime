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
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.BorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;

/**
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 * 
 */
public class SelectAllAction extends DiagramAction {
	/** whether to select shapes */
	private boolean selectShapes;
	/** whether to select connections */
	private boolean selectConnections;

	/**
	 * @param partService
	 * @param selectShapes
	 * @param selectConnections
	 */
	private SelectAllAction(
		IWorkbenchPage partService,
		boolean selectShapes,
		boolean selectConnections) {
		super(partService);
		this.selectShapes = selectShapes;
		this.selectConnections = selectConnections;
	}

	protected Request createTargetRequest() {
		return null;
	}

	/**
	 * The operation set is the shapes, connections or both on the diagrm edit part
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty() || !(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;

		List selectables = new ArrayList();

		EditPart primaryEP = (EditPart) selection.get(selection.size() - 1);
		List nodeEditParts = new ArrayList();
		nodeEditParts.addAll(getSelectableNodes(primaryEP));

		if (selectShapes)
			selectables.addAll(nodeEditParts);
		if (selectConnections)
			selectables.addAll(addSelectableConnections(nodeEditParts));
		return filterEditPartsMatching(selectables, getSelectionConditional());
	}

	/**
	 * Determines the candidate list of node editparts for selection
	 * 
	 * @param editpart
	 * @return
	 */
	protected List getSelectableNodes(EditPart editpart) {
		if (editpart == null) {
			return Collections.EMPTY_LIST;
		}
			
		List retval = new ArrayList();
		getSelectableNodesInside(editpart, true, retval);
		return retval;
	}
			
	/**
	 * Determines the candidate list of node editparts for selection
	 * 
	 * @param editpart
	 * @param topLevel <code>boolean</code> is this the initial entry point into the recursive method.
	 * @param retval <code>List</code> to modify
	 */
	private void getSelectableNodesInside(EditPart editpart, boolean topLevel, List retval) {

		if ( editpart instanceof ISurfaceEditPart) {
			getSelectableChildrenNodes(editpart, retval);
			}
		else if (editpart instanceof IPrimaryEditPart) {
			if (topLevel) {
		if (editpart instanceof ConnectionEditPart) {
			ConnectionEditPart connection = (ConnectionEditPart) editpart;
			EditPart source = connection.getSource();
			EditPart target = connection.getTarget();
			if (source != null && target != null) {
						getSelectableNodesInside(source, true, retval);
				if (target.getParent() != source.getParent())
							getSelectableNodesInside(target, true, retval);
			}
		}
				else
					getSelectableNodesInside(editpart.getParent(), true, retval);
			}
			else {
				if (editpart.isSelectable())
					retval.add(editpart);
				getSelectableChildrenNodes(editpart, retval);
			}
		}
	}

	private void getSelectableChildrenNodes(EditPart editpart, List retval) {
		Iterator iter = editpart.getChildren().iterator();
		while( iter.hasNext() ) {
			EditPart child = (EditPart)iter.next();
			getSelectableNodesInside(child, false, retval);
	}
	}

	/**
	 * This method searches an edit part for a child that is a border item edit part
	 * @param parent part needed to search
	 * @param set to be modified of border item edit parts that are direct children of the parent
	 */
	private void getBorderItemEditParts(EditPart parent, Set retval) {
		
		Iterator iter = parent.getChildren().iterator();
		while(iter.hasNext()) {
			EditPart child = (EditPart)iter.next();
			if( child instanceof BorderItemEditPart ) {
				retval.add(child);
				retval.addAll(child.getChildren());
			}
			getBorderItemEditParts(child, retval);
		}
	}
	
	/**
	 * Determines the candidate list of connection edit for selection
	 * A connection is included if atleast the source or the target is
	 * included in the given list
	 * 
	 * @param editparts
	 */
	protected List addSelectableConnections(List editparts) {
		List selectableConnections = new ArrayList();
		DiagramEditPart diagramEditPart = getDiagramEditPart();
		Set connnectableEditParts = new HashSet(editparts);
		ListIterator li = editparts.listIterator();
		while (li.hasNext()) {
			getBorderItemEditParts((EditPart)li.next(), connnectableEditParts);
		}
		
		if (diagramEditPart != null) {
			Iterator connections = diagramEditPart.getConnections().iterator();
			while (connections.hasNext()) {
				ConnectionEditPart connection =
					(ConnectionEditPart) connections.next();
				if (connnectableEditParts.contains(connection.getSource())
					|| connnectableEditParts.contains(connection.getTarget()))
					selectableConnections.add(connection);
			}
		}
		return selectableConnections;
	}

	/**
	 * @return The Selection Conditional which tests if the editpart is selectable
	 */
	protected EditPartViewer.Conditional getSelectionConditional() {
		return new EditPartViewer.Conditional() {
			public boolean evaluate(EditPart editpart) {
				return editpart.isSelectable();
			}
		};
	}

	/**
	 * Returns true if the operation set is not empty and only if the diagram is selected. 
	 */
	protected boolean calculateEnabled() {
		return !getOperationSet().isEmpty();
	}

	protected void doRun(IProgressMonitor progressMonitor) {
		getDiagramGraphicalViewer().setSelection(
			new StructuredSelection(getOperationSet()));
	}

	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * Create the SelectAll action
	 * 
	 * @return The SelectAll action
	 */
	public static SelectAllAction createSelectAllAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, true, true);
		action.setId(ActionFactory.SELECT_ALL.getId());
		action.setText(DiagramUIActionsMessages.SelectAllAction_SelectAll);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_SelectAll);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTALL);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTALL_DISABLED);
		return action;
	}
	
	/**
	 * Create the SelectAll toolbar action
	 * 
	 * @return The SelectAll toobar action
	 */
	public static SelectAllAction createToolbarSelectAllAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, true, true);
		action.setId(ActionIds.ACTION_TOOLBAR_SELECT_ALL);
		action.setText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectAll);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectAll);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTALL);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTALL_DISABLED);
		return action;
	}

	/**
	 * Create the SelectAllShapes action
	 * 
	 * @return The SelectAllShapes action
	 */
	public static SelectAllAction createSelectAllShapesAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, true, false);
		action.setId(ActionIds.ACTION_SELECT_ALL_SHAPES);
		action.setText(DiagramUIActionsMessages.SelectAllAction_SelectShapes);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_SelectShapes);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTSHAPES);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTSHAPES_DISABLED);
		return action;
	}
	
	/**
	 * Create the SelectAllShapes toolbar action
	 * 
	 * @return The SelectAllShapes toolbar action
	 */
	public static SelectAllAction createToolbarSelectAllShapesAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, true, false);
		action.setId(ActionIds.ACTION_TOOLBAR_SELECT_ALL_SHAPES);
		action.setText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectShapes);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectShapes);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTSHAPES);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTSHAPES_DISABLED);
		return action;
	}

	/**
	 * Create the SelectAllConnections action
	 * 
	 * @return The SelectAllConnections action
	 */
	public static SelectAllAction createSelectAllConnectionsAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, false, true);
		action.setId(ActionIds.ACTION_SELECT_ALL_CONNECTIONS);
		action.setText(DiagramUIActionsMessages.SelectAllAction_SelectConnections);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_SelectConnections);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTCONNECTIONS);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTCONNECTIONS_DISABLED);
		return action;
	}
	
	/**
	 * Create the SelectAllConnections toolbar action
	 * 
	 * @return The SelectAllConnections toolbar action
	 */
	public static SelectAllAction createToolbarSelectAllConnectionsAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, false, true);
		action.setId(ActionIds.ACTION_TOOLBAR_SELECT_ALL_CONNECTIONS);
		action.setText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectConnections);
		action.setToolTipText(DiagramUIActionsMessages.SelectAllAction_toolbar_SelectConnections);
		action.setImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTCONNECTIONS);
		action.setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_SELECTCONNECTIONS_DISABLED);
		return action;
	}

}
