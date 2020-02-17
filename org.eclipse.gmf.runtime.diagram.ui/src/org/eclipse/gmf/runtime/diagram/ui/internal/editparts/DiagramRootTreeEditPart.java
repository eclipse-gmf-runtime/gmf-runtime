/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.editparts.RootTreeEditPart;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * An override of RootTreeEditPart to give the root's contents a TreeItem widget
 * instead of a Tree widget. This will make the root's contents (the diagram) show
 * in the Outline Tree viewer as the root tree item. It will also fix the problem
 * of deleting a view selects the next node in the tree istead of the diagram node
 * 
 * @author melaasar
 */
public class DiagramRootTreeEditPart extends RootTreeEditPart {

	private TreeItem diagramTreeItem;
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(org.eclipse.gef.EditPart, int)
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		diagramTreeItem = new TreeItem((Tree)getWidget(), 0, index);
		((TreeEditPart)childEditPart).setWidget(diagramTreeItem);
	}
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#removeChildVisual(org.eclipse.gef.EditPart)
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		diagramTreeItem.dispose();
		super.removeChildVisual(childEditPart);
	}

}
