/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import com.ibm.xtools.notation.View;

/**
 * Performs a drop of one or more semantic elements using a given transfer. The Drop is performed by
 * using a {@link DropObjectsRequest} to obtain a <code>Command</code> from the targeted
 * <code>EditPart</code>. The target edit part might re-interpret the <code>DropElementsRequest</code>
 * to mean another request.
 * <P>
 * This class is <code>abstract</code>. Subclasses are responsible for providing the
 * appropriate <code>Transfer</code> object based on the type of elements that are being dragged.
 * 
 * @author melaasar
 */
public abstract class DiagramDropTargetListener
	extends AbstractTransferDropTargetListener {

	/**
	 * Constructor for DiagramDropTargetListener.
	 * @param viewer
	 */
	public DiagramDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	/**
	 * Constructor for DiagramDropTargetListener.
	 * @param viewer
	 * @param xfer
	 */
	public DiagramDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	/**
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		DropObjectsRequest request =  new DropObjectsRequest();
		request.setObjects(getObjectsBeingDropped());
		request.setAllowedDetail(getCurrentEvent().operations);
		return request;
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
		getCurrentEvent().detail = getDropObjectsRequest().getRequiredDetail();
	}

	/**
	 * The purpose of a template is to be copied. Therefore, the Drop operation is set to
	 * <code>DND.DROP_COPY</code> by default.
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDragOver()
	 */
	protected void handleDragOver() {
		super.handleDragOver();
		getCurrentEvent().detail = getDropObjectsRequest().getRequiredDetail();
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
	 * Assumes that the target request is a {@link DropObjectsRequest}. 
	 */
	protected void updateTargetRequest() {
		DropObjectsRequest request = getDropObjectsRequest();
		request.setLocation(getDropLocation());
	}

	/**
	 * Make sure the target can produce a command
	 */
	public boolean isEnabled(DropTargetEvent event) {
		if (super.isEnabled(event)){
			boolean result = calculateTargetEditPart() != null;
			return result;
		}
		return false;
	}

	/**
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetEditPart()
	 */
	protected void updateTargetEditPart() {
		setTargetEditPart(calculateTargetEditPart());
	}

	/**
	 * @return an edit part that can produce an executable command for the target request
	 */
	private EditPart calculateTargetEditPart() {
		updateTargetRequest();
		EditPart ep = getViewer()
			.findObjectAtExcluding(
				getDropLocation(),
				getExclusionSet(),
				new EditPartViewer.Conditional() {
					public boolean evaluate(EditPart editpart) {
						Command command = editpart.getCommand(getTargetRequest());
						return command != null && command.canExecute();
					}
				});
		if (ep != null) {
			Command command = ep.getCommand(getTargetRequest());
			return (command != null && command.canExecute())? ep : null;
		}
		return ep;
	}

}
