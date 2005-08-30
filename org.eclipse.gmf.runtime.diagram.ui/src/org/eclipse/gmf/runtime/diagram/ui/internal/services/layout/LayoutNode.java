/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import com.ibm.xtools.notation.Node;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
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
