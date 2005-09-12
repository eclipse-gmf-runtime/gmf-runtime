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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GateEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;

/**
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 * 
 */
public class SelectAllAction extends PresentationAction {
	/** whether to select shapes */
	private boolean selectShapes;
	/** whether to select connectors */
	private boolean selectConnectors;

	/**
	 * @param partService
	 * @param selectShapes
	 * @param selectConnectors
	 */
	private SelectAllAction(
		IWorkbenchPage partService,
		boolean selectShapes,
		boolean selectConnectors) {
		super(partService);
		this.selectShapes = selectShapes;
		this.selectConnectors = selectConnectors;
	}

	/**
	 * @see com.ibm.xtools.presentation.internal.ui.actions.PresentationAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return null;
	}

	/**
	 * The operation set is the shapes, connectors or both on the diagrm edit part
	 * 
	 * @see com.ibm.xtools.presentation.internal.ui.actions.PresentationAction#createOperationSet()
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
		if (selectConnectors)
			selectables.addAll(addSelectableConnectors(nodeEditParts));
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
			if( child instanceof GateEditPart ) {
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
	 * Determines the candidate list of connector edit for selection
	 * A connector is included if atleast the source or the target is
	 * included in the given list
	 * 
	 * @param editparts
	 */
	protected List addSelectableConnectors(List editparts) {
		List selectableConnectors = new ArrayList();
		DiagramEditPart diagramEditPart = getDiagramEditPart();
		if (diagramEditPart != null) {
			Iterator connectors = diagramEditPart.getConnectors().iterator();
			while (connectors.hasNext()) {
				ConnectionEditPart connection =
					(ConnectionEditPart) connectors.next();
				if (editparts.contains(connection.getSource())
					|| editparts.contains(connection.getTarget()))
					selectableConnectors.add(connection);
			}
		}
		return selectableConnectors;
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
	 * @see com.ibm.xtools.presentation.internal.ui.actions.PresentationAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return !getOperationSet().isEmpty();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		getDiagramGraphicalViewer().setSelection(
			new StructuredSelection(getOperationSet()));
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
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
		action.setText(Messages.getString("SelectAllAction.SelectAll")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.SelectAll")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTALL);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTALL_DISABLED);
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
		action.setText(Messages.getString("SelectAllAction.toolbar.SelectAll")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.toolbar.SelectAll")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTALL);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTALL_DISABLED);
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
		action.setText(Messages.getString("SelectAllAction.SelectShapes")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.SelectShapes")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTSHAPES);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTSHAPES_DISABLED);
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
		action.setText(Messages.getString("SelectAllAction.toolbar.SelectShapes")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.toolbar.SelectShapes")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTSHAPES);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTSHAPES_DISABLED);
		return action;
	}

	/**
	 * Create the SelectAllConnectors action
	 * 
	 * @return The SelectAllConnectors action
	 */
	public static SelectAllAction createSelectAllConnectorsAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, false, true);
		action.setId(ActionIds.ACTION_SELECT_ALL_CONNECTORS);
		action.setText(Messages.getString("SelectAllAction.SelectConnectors")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.SelectConnectors")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTCONNECTORS);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTCONNECTORS_DISABLED);
		return action;
	}
	
	/**
	 * Create the SelectAllConnectors toolbar action
	 * 
	 * @return The SelectAllConnectors toolbar action
	 */
	public static SelectAllAction createToolbarSelectAllConnectorsAction(IWorkbenchPage partService) {
		SelectAllAction action = new SelectAllAction(partService, false, true);
		action.setId(ActionIds.ACTION_TOOLBAR_SELECT_ALL_CONNECTORS);
		action.setText(Messages.getString("SelectAllAction.toolbar.SelectConnectors")); //$NON-NLS-1$
		action.setToolTipText(Messages.getString("SelectAllAction.toolbar.SelectConnectors")); //$NON-NLS-1$
		action.setImageDescriptor(Images.DESC_ACTION_SELECTCONNECTORS);
		action.setDisabledImageDescriptor(Images.DESC_ACTION_SELECTCONNECTORS_DISABLED);
		return action;
	}

}
