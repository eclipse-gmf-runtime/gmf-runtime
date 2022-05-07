/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This is the action handler for the diagram menu.   Adds actions to 
 * create note and text views.
 *
 * @author schafe
 */
public class CreateShapeViewAction extends DiagramAction {

	protected String semanticHint;

	/**
	 * Constructor
	 * 
	 * @param workbenchPage, the workbench page
	 * @param id, the id of this action
	 * @param semanticHint
	 * @param label, the menu item label the user will see
	 * @param imageDescriptor, the image next to the label that the user sees
	 */
	public CreateShapeViewAction(
		IWorkbenchPage workbenchPage,
		String actionId,
		String semanticHint,
		String label,
		ImageDescriptor imageDescriptor) {

		super(workbenchPage);
		setId(actionId);
		setSemanticHint(semanticHint);
		setText(label);
		setToolTipText(label);
		setImageDescriptor(imageDescriptor);
	}

	/**
	 * Creates a new request to create the shape view.
	 * 
	 * @return A request to create the shape view.
	 */
	protected Request createTargetRequest() {
		ViewDescriptor viewDescriptor;
		viewDescriptor = new ViewDescriptor(null, Node.class,
			getSemanticHint(), getPreferencesHint());
		return new CreateViewRequest(viewDescriptor);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	protected void setSemanticHint(String hint) {
		this.semanticHint = hint;
	}

	protected String getSemanticHint() {
		return this.semanticHint;
	}	
	
	protected void updateTargetRequest() {
		super.updateTargetRequest();

		CreateViewRequest req = (CreateViewRequest) getTargetRequest();
		req.setLocation(getMouseLocation());
	}
    
    protected void doRun(IProgressMonitor progressMonitor) {
        super.doRun(progressMonitor);
        selectAddedObject();
    }
    
    /**
     * Selects the newly added shape view(s) by default.
     */
    protected void selectAddedObject() {
        Object result = ((CreateRequest) getTargetRequest()).getNewObject();
        if (!(result instanceof Collection)) {
            return;
        }
        final List editparts = new ArrayList(1);

        IDiagramGraphicalViewer viewer = getDiagramGraphicalViewer();
        if (viewer == null) {
            return;
        }

        Map editpartRegistry = viewer.getEditPartRegistry();
        for (Iterator iter = ((Collection) result).iterator(); iter.hasNext();) {
            Object viewAdaptable = iter.next();
            if (viewAdaptable instanceof IAdaptable) {
                Object editPart = editpartRegistry
                    .get(((IAdaptable) viewAdaptable).getAdapter(View.class));
                if (editPart != null)
                    editparts.add(editPart);
            }
        }

        if (!editparts.isEmpty()) {
            viewer.setSelection(new StructuredSelection(editparts));

            // automatically put the first shape into edit-mode
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    EditPart editPart = (EditPart) editparts.get(0);
                    editPart.performRequest(new Request(
                        RequestConstants.REQ_DIRECT_EDIT));
                }
            });
        }
    }

}
