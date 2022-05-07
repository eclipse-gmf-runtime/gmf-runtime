/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * Performs a drop of one or more semantic elements using a given transfer. The Drop is performed by
 * using a {@link DropObjectsRequest} to obtain a <code>Command</code> from the targeted
 * <code>EditPart</code>. The target edit part might re-interpret the <code>DropElementsRequest</code>
 * to mean another request. Note that some transfers on different OS's occur at the drop time, hence
 * live feedback cannot be provided for these transfers (i.e. command created). If this is the case then to enable
 * drag and drop on different platforms {@link #isDataTransfered()} needs to be implemented.
 * <P>
 * This class is <code>abstract</code>. Subclasses are responsible for providing the
 * appropriate <code>Transfer</code> object based on the type of elements that are being dragged.
 *
 * @author melaasar, aboyko
 */
public abstract class DiagramDropTargetListener
    extends AbstractTransferDropTargetListener {

    /**
     * Constructor for DiagramDropTargetListener.
     * @param viewer
     */
    public DiagramDropTargetListener(EditPartViewer viewer) {
        super(viewer);
        setEnablementDeterminedByCommand(true);
    }

    /**
     * Constructor for DiagramDropTargetListener.
     * @param viewer
     * @param xfer
     */
    public DiagramDropTargetListener(EditPartViewer viewer, Transfer xfer) {
        super(viewer, xfer);
        setEnablementDeterminedByCommand(true);
    }

    /**
     *
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
     */
    protected Request createTargetRequest() {
        return  new DropObjectsRequest();
    }

    /**
     * A helper method that casts the target Request to a DropElementsRequest.
     * @return DropElementsRequest
     */
    protected final DropObjectsRequest getDropObjectsRequest() {
        return ((DropObjectsRequest) getTargetRequest());
    }

    /**
     * gets a list of objects being dropped on the diagram
    * @return <code>List</code>
     */
    protected abstract List getObjectsBeingDropped();

    /**
     * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
     */
    public void dragEnter(DropTargetEvent event) {
        super.dragEnter(event);
        handleDragEnter(); // called to properly initialize the effect
    }

    /**
     * Called whenever the User enters the target. By default, the target Request and
     * target EditPart are updated, and feedback is
     */
    protected void handleDragEnter() {
        handleDragOver();
    }    

    /**
     * The purpose of a template is to be copied. Therefore, the drop operation can't be
     * anything but <code>DND.DROP_COPY</code>.
     * @see AbstractTransferDropTargetListener#handleDragOperationChanged()
     */
    protected void handleDragOperationChanged() {
        super.handleDragOperationChanged();
        /*
         * The edit policies creating the command for the drop request may modify the
         * required detail field, so it's being set for the event here. However, if the command
         * can't be created due to the fact that data hasn't been transfered yet then the request will
         * contain DND.DROP_NONE required detail that we don't want to set for the event.  
         */
        if (getDropObjectsRequest().getRequiredDetail() != DND.DROP_NONE) {
            getCurrentEvent().detail = getDropObjectsRequest().getRequiredDetail();
        }
    }

    /**
     * The purpose of a template is to be copied. Therefore, the Drop operation is set to
     * <code>DND.DROP_COPY</code> by default.
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDragOver()
     */
    protected void handleDragOver() {
        super.handleDragOver();
        /*
         * The edit policies creating the command for the drop request may modify the
         * required detail field, so it's being set for the event here. However, if the command
         * can't be created due to the fact that data hasn't been transfered yet then the request will
         * contain DND.DROP_NONE required detail that we don't want to set for the event.  
         */
        if (getDropObjectsRequest().getRequiredDetail() != DND.DROP_NONE) {
            getCurrentEvent().detail = getDropObjectsRequest().getRequiredDetail();
        }
        getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
    }

    /**
     * Overridden to select the request result if any
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
     */
    protected void handleDrop() {
        getViewer().setCursor(Cursors.WAIT);
        super.handleDrop();
		getViewer().setCursor(null);
        selectAddedViews();
    }

    /**
     * Selects the created views that could result from the drop request if any
     */
    private void selectAddedViews() {
        Object result = getDropObjectsRequest().getResult();
        if (result == null || !(result instanceof Collection))
            return;
        EditPartViewer viewer = getViewer();
        List editParts = new ArrayList();
        Iterator views = ((Collection)result).iterator();

        while (views.hasNext()) {
            Object view = views.next();
            if (view instanceof IAdaptable) {
                EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(((IAdaptable)view).getAdapter(View.class));
                if (editPart != null)
                    editParts.add(editPart);
            }
        }
                
        if (!editParts.isEmpty()) {
            //Force a layout first.
            viewer.getControl().forceFocus();
            getViewer().flush();
            viewer.setSelection(new StructuredSelection(editParts));
        }
    }

    /**
	 * Assumes that the target request is a {@link DropObjectsRequest}. GEF
	 * wipes out the request in {@link #isEnabled(DropTargetEvent)} method, we
	 * don't. Hence we just update the necessary fields: <li>the mouse location
	 * <li>the objects being dropped <li>the allowed detail that comes from the
	 * DND event
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#
	 * 	updateTargetRequest()
	 */
    protected void updateTargetRequest() {
        DropObjectsRequest request = getDropObjectsRequest();
        request.setLocation(getDropLocation());
        request.setObjects(getObjectsBeingDropped());
        request.setAllowedDetail(getCurrentEvent().operations);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
     */
    public boolean isEnabled(DropTargetEvent event) {
    	/*
    	 * Cache the current event 
    	 */
        setCurrentEvent(event);
        
        /*
         * Update the target request and target editpart
         */
        updateTargetRequest();
        updateTargetEditPart();
        
        if (getTargetEditPart() == null) {
            return false;
        } else if (isEnablementDeterminedByCommand() && isDataTransfered()) {
        	/*
        	 * Check the command only if:
        	 * 1) The data has been transfered from the drag source to the event
        	 * 2) The "ask for command" option is on. (It's "on" by default)
        	 */
            Command command = getCommand();
            return command != null && command.canExecute();
        } else {
        	/*
        	 * Otherwise we should enable the drop. Executable command needs to be created at the drop time anyway.
        	 * Hence, we'll fail the drop there if there is no target editpart or no data transfered. 
        	 */
            return true;
        }
    }

	/**
	 * It is not a common use case to have the transfered data at the drag time,
	 * hence live feedback cannot be provided for all types of DnD. Since the
	 * occurrence of the data transfer at the drag time depends mostly on the
	 * used OS and the the type of data transfer, clients are responsible to
	 * experiment with their DnD and come up with the appropriate implementation
	 * of this method for their specific DnD support. By default the method
	 * checks if there is a local selection transfer or data field in the event
	 * is not null.
	 * 
	 * @return <code>true</code> if data has been transfered.
	 */
    protected boolean isDataTransfered() {
        return LocalSelectionTransfer.getTransfer().getSelection() != null || getCurrentEvent().data != null;
    }

}