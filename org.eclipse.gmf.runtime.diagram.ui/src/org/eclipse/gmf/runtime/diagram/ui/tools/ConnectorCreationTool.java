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

package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.util.SelectInDiagramHelper;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

/**
 * Generic Connector Creation Tool - creates a semantic model element and a view for it.
 * 
 * Supports creation of a connector when there is not information is not all available
 * even after ther user has selected the target. When the user clicks on the
 * target element, then they will be presented with a diaglog to permit them to
 * enter the additional information.
 * 
 * @author melaasar
 *  * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool}
 */
public class ConnectorCreationTool
	extends org.eclipse.gef.tools.ConnectionCreationTool {

	// temporarily disable the autoexpose helper since it interferes with menu selection.
	// see defect RATLC00525995	
	private boolean antiScroll = false;
	
	/** @deprecated the requested element kind */
	private IElementType elementType = null;

	/** Should deactivation be avoided? */
	private boolean avoidDeactivation = false;
	
	/** Does the user have the ctrl key pressed? */
	private boolean isCtrlKeyDown;
	
	static private String CONNECTORCURSOR_MASK = "elcl16/connectcursor_mask.bmp";//$NON-NLS-1$
	static private String CONNECTORCURSOR_SOURCE = "elcl16/connectcursor_source.bmp";//$NON-NLS-1$	
	
	static private String NOCONNECTORCURSOR_MASK = "dlcl16/noconnectcursor_mask.bmp";//$NON-NLS-1$
	static private String NOCONNECTORCURSOR_SOURCE = "dlcl16/noconnectcursor_source.bmp";//$NON-NLS-1$	

	static private Cursor CURSOR_CONNECTOR = new Cursor(Display.getDefault(), DiagramResourceManager
		.getInstance().getImageDescriptor(CONNECTORCURSOR_SOURCE).getImageData(),
		DiagramResourceManager.getInstance().getImageDescriptor(
			CONNECTORCURSOR_MASK).getImageData(), 0, 0);

	static private Cursor CURSOR_CONNECTOR_NOT = new Cursor(Display.getDefault(), DiagramResourceManager
		.getInstance().getImageDescriptor(NOCONNECTORCURSOR_SOURCE).getImageData(),
		DiagramResourceManager.getInstance().getImageDescriptor(
			NOCONNECTORCURSOR_MASK).getImageData(), 0, 0);		

	/**
	 * Creates a new ConnectionCreationTool, the elementTypeInfo and viewFactoryhint will
	 * be set later.
	 */
	public ConnectorCreationTool() {
		setUnloadWhenFinished(true);
		setDefaultCursor(CURSOR_CONNECTOR);
		setDisabledCursor(CURSOR_CONNECTOR_NOT);		
	}

	/**
	 * Method CreationTool.
	 * Creates a new CreationTool with the given elementType
	 * @param elementType
	 */
	public ConnectorCreationTool(IElementType elementType) {
		this();
		setSemanticRequestType(elementType);
	}
	
	/**
	 * @return Returns the elementType.
	 */
	protected IElementType getElementType() {
		return elementType;
	}
		

	/**
	 * Sets the elementType.
	 * @param elementType The elementType to set
	 */
	protected void setSemanticRequestType(IElementType elementType) {
		this.elementType = elementType;
	}
	
	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		EditPartViewer viewer = getCurrentViewer();
		if (viewer != null) {
			RootEditPart rootEP = viewer.getRootEditPart();
			if (rootEP instanceof IDiagramPreferenceSupport) {
				return ((IDiagramPreferenceSupport) rootEP)
					.getPreferencesHint();
			}
		}
		return PreferencesHint.USE_DEFAULTS;
	}

	/**
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return CreateViewRequestFactory
			.getCreateConnectionRequest(getElementType(), getPreferencesHint());
	}

	/**
	 * Since both the view and semantic requests contain results we need to free
	 * them when the tool is deactivated
	 * 
	 * @see org.eclipse.gef.Tool#deactivate()
	 */
	public void deactivate() {

		if (!avoidDeactivation()) {
			super.deactivate();
			setTargetRequest(null);
		}

	}
	
	/**
	 * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#eraseSourceFeedback()
	 */
	protected void eraseSourceFeedback() {
		if (!avoidDeactivation()) {
			super.eraseSourceFeedback();
		}
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	protected boolean handleButtonUp(int button) {
		setCtrlKeyDown(getCurrentInput().isControlKeyDown());

		if (isInState(STATE_CONNECTION_STARTED))
			handleCreateConnection();
		setState(STATE_TERMINAL);

		if (isInState(STATE_TERMINAL | STATE_INVALID)) {
			handleFinished();
		}

		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
	 * Called when the current tool operation is complete.
	 */
	protected void handleFinished() {
		if (!isCtrlKeyDown()) {
			super.handleFinished();
		} else {
			reactivate();
		}
	}

	/**
	 * Method selectAddedObject.
	 * Select the newly added connector
	 */
	private void selectAddedObject(EditPartViewer viewer, Collection objects) {
		final List editparts = new ArrayList();
		for (Iterator i = objects.iterator(); i.hasNext();) {
			Object object = i.next();
			if (object instanceof IAdaptable) {
				Object editPart =
					viewer.getEditPartRegistry().get(
						((IAdaptable)object).getAdapter(View.class));
				if ( editPart instanceof AbstractConnectionEditPart ) {
					editparts.add( editPart );
				}
//				if (editPart != null)
//					editparts.add(editPart);
			}
		}

		if (!editparts.isEmpty()) {
			viewer.setSelection(new StructuredSelection(editparts));
		
			// automatically put the first shape into edit-mode
			Display.getCurrent().asyncExec(new Runnable() {
				public void run(){
					EditPart editPart = (EditPart) editparts.get(0);
					//
					// add active test since test scripts are failing on this
					// basically, the editpart has been deleted when this 
					// code is being executed. (see RATLC00527114)
					if ( editPart.isActive() ) {
						editPart.performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT));
					}
				}
			});
		}
	}

	/**
		*  Handles double click to create the shape in defualt position
		* @see org.eclipse.gef.tools.AbstractTool#handleDoubleClick(int)
		*/
	protected boolean handleDoubleClick(int button) {
		createConnector();
		return true;

	}

	/**
	 * Creates a connector between the two select shapes.
	 * edit parts.  
	 */
	protected void createConnector() {

		
		List selectedEditParts = getCurrentViewer().getSelectedEditParts();

		
		// only attempt to create connector if there are two shapes selected
		if ( !selectedEditParts.isEmpty() ) {

			IGraphicalEditPart sourceEditPart =
				(IGraphicalEditPart) selectedEditParts.get(0);
			
			IGraphicalEditPart targetEditPart = selectedEditParts.size() == 2
				? (IGraphicalEditPart) selectedEditParts.get(1)
				: sourceEditPart;

			CreateConnectionViewRequest connectorRequest =
				(CreateConnectionViewRequest) createTargetRequest();

			connectorRequest.setTargetEditPart(sourceEditPart);
			connectorRequest.setType(RequestConstants.REQ_CONNECTION_START);
			connectorRequest.setLocation(new Point(0, 0));

			// only if the connector is supported will we get a non null
			// command from the sourceEditPart
			if (sourceEditPart.getCommand(connectorRequest) != null) {

				connectorRequest.setSourceEditPart(sourceEditPart);
				connectorRequest.setTargetEditPart(targetEditPart);
				connectorRequest.setType(RequestConstants.REQ_CONNECTION_END);
				connectorRequest.setLocation(new Point(0, 0));

				Command command = targetEditPart.getCommand(connectorRequest);

				if (command != null) {
					setCurrentCommand(command);
					antiScroll = true;
					executeCurrentCommand();
					antiScroll = false;
					selectAddedObject(getCurrentViewer(), DiagramCommandStack.getReturnValues(command));
				}
			}
			deactivate();

		}

	}

	/**  
	 * Overide to handle use case when the user has selected a tool
	 * and then click on the enter key which translated to SWT.Selection
	 * it will result in the new shape being created
	 * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
	 */
	protected boolean handleKeyUp(KeyEvent e) {
		if (e.keyCode == SWT.Selection) {
			setEditDomain(getCurrentViewer().getEditDomain());
			createConnector();
			return true;
		}
		return false;
	}

	
	/**
	* Overridden so that the tool doesn't get deactivated and the feedback
	* erased if popup dialogs appear to complete the command.
	* @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleCreateConnection()
	*/
	protected boolean handleCreateConnection() {
		
		
		// When a connection is to be created, a dialog box may appear which
		// will cause this tool to be deactivated and the feedback to be 
		// erased. This behavior is overridden by setting the avoid 
		// deactivation flag.
		setAvoidDeactivation(true);

		EditPartViewer viewer = getCurrentViewer();
		Command endCommand = getCommand();
		setCurrentCommand(endCommand);
		
		antiScroll =  true;
		executeCurrentCommand();
		antiScroll = false;
		
		
		selectAddedObject(viewer, DiagramCommandStack.getReturnValues(endCommand));
		
		setAvoidDeactivation(false);
		eraseSourceFeedback();
		deactivate();

		return true;
	}
	
	/**
	 * Should deactivation be avoided?
	 * @return  true if deactivation is to be avoided
	 */
	protected boolean avoidDeactivation() {
		return avoidDeactivation;
	}

	/**
	 * Sets if deactivation be temporarily avoided.
	 * @param avoidDeactivation true if deactivation is to be avoided
	 */
	protected void setAvoidDeactivation(boolean avoidDeactivation) {
		this.avoidDeactivation = avoidDeactivation;
	}

	/**
	 * @return Returns the isCtrlKeyDown.
	 */
	protected boolean isCtrlKeyDown() {
		return isCtrlKeyDown;
	}
	/**
	 * @param isCtrlKeyDown The isCtrlKeyDown to set.
	 */
	protected void setCtrlKeyDown(boolean isCtrlKeyDown) {
		this.isCtrlKeyDown = isCtrlKeyDown;
	}
	
		
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.CreationTool#handleMove()
	 */
	protected boolean handleMove() {
		if (!antiScroll){
			boolean bool = super.handleMove();
			boolean cont = getState() == STATE_CONNECTION_STARTED &&
										 ((getCommand() == null)||
										 ((getCommand() != null) && (getCommand().canExecute())));
			if (cont){
				if ((getTargetEditPart() != null)
					&& (getTargetEditPart().getViewer() instanceof ScrollingGraphicalViewer)
					&& (getTargetEditPart().getViewer().getControl() instanceof FigureCanvas)) {
					FigureCanvas figureCanvas = (FigureCanvas) ((ScrollingGraphicalViewer) getTargetEditPart()
						.getViewer()).getControl();
					Point location1 = getLocation().getCopy();
					SelectInDiagramHelper.exposeLocation(figureCanvas,location1);
					
				}
			}
			return bool;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.TargetingTool#doAutoexpose()
	 */
	protected void doAutoexpose() {
		if (!antiScroll)
			super.doAutoexpose();
		return;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.TargetingTool#getCommand()
	 */
	protected Command getCommand() {	
		if (!antiScroll)
			return super.getCommand();
		return null;
	}
	
	
}
