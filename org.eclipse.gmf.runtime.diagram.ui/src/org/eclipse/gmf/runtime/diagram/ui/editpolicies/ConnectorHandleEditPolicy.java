/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandleLocator;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle.HandleDirection;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;

/**
 * This editpolicy is responsible for adding the connector handles to a shape.
 * 
 * @author cmahoney
 */
public class ConnectorHandleEditPolicy extends DiagramAssistantEditPolicy {

	/**
	 * Listens to the owner figure being moved so the handles can be removed
	 * when this occurs.
	 */
	private class OwnerMovedListener implements FigureListener {

		/**
		 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
		 */
		public void figureMoved(IFigure source) {
			hideDiagramAssistant();
		}
	}

	/** listener for owner shape movement */
	private OwnerMovedListener ownerMovedListener = new OwnerMovedListener();

	/** list of connector handles currently being displayed */
	private List handles = null;

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#isDiagramAssistant(java.lang.Object)
	 */
	protected boolean isDiagramAssistant(Object object) {
		return object instanceof ConnectorHandle;
	}

	/**
	 * Gets the two connector handle figures to be added to this shape if they
	 * support user gestures.
	 * @return a list of <code>ConnectorHandle</code> objects
	 */
	protected List getHandleFigures() {
		List list = new ArrayList(2);
		
		String tooltip;
		tooltip = buildTooltip(HandleDirection.INCOMING);
		if (tooltip != null) {
			list.add(new ConnectorHandle((IGraphicalEditPart) getHost(),
				HandleDirection.INCOMING, tooltip));
		}

		tooltip = buildTooltip(HandleDirection.OUTGOING);
		if (tooltip != null) {
			list.add(new ConnectorHandle((IGraphicalEditPart) getHost(),
				HandleDirection.OUTGOING, tooltip));
		}

		return list;
	}

	/**
	 * Builds the applicable tooltip string based on whether the Modeling
	 * Assistant Service supports handle gestures on this element. If no
	 * gestures are supported, the tooltip returned will be null.
	 * 
	 * @param direction
	 *            the handle direction.
	 * @return tooltip the tooltip string; if null, the handle should be not be
	 *         displayed
	 */
	protected String buildTooltip(HandleDirection direction) {
		ModelingAssistantService service = ModelingAssistantService
			.getInstance();

		boolean supportsCreation = (direction == HandleDirection.OUTGOING) ? !service
			.getRelTypesOnSource(getHost()).isEmpty()
			: !service.getRelTypesOnTarget(getHost()).isEmpty();

		boolean supportsSRE = (direction == HandleDirection.OUTGOING) ? !service
			.getRelTypesForSREOnSource(getHost()).isEmpty()
			: !service.getRelTypesForSREOnTarget(getHost()).isEmpty();

		if (supportsSRE) {
			if (supportsCreation) {
				return PresentationResourceManager
					.getI18NString("ConnectorHandle.ToolTip.ShowRelatedElementsAndCreateRelationship"); //$NON-NLS-1$
			} else {
				return PresentationResourceManager
					.getI18NString("ConnectorHandle.ToolTip.ShowRelatedElementsOnly"); //$NON-NLS-1$
			}
		} else if (supportsCreation) {
			return PresentationResourceManager
				.getI18NString("ConnectorHandle.ToolTip.CreateRelationshipOnly"); //$NON-NLS-1$
		}
		return null;
	}

	public void activate() {
		super.activate();
		
		((IGraphicalEditPart) getHost()).getFigure().addFigureListener(
			ownerMovedListener);
	}

	public void deactivate() {
		((IGraphicalEditPart) getHost()).getFigure().removeFigureListener(
			ownerMovedListener);

		super.deactivate();
	}

	/**
	 * @deprecated use {@link #showDiagramAssistant(Point)}
	 */
	protected void addConnectorHandles(Point referencePoint) {
		showDiagramAssistant(referencePoint);
	}
	
	protected void showDiagramAssistant(Point referencePoint) {
		if (referencePoint == null) {
			referencePoint = getHostFigure().getBounds().getRight();
		}

		handles = getHandleFigures();
		if (handles == null) {
			return;
		}

		ConnectorHandleLocator locator = getConnectorHandleLocator(referencePoint);
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			ConnectorHandle handle = (ConnectorHandle) iter.next();
			
			handle.setLocator(locator);
			locator.addHandle(handle);

			handle.addMouseMotionListener(this);
			layer.add(handle);

			// Register this figure with it's host editpart so mouse events
			// will be propagated to it's host.
			getHost().getViewer().getVisualPartMap().put(handle, getHost());
		}
		
		if(!shouldAvoidHidingDiagramAssistant())
		{
			// dismiss the handles after a delay
			hideDiagramAssistantAfterDelay(getDisappearanceDelay());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#getPreferenceName()
	 */
	String getPreferenceName() {
		return IPreferenceConstants.PREF_SHOW_CONNECTOR_HANDLES;
	}

	/**
	 * Removes the connector handles.
	 */
	protected void hideDiagramAssistant() {
		if (handles == null) {
			return;
		}
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			IFigure handle = (IFigure) iter.next();
			handle.removeMouseMotionListener(this);
			layer.remove(handle);
			getHost().getViewer().getVisualPartMap().remove(handle);
		}
		handles = null;
	}
		
	private boolean isSelectionToolActive()
	{
		// getViewer calls getParent so check for null
		if(getHost().getParent() != null)
		{
			Tool theTool = getHost().getViewer().getEditDomain().getActiveTool();
			if((theTool != null) && theTool instanceof SelectionTool)
			{
				return true;
			}
		}
		return false;		
	}

	/**
	 * checks if the Host edit part is editable or not
	 * @return true or false
	 * @deprecated if someone needs this let me know -- Cherie Mahoney
	 */
	protected boolean isEditable() {
		GraphicalEditPart theEditPart = null;
		if (getHost() instanceof GraphicalEditPart) {
			theEditPart = (GraphicalEditPart) getHost();
			return theEditPart.isEditModeEnabled();
		} 
		return false;
	}
	
	/**
	 * checks if it is ok to show ConnectorHandles or not 
	 * @return true or false
	 * @deprecated use shouldShowDiagramAssistant()
	 */
	protected boolean isOkayToShowConnectorHandles(){
		return shouldShowDiagramAssistant();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#shouldShowDiagramAssistant()
	 */
	protected boolean shouldShowDiagramAssistant(){
		if (!super.shouldShowDiagramAssistant()) {
			return false;
		}
		if (handles != null || !isSelectionToolActive()) {
			return false;
		}
		return true;
	}
	
	/**
	 * get the connector handle locator using the host and the passed reference
	 * point
	 * 
	 * @param referencePoint
	 * @return <code>ConnectorHandleLocator</code>
	 */
	protected ConnectorHandleLocator getConnectorHandleLocator(Point referencePoint){
		return new ConnectorHandleLocator(getHostFigure(), referencePoint);		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramAssistantEditPolicy#isDiagramAssistantShowing()
	 */
	protected boolean isDiagramAssistantShowing() {
		return handles != null;
	}
}