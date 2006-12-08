/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.ui.action.AbstractModelActionDelegate;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * An action delegate that handles duplication of selected model elements and
 * views on a diagram.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 */
public class DuplicateActionDelegate
	extends AbstractModelActionDelegate
	implements IObjectActionDelegate, IWorkbenchWindowActionDelegate, IHandler{
    
	/**
	 * Runs this duplicate action delegate by executing a duplicate command on
	 * the selected model elements or views.
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionDelegate#doRun(IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		Object request;
		ICommand cmd;
		if (getWorkbenchPart() instanceof IDiagramWorkbenchPart) {
			request = new DuplicateRequest();
			cmd = getDuplicateViewCommand(getStructuredSelection(),
				getWorkbenchPart(), (DuplicateRequest) request);
		} else {
			request = new DuplicateElementsRequest(getEditingDomain(getStructuredSelection()));
			cmd = getDuplicateElementsCommand(getStructuredSelection(),
				(DuplicateElementsRequest) request);
		}
		if (cmd != null && cmd.canExecute()) {
            IStatus status = execute(cmd, progressMonitor, null);

			if (status.isOK()) {
				if (request instanceof DuplicateRequest) {
					selectViews(((DuplicateRequest) request)
						.getDuplicatedViews());
				} else {
					// This should select the new elements in ME. Once
					// RATLC00533879 is fixed, this can be implemented.
				}
			}
		}
	}

	/**
     * Determines if the selection can be duplicated by trying to get a command
     * to do so.
     * 
     * @param selection
     * @param workbenchPart
     * @return true if the selection can be duplicated; false otherwise.
     */
	static boolean canDuplicate(IStructuredSelection selection,
			IWorkbenchPart workbenchPart) {

		ICommand cmd = (workbenchPart instanceof IDiagramWorkbenchPart) ? getDuplicateViewCommand(
			selection, workbenchPart, new DuplicateRequest())
			: getDuplicateElementsCommand(selection,
				new DuplicateElementsRequest(getEditingDomain(selection)));

		return (cmd != null && cmd.canExecute());
	}

	/**
	 * Tries to get a command to duplicate the selected elements by going to the
	 * semantic service.
	 * 
	 * @param selection
	 *            the selected elements
	 * @param request
	 *            the empty <code>DuplicateElementsRequest</code>
	 * @return a command to duplicate the elements, or null if one could not be
	 *         found.
	 */
	private static ICommand getDuplicateElementsCommand(
			IStructuredSelection selection, DuplicateElementsRequest request) {
		HashSet elements = new HashSet();
		for (Iterator i = selection.iterator(); i.hasNext();) {
			EObject element = (EObject) ((IAdaptable) i.next())
				.getAdapter(EObject.class);
			if (null != element) {
				elements.add(element);
			}
		}

		if (!elements.isEmpty()) {
			request.setElementsToBeDuplicated(new ArrayList(elements));
            
            IElementType elementType = ElementTypeRegistry.getInstance()
                .getElementType(request.getEditHelperContext());
            
            if (elementType != null) {
                return elementType.getEditCommand(request);
            }
		}
		return null;
	}

	/**
	 * Tries to get a command to duplicate the selected views by sending a
	 * request to the diagram editpart.
	 * 
	 * @param selection
	 *            the selected editparts
	 * @param workbenchPart
	 *            the workbench part from which the diagram editpart can be
	 *            found
	 * @param request
	 *            the empty <code>DuplicateElementsRequest</code>
	 * @return a command to duplicate the views (and underlying elements), or
	 *         null if one could not be found.
	 */
	private static ICommand getDuplicateViewCommand(
			IStructuredSelection selection, IWorkbenchPart workbenchPart,
			DuplicateRequest request) {
		List eps = new ArrayList();
		for (Iterator i = selection.iterator(); i.hasNext();) {
			Object selectedItem = i.next();
			if (selectedItem instanceof IGraphicalEditPart) {
				eps.add(selectedItem);
			}
		}

		if (!eps.isEmpty()) {
			request.setEditParts(eps);
			Command cmd = ((IDiagramWorkbenchPart) workbenchPart)
				.getDiagramEditPart().getCommand(request);
			if (cmd != null && cmd.canExecute()) {
				return new CommandProxy(cmd);
			}
		}
		return null;
	}

	/**
	 * Selects the newly added views on the diagram.
	 */
	private void selectViews(List views) {
		IDiagramGraphicalViewer viewer = ((IDiagramWorkbenchPart) getWorkbenchPart())
			.getDiagramGraphicalViewer();
		if (viewer != null && views != null && !views.isEmpty()) {

			List editparts = new ArrayList();
			for (Iterator iter = views.iterator(); iter.hasNext();) {
				Object view = iter.next();
				if (view instanceof View) {
					Object ep = viewer.getEditPartRegistry().get(view);
					if (ep != null) {
						editparts.add(ep);
					}
				}
			}

			if (!editparts.isEmpty()) {
				viewer.setSelection(new StructuredSelection(editparts));
			}
		}
	}
    
    public static TransactionalEditingDomain getEditingDomain(
            IStructuredSelection selection) {

        for (Iterator i = selection.iterator(); i.hasNext();) {
            EObject element = (EObject) ((IAdaptable) i.next())
                .getAdapter(EObject.class);

            if (element != null) {
                TransactionalEditingDomain editingDomain = TransactionUtil
                    .getEditingDomain(element);

                if (editingDomain != null) {
                    return editingDomain;
                }
            }
        }
        return null;
    }
    
    // Documentation copied from superclass
    protected TransactionalEditingDomain getEditingDomain() {
        return getEditingDomain(getStructuredSelection());
    }

    public void addHandlerListener(IHandlerListener handlerListener) {
        // nothing
    }

    public Object execute(ExecutionEvent event)
        throws ExecutionException {
        return null;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isHandled() {
        return true;
    }

    public void removeHandlerListener(IHandlerListener handlerListener) {
        // nothing
        
    }

}