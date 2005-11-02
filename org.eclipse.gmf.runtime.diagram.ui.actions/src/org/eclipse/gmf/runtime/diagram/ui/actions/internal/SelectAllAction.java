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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramActionsResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.BorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
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
	 * This method searches an edit part for a child that is a gate edit part
	 * @param parent part needed to search
	 * @return list of gated edit parts that are direct children of the parent
	 */
	private List getGateEditParts(EditPart parent) {
		List list = new LinkedList();
		
		
		Iterator iter = parent.getChildren().iterator();
		while(iter.hasNext()) {
			EditPart child = (EditPart)iter.next();
			if( child instanceof BorderItemEditPart ) {
				list.add(child);
				list.addAll(child.getChildren());
			}
			list.addAll( getGateEditParts(child) );
		}
		
		if( list.isEmpty() )
			return Collections.EMPTY_LIST;
		
		return list;
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
			
		if (editpart instanceof DiagramEditPart
			|| editpart instanceof ShapeCompartmentEditPart
			|| editpart instanceof ListCompartmentEditPart) {
			
			List list = new LinkedList();

			Iterator iter = editpart.getChildren().iterator();
			while( iter.hasNext() ) {
				EditPart child = (EditPart)iter.next();
				list.add( child );
				if (!(editpart instanceof DiagramEditPart))
					list.addAll( getGateEditParts( child ) );
			}

			return list;
		}
		
		if (editpart instanceof ConnectionEditPart) {
			ConnectionEditPart connection = (ConnectionEditPart) editpart;
			EditPart source = connection.getSource();
			EditPart target = connection.getTarget();
			if (source != null && target != null) {
				List list = new ArrayList();
				list.addAll(getSelectableNodes(source));
				if (target.getParent() != source.getParent())
					list.addAll(getSelectableNodes(target));
				return list;
			}
		}

		return getSelectableNodes(editpart.getParent());
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
		if (diagramEditPart != null) {
			Iterator connections = diagramEditPart.getConnections().iterator();
			while (connections.hasNext()) {
				ConnectionEditPart connection =
					(ConnectionEditPart) connections.next();
				if (editparts.contains(connection.getSource())
					|| editparts.contains(connection.getTarget()))
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectAll")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectAll")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTALL));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTALL_DISABLED));
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectAll")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectAll")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTALL));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTALL_DISABLED));
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectShapes")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectShapes")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTSHAPES));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTSHAPES_DISABLED));
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectShapes")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectShapes")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTSHAPES));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTSHAPES_DISABLED));
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectConnections")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.SelectConnections")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTCONNECTIONS));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTCONNECTIONS_DISABLED));
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
		action.setText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectConnections")); //$NON-NLS-1$
		action.setToolTipText(DiagramActionsResourceManager.getI18NString("SelectAllAction.toolbar.SelectConnections")); //$NON-NLS-1$
		action.setImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTCONNECTIONS));
		action.setDisabledImageDescriptor(DiagramActionsResourceManager.getInstance().getImageDescriptor(DiagramActionsResourceManager.IMAGE_SELECTCONNECTIONS_DISABLED));
		return action;
	}

}
