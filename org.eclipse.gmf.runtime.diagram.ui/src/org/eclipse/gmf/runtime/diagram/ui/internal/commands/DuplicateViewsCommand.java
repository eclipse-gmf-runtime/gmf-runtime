/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import com.ibm.xtools.notation.Bounds;
import com.ibm.xtools.notation.LayoutConstraint;
import com.ibm.xtools.notation.Node;
import com.ibm.xtools.notation.View;

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

	/**
	 * Creates a new <code>DuplicateViewsCommand</code>.
	 * 
	 * @param label
	 *            the command label
	 * @param request
	 *            the <code>DuplicateElementsRequest</code> whose list of duplicated
	 *            views will be populated
	 * @param viewsToDuplicate
	 *            the views to be duplicated
	 * @param duplicatedElements
	 *            the map of elements that were duplicated that should be used
	 *            to change the duplicated views to reference its dupliated
	 *            element (if applicable)
	 */
	public DuplicateViewsCommand(String label, DuplicateRequest request,
			List viewsToDuplicate, Map duplicatedElements) {
		super(label, viewsToDuplicate);
		this.duplicatedElements = duplicatedElements;
		duplicatedViewsToBeReturned = request.getDuplicatedViews();
	}

	/**
	 * Creates a new <code>DuplicateViewsCommand</code>.
	 * 
	 * @param label
	 *            the command label
	 * @param request
	 *            the <code>DuplicateElementsRequest</code> whose list of duplicated
	 *            views will be populated
	 * @param viewsToDuplicate
	 *            the views to be duplicated
	 */
	public DuplicateViewsCommand(String label, DuplicateRequest request,
			List viewsToDuplicate) {
		this(label, request, viewsToDuplicate, null);
	}

	/**
	 * Overridden to association the duplicated views with the duplicated
	 * elements.
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		CommandResult result = super.doExecute(progressMonitor);

		if (!result.getStatus().isOK()) {
			return result;
		}

		// Update the duplicated views to reference the duplicated elements.
		if (duplicatedElements != null && !duplicatedElements.isEmpty()) {
			EcoreUtil.Copier copier = new EcoreUtil.Copier();
			copier.putAll(duplicatedElements);
			copier.putAll(getAllDuplicatedObjectsMap());
			copier.copyReferences();
		}

		for (Iterator iter = getObjectsToBeDuplicated().iterator(); iter
			.hasNext();) {
			View originalView = (View) iter.next();
			View duplicateView = (View) getAllDuplicatedObjectsMap().get(
				originalView);

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
					bounds.setX(x + MapMode.DPtoLP(10));
					int y = bounds.getY();
					bounds.setY(y + MapMode.DPtoLP(10));
				}
			}

			if (duplicateView != null) {
				duplicatedViewsToBeReturned.add(duplicateView);
			}
		}

		return newOKCommandResult(duplicatedViewsToBeReturned);

	}
}