/******************************************************************************
 * Copyright (c) 2004-2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IInternalLayoutRunnable;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.util.Assert;

/**
 * @author sshaw
 * 
 * Abstract provider for internal layout providers. Provides a redirection from
 * the Node based api to the internal EditPart based api.
 */
abstract public class AbstractLayoutEditPartProvider extends
		AbstractLayoutNodeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider#layoutNodes(java.util.List,
	 *      boolean, org.eclipse.core.runtime.IAdaptable)
	 */
	public Runnable layoutLayoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		IGraphicalEditPart gep = (IGraphicalEditPart) layoutHint
				.getAdapter(IGraphicalEditPart.class);
		List editparts = new ArrayList(layoutNodes.size());
		Assert.isNotNull(gep, "The Graphical EditPart is null"); //$NON-NLS-1$

		Map registry = gep.getViewer().getEditPartRegistry();
		Iterator nodes = layoutNodes.listIterator();
		while (nodes.hasNext()) {
			Node view = ((ILayoutNode) nodes.next()).getNode();
			editparts.add(registry.get(view));
		}

		if (offsetFromBoundingBox) {
			final Command cmdSelect = layoutEditParts(editparts, layoutHint);
			return new IInternalLayoutRunnable() {
				public void run() {
					cmdSelect.execute();
				}

				public Command getCommand() {
					return cmdSelect;
				}
			};
		} else {
			final Command cmdDiag = layoutEditParts(gep, layoutHint);

			return new IInternalLayoutRunnable() {
				public void run() {
					cmdDiag.execute();
				}

				public Command getCommand() {
					return cmdDiag;
				}
			};
		}
	}

	/**
	 * Layout the objects in this container using the specified layout type.
	 * 
	 * @param containerEditPart
	 *            <code>GraphicalEditPart</code> that is the container for the
	 *            layout operation.
	 * @param layoutHint
	 *            <code>IAdaptable</code> hint to the provider to determine
	 *            the layout kind.
	 * @return <code>Command</code> that when executed will layout the edit
	 *         parts in the container
	 */
	abstract public Command layoutEditParts(
			GraphicalEditPart containerEditPart, IAdaptable layoutHint);

	/**
	 * Layout this list of selected objects, using the specified layout hint.
	 * The selected objects all reside within the same parent container. Other
	 * elements that are part of the container but not specified in the list of
	 * objects, are ignored.
	 * 
	 * @param selectedObjects
	 *            <code>List</code> of <code>EditPart</code> objects that
	 *            are to be layed out.
	 * @param layoutHint
	 *            <code>IAdaptable</code> hint to the provider to determine
	 *            the layout kind.
	 * @return <code>Command</code> that when executed will layout the edit
	 *         parts in the container
	 */
	abstract public Command layoutEditParts(List selectedObjects,
			IAdaptable layoutHint);
}
