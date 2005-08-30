/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandleLocator;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectorHandle.HandleDirection;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This editpolicy is responsible for adding the connector handles to a shape.
 * 
 * @author cmahoney
 */
public class ConnectorHandleEditPolicy extends GraphicalEditPolicy {

	/** Delay in ms to wait after leaving shape before removing handles. */
	private static final int REMOVE_HANDLES_DELAY = 1000;

	/** Only keep the widget visible for this length of time.*/
	private int STAY_VISIBLE_DELAY = 2000;
	
	/**
	 * Delay in ms to wait before showing the connector handles upon entering
	 * the shape.
	 */
	private static final int ADD_HANDLES_DELAY = 200;

	/**
	 * Listens to mouse events on the owner shape and handles so that the
	 * handles can be added or removed at the appropriate time.
	 */
	private class MouseListener extends MouseMotionListener.Stub {

		public void mouseEntered(MouseEvent me) {
			setMouseLocation(me.getLocation());
		}

		public void mouseExited(MouseEvent me) {
			setMouseLocation(null);
			removeConnectorHandlesAfterDelay();
		}

		/**
		 * We the tools to dismiss after the delay period if 
		 * the mouse is inside the shape, but we do not want the tools to 
		 * dismiss if the mouse is inside the one of the tools.
		 * 
		 */
		private boolean shouldHoverActivate(Object theObj)
		{
			if(theObj instanceof ConnectorHandle)
				return true;
						
			return false;
		}
		
		/**
		 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
		 */
		public void mouseMoved(MouseEvent me) {
			setActivateOnHover(true);
			setMouseLocation(me.getLocation());

			// if the cursor is inside the handles
			// then we do not want to deactivate
			if (!shouldHoverActivate(me.getSource()))
				setActivateOnHover(false);

			addConnectorHandlesAfterDelay();
				
			super.mouseMoved(me);
		}		
	}

	/**
	 * Listens to the owner figure being moved so the handles can be 
	 * removed when this occurs.
	 */
	private class OwnerMovedListener implements FigureListener {

		/**
		 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
		 */
		public void figureMoved(IFigure source) {
			removeConnectorHandles();
		}
	}

	/**
	 * Listens to the focus events on the owner editpart so that the handles
	 * can be added when the space bar is pressed.  I tried to use 
	 * IFigure.addFocusListener(), but the figure isn't getting any focus
	 * change events when the space bar is pressed.
	 */
	private class FocusListener extends EditPartListener.Stub {
		/**
		* @see org.eclipse.gef.EditPartListener#selectedStateChanged(org.eclipse.gef.EditPart)
		*/
		public void selectedStateChanged(EditPart part) {
			if (part.hasFocus()) {
				addConnectorHandles(getHostFigure().getBounds().getRight());
			} else {
				removeConnectorHandles();
			}
		}
	}
	
	/** mouse motion listener for the owner shape and handles */
	private MouseListener mouseListener = new MouseListener();

	/** listener for owner shape movement */
	private OwnerMovedListener ownerMovedListener = new OwnerMovedListener();

	/** listener for focus change on editpart */
	private FocusListener focusListener = new FocusListener();

	/** list of connector handles currently being displayed */
	private List handles = null;

	private boolean myActivateOnHover = true;
	
	/** the <code>Runnable</code> used for the remove handles delay timer */
	private Runnable removeHandlesRunnable = new Runnable() {

		/**
		 * The connector handles are removed when this task is run, assuming the
		 * mouse is still outside the shape.
		 */
		public void run() {
			if (getMouseLocation() == null || !getActivateOnHover()) {
				removeConnectorHandles();
			}
		}
	};
	
	/**
	 * The current mouse location within the shape used to determine where the
	 * connector handles should be displayed. If this is null, then we can
	 * assume the user has moved the mouse outside the shape or connector
	 * handles.
	 */
	private Point mouseLocation;

	/** the <code>Runnable</code> class used for the add handles delay timer */
	private class AddHandlesRunnable
		implements Runnable {

		/** the mouse location when the timer was started */
		private Point originalMouseLocation;

		/**
		 * @param originalMouseLocation
		 */
		protected AddHandlesRunnable(Point originalMouseLocation) {
			this.originalMouseLocation = originalMouseLocation;
		}

		/**
		 * The connector handles are added when this task is run, assuming the
		 * mouse is still at the same spot where it was when the timer was
		 * started (i.e. only add the connector handles when the user stops
		 * moving the mouse).
		 */
		public void run() {
			if (originalMouseLocation.equals(getMouseLocation())) {
				if (handles != null && !getActivateOnHover()) {
					removeConnectorHandles();
				}
				addConnectorHandles(originalMouseLocation);
			}
		}
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

		((IGraphicalEditPart) getHost()).getFigure().addMouseMotionListener(
			mouseListener);
		((IGraphicalEditPart) getHost()).getFigure().addFigureListener(
			ownerMovedListener);
		((IGraphicalEditPart) getHost()).addEditPartListener(focusListener);
	}

