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

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command duplicates a list of notational views and adds each duplicate to
 * it's originator's container. It also duplicates the semantic element that the
 * view references (if it does reference a semantic element). All references
 * between views and elements that are duplicated are updated to refer to the
 * new duplicated objects.
 * 
 * <p>
 * The command returns the new duplicated views.
 * </p>
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DuplicateViewsCommand
	extends DuplicateEObjectsCommand {

	/**
	 * This is a map passed in by the client of all the elements that have been
	 * duplicated (key is original element, value is duplicated element) so that
	 * the views that are duplicated can be updated to point to the
	 * corresponding duplicated element.
	 */
	Map duplicatedElements;

	/**
	 * This will be populated with the views that are duplicated after the
	 * command executes.
	 */
	List duplicatedViewsToBeReturned = new ArrayList();
	
	private Point offset = new Point(0, 0);

	/**
     * Creates a new <code>DuplicateViewsCommand</code>.
     * 
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label
     *            the command label
     * @param request
     *            the <code>DuplicateElementsRequest</code> whose list of
     *            duplicated views will be populated
     * @param viewsToDuplicate
     *            the views to be duplicated
     * @param duplicatedElements
     *            the map of elements that were duplicated that should be used
     *            to change the duplicated views to reference its dupliated
     *            element (if applicable)
     * @param offset
     *            the offset from the location of the original views where the
     *            new views will be placed.
     */
	public DuplicateViewsCommand(TransactionalEditingDomain editingDomain, String label, DuplicateRequest request,
			List viewsToDuplicate, Map duplicatedElements, Point offset) {
		super(editingDomain, label, viewsToDuplicate);
		this.duplicatedElements = duplicatedElements;
		duplicatedViewsToBeReturned = request.getDuplicatedViews();
		this.offset = offset;
	}

	/**
     * Creates a new <code>DuplicateViewsCommand</code>.
     * 
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param label
     *            the command label
     * @param request
     *            the <code>DuplicateElementsRequest</code> whose list of
     *            duplicated views will be populated
     * @param viewsToDuplicate
     *            the views to be duplicated
     * @param offset
     *            the offset from the location of the original views where the
     *            new views will be placed.
     */
	public DuplicateViewsCommand(TransactionalEditingDomain editingDomain, String label, DuplicateRequest request,
			List viewsToDuplicate, Point offset) {
		this(editingDomain, label, request, viewsToDuplicate, null, offset);
	}

	/**
	 * Overridden to association the duplicated views with the duplicated
	 * elements.
	 */
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		CommandResult result = super.doExecuteWithResult(progressMonitor, info);

		if (!result.getStatus().isOK()) {
			return result;
		}

		for (Iterator iter = getObjectsToBeDuplicated().iterator(); iter
			.hasNext();) {
			View originalView = (View) iter.next();
			View duplicateView = (View) getAllDuplicatedObjectsMap().get(
				originalView);

            // Update the duplicated views to reference the duplicated elements.
			EObject originalElement = duplicateView.getElement();
            if (duplicatedElements != null) {
                Object duplicateElement = duplicatedElements
                    .get(originalElement);
                if (duplicateElement != null) {
                    duplicateView.setElement((EObject) duplicateElement);
                }
            }
            
			// Remove source and target edges that were not duplicated.
			List sourceRefs = new ArrayList(duplicateView.getSourceEdges());
			for (Iterator iterator = sourceRefs.iterator(); iterator.hasNext();) {
				EObject edge = (EObject) iterator.next();
				if (!getAllDuplicatedObjectsMap().containsValue(edge)) {
					sourceRefs.remove(edge);
				}
			}
			List targetRefs = new ArrayList(duplicateView.getTargetEdges());
			for (Iterator iterator = targetRefs.iterator(); iterator.hasNext();) {
				EObject edge = (EObject) iterator.next();
				if (!getAllDuplicatedObjectsMap().containsValue(edge)) {
					targetRefs.remove(edge);
				}
			}

			if (duplicateView instanceof Node) {
				// Change the location of the duplicated views.
				LayoutConstraint layoutConstraint = ((Node) duplicateView)
					.getLayoutConstraint();
				if (layoutConstraint instanceof Bounds) {
					Bounds bounds = (Bounds) layoutConstraint;
					int x = bounds.getX();
					bounds.setX(x + offset.x);
					int y = bounds.getY();
					bounds.setY(y + offset.y);
				}
			} else if (duplicateView instanceof Edge) {
                assert originalView instanceof Edge;
                
                // If the source/target wasn't duplicated, then the copier
                // would not have set the source/target.
                Edge duplicateEdge = (Edge) duplicateView;
                Edge originalEdge = (Edge) originalView;

                boolean sourceDuplicated = duplicateEdge.getSource() != null;
                boolean targetDuplicated = duplicateEdge.getTarget() != null;

                if (!sourceDuplicated) {
                    duplicateEdge.setSource(originalEdge.getSource());
                }
                if (!targetDuplicated) {
                    duplicateEdge.setTarget(originalEdge.getTarget());
                }
            }

			if (duplicateView != null) {
				duplicatedViewsToBeReturned.add(duplicateView);
			}
		}

		return CommandResult.newOKCommandResult(duplicatedViewsToBeReturned);

	}  
    
}