/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Generic Connection Creation Tool - creates a semantic model element and a
 * view for it.
 * 
 * Supports creation of a connection when there is not information is not all
 * available even after ther user has selected the target. When the user clicks
 * on the target element, then they will be presented with a diaglog to permit
 * them to enter the additional information.
 * 
 * @author melaasar
 */
public class ConnectionCreationTool
    extends org.eclipse.gef.tools.ConnectionCreationTool {

    private IElementType elementType = null;

    /** Should deactivation be avoided? */
    private boolean avoidDeactivation = false;

    /** Does the user have the ctrl key pressed? */
    private boolean isCtrlKeyDown;

    static private Cursor CURSOR_CONNECTION = new Cursor(Display.getDefault(),
        DiagramUIPluginImages.DESC_CONNECTION_CURSOR_SOURCE.getImageData(),
        DiagramUIPluginImages.DESC_CONNECTION_CURSOR_MASK.getImageData(), 0, 0);

    static private Cursor CURSOR_CONNECTION_NOT = new Cursor(Display
        .getDefault(), DiagramUIPluginImages.DESC_NO_CONNECTION_CURSOR_SOURCE
        .getImageData(), DiagramUIPluginImages.DESC_NO_CONNECTION_CURSOR_MASK
        .getImageData(), 0, 0);
    
    static private Cursor CURSOR_TARGET_MENU = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);

    /**
     * Creates a new ConnectionCreationTool, the elementTypeInfo and
     * viewFactoryhint will be set later.
     */
    public ConnectionCreationTool() {
        setUnloadWhenFinished(true);
        setDefaultCursor(CURSOR_CONNECTION);
        setDisabledCursor(CURSOR_CONNECTION_NOT);
    }

    /**
     * Method CreationTool. Creates a new CreationTool with the given
     * elementType
     * 
     * @param elementType
     */
    public ConnectionCreationTool(IElementType elementType) {
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
     * 
     * @param elementType
     *            The elementType to set
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
        return CreateViewRequestFactory.getCreateConnectionRequest(
            getElementType(), getPreferencesHint());
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
     * @see org.eclipse.gef.tools.AbstractTool#handleFinished() Called when the
     *      current tool operation is complete.
     */
    protected void handleFinished() {
        if (!isCtrlKeyDown()) {
            super.handleFinished();
        } else {
            reactivate();
        }
    }

    /**
     * Method selectAddedObject. Select the newly added connection
     */
    protected void selectAddedObject(EditPartViewer viewer, Collection objects) {
        final List editparts = new ArrayList();
        final EditPart[] primaryEP = new EditPart[1];
        for (Iterator i = objects.iterator(); i.hasNext();) {
            Object object = i.next();
            if (object instanceof IAdaptable) {
                Object editPart = viewer.getEditPartRegistry().get(
                    ((IAdaptable) object).getAdapter(View.class));

                if (editPart instanceof IPrimaryEditPart) {
                    editparts.add(editPart);
                }
                
                // Priority is to put a shape into direct edit mode.
                if (editPart instanceof ShapeEditPart) {
                    primaryEP[0] = (ShapeEditPart) editPart;
                }
            }
        }

        if (!editparts.isEmpty()) {
            viewer.setSelection(new StructuredSelection(editparts));

            // automatically put the first shape into edit-mode
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    if (primaryEP[0] == null) {
                        primaryEP[0] = (EditPart) editparts.get(0);
                    }
                    //
                    // add active test since test scripts are failing on this
                    // basically, the editpart has been deleted when this
                    // code is being executed. (see RATLC00527114)
                    if (primaryEP[0].isActive()) {
                        primaryEP[0].performRequest(new Request(
                            RequestConstants.REQ_DIRECT_EDIT));
                    }
                }
            });
        }
    }

    /**
     * Handles double click to create the shape in defualt position
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleDoubleClick(int)
     */
    protected boolean handleDoubleClick(int button) {
        createConnection();
        return true;

    }

    /**
     * Creates a connection between the two select shapes. edit parts.
     */
    protected void createConnection() {

        List selectedEditParts = getCurrentViewer().getSelectedEditParts();

        // only attempt to create connection if there are two shapes selected
        if (!selectedEditParts.isEmpty()) {

            IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) selectedEditParts
                .get(0);

            IGraphicalEditPart targetEditPart = selectedEditParts.size() == 2 ? (IGraphicalEditPart) selectedEditParts
                .get(1)
                : sourceEditPart;

            CreateConnectionRequest connectionRequest = (CreateConnectionRequest) createTargetRequest();

            connectionRequest.setTargetEditPart(sourceEditPart);
            connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
            connectionRequest.setLocation(new Point(0, 0));

            // only if the connection is supported will we get a non null
            // command from the sourceEditPart
            if (sourceEditPart.getCommand(connectionRequest) != null) {

                connectionRequest.setSourceEditPart(sourceEditPart);
                connectionRequest.setTargetEditPart(targetEditPart);
                connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
                connectionRequest.setLocation(new Point(0, 0));

                Command command = targetEditPart.getCommand(connectionRequest);

                if (command != null) {
                    setCurrentCommand(command);
                    executeCurrentCommand();
                    selectAddedObject(getCurrentViewer(), DiagramCommandStack
                        .getReturnValues(command));
                }
            }
            deactivate();

        }

    }

    /**
     * Overide to handle use case when the user has selected a tool and then
     * click on the enter key which translated to SWT.Selection it will result
     * in the new shape being created
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    protected boolean handleKeyUp(KeyEvent e) {
        if (e.keyCode == SWT.Selection) {
            setEditDomain(getCurrentViewer().getEditDomain());
            createConnection();
            return true;
        }
        return false;
    }

    /**
     * Overridden so that the tool doesn't get deactivated and the feedback
     * erased if popup dialogs appear to complete the command.
     * 
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

        executeCurrentCommand();

        selectAddedObject(viewer, DiagramCommandStack
            .getReturnValues(endCommand));

        setAvoidDeactivation(false);
        eraseSourceFeedback();
        deactivate();

        return true;
    }

    /**
     * Should deactivation be avoided?
     * 
     * @return true if deactivation is to be avoided
     */
    protected boolean avoidDeactivation() {
        return avoidDeactivation;
    }

    /**
     * Sets if deactivation be temporarily avoided.
     * 
     * @param avoidDeactivation
     *            true if deactivation is to be avoided
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
     * @param isCtrlKeyDown
     *            The isCtrlKeyDown to set.
     */
    protected void setCtrlKeyDown(boolean isCtrlKeyDown) {
        this.isCtrlKeyDown = isCtrlKeyDown;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.CreationTool#handleMove()
     */
    protected boolean handleMove() {
        boolean bool = super.handleMove();
        if (isInState(STATE_CONNECTION_STARTED)) {
            // Expose the diagram as the user scrolls in the area handled by the
            // autoexpose helper.
            updateAutoexposeHelper();
        }
        return bool;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#calculateCursor()
     */
    protected Cursor calculateCursor() {
        if (isInState(STATE_CONNECTION_STARTED)) {

            // Give some feedback so the user knows the area where autoscrolling
            // will occur.
            if (getAutoexposeHelper() != null) {
                return SharedCursors.HAND;
            } else {

                // Give some feedback so the user knows that they can't drag
                // outside the viewport.
                if (getCurrentViewer() != null) {
                    Control control = getCurrentViewer().getControl();
                    if (control instanceof FigureCanvas) {
                        Viewport viewport = ((FigureCanvas) control)
                            .getViewport();
                        Rectangle rect = Rectangle.SINGLETON;
                        viewport.getClientArea(rect);
                        viewport.translateToParent(rect);
                        viewport.translateToAbsolute(rect);

                        if (!rect.contains(getLocation())) {
                            return getDisabledCursor();
                        }
                    }
                }
            }
        }
        Command command = getCurrentCommand();
        if (command != null && command.canExecute())
        {
            EditPart ep = getTargetEditPart();
            if (ep instanceof DiagramEditPart || ep instanceof CompartmentEditPart)
                return CURSOR_TARGET_MENU;
        }
        return super.calculateCursor();
    }

}