	public void deactivate() {
		((IGraphicalEditPart) getHost()).getFigure().removeMouseMotionListener(
			mouseListener);
		((IGraphicalEditPart) getHost()).getFigure().removeFigureListener(
			ownerMovedListener);
		((IGraphicalEditPart) getHost()).removeEditPartListener(focusListener);
		
		removeConnectorHandles();

		super.deactivate();
	}

	/**
	 * Creates the handles and adds them to the handle layer if the
	 * handles have not already been created and all the conditions
	 * are met.
	 * @param referencePoint the point inside the shape where the handles
	 * should be placed near.
	 */
	protected void addConnectorHandles(Point referencePoint) {
		if (!(isOkayToShowConnectorHandles()))
			return;

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

			handle.addMouseMotionListener(mouseListener);
			layer.add(handle);

			// Register this figure with it's host editpart so mouse events
			// will be propagated to it's host.
			getHost().getViewer().getVisualPartMap().put(handle, getHost());
		}
		
		if(!getActivateOnHover())
		{
			// dismiss the handles after STAY_VISIBLE_DELAY ms.
			removeConnectorHandlesAfterDelayAmt(STAY_VISIBLE_DELAY);
		}
	}

	/**
	 * Is the show connector handles preference turned on?
	 * @return true iff the show connector handles preference is turned on
	 */
	private boolean isShowHandlesPreferenceOn() {
		IPreferenceStore preferenceStore = (IPreferenceStore)
			((IGraphicalEditPart) getHost())
				.getDiagramPreferencesHint().getPreferenceStore();
		return preferenceStore.getBoolean(
			IPreferenceConstants.PREF_SHOW_CONNECTOR_HANDLES);
	}

	/**
	 * Is the semantic reference (if applicable) is unresolvable?
	 * 
	 * @return true iff the semantic reference (if applicable) is unresolvable
	 */
	private boolean isSemanticReferenceUnresolvable() {
		final View view = (View)getHost().getModel();
		if (view.getElement() != null) {
			Boolean retval = (Boolean) MEditingDomainGetter.getMEditingDomain(view).runAsRead(new MRunnable() {

				public Object run() {
					return new Boolean(ViewUtil.resolveSemanticElement(view) == null);
				}
			});
			return retval.booleanValue();
		}
		return false;
	}

	/**
	 * Sets a timer to remove the connector handles if the handles are currently
	 * shown.
	 */
	private void removeConnectorHandlesAfterDelay() {
		removeConnectorHandlesAfterDelayAmt(REMOVE_HANDLES_DELAY);
		
		
	}
	
	private void removeConnectorHandlesAfterDelayAmt(int theDelay) {
		if (handles != null) {
			Display.getCurrent().timerExec(theDelay, removeHandlesRunnable);
		}
	}

	/**
	 * Sets a timer to add the connector handles if the mouse is still in the
	 * shape
	 */
	private void addConnectorHandlesAfterDelay() {
		Display.getCurrent().timerExec(ADD_HANDLES_DELAY,
			new AddHandlesRunnable(getMouseLocation()));
	}
	
	/**
	 * Removes the connector handles.
	 */
	private void removeConnectorHandles() {
		if (handles == null) {
			return;
		}
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			IFigure handle = (IFigure) iter.next();
			handle.removeMouseMotionListener(mouseListener);
			layer.remove(handle);
			getHost().getViewer().getVisualPartMap().remove(handle);
		}
		handles = null;
	}
		
	private void setActivateOnHover(boolean bVal)
	{
		myActivateOnHover=bVal;
	}
	private boolean getActivateOnHover()
	{
		return myActivateOnHover;
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
	 */
	protected boolean isOkayToShowConnectorHandles(){
		if (handles != null || !getHost().isActive() || 
				!isShowHandlesPreferenceOn() || 
				isSemanticReferenceUnresolvable() ||
				!isSelectionToolActive() ||
				!isEditable())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * get the connector handle locator using the host and the passed reference point
	 * @param referencePoint
	 * @return <code>ConnectorHandleLocator</code>
	 */
	protected ConnectorHandleLocator getConnectorHandleLocator(Point referencePoint){
		return new ConnectorHandleLocator(getHostFigure(), referencePoint);		
	}
	
	/**
	 * @return Returns the mouseLocation.
	 */
	protected Point getMouseLocation() {
		return mouseLocation;
	}
	
	/**
	 * @param mouseLocation The mouseLocation to set.
	 */
	protected void setMouseLocation(Point mouseLocation) {
		this.mouseLocation = mouseLocation;
	}
}
