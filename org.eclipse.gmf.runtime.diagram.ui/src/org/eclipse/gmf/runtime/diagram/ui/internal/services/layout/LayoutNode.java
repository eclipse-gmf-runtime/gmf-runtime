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

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.notation.Node;


/**
 * @author sshaw
 *
 * Concrete class implementing the ILayoutNode interface
 */
public class LayoutNode
	implements ILayoutNode {

	private Node node;
	private int width;
	private int height;
	
	public LayoutNode(Node node, int width, int height) {
		this.node = node;
		this.width = width;
		this.height = height;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode#getNode()
	 */
	public Node getNode() {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode#getSize()
	 */
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode#getSize()
	 */
	public int getHeight() {
		return height;
	}

	
}
