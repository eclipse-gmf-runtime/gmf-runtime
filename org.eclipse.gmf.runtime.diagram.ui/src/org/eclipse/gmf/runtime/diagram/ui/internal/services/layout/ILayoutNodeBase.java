/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gmf.runtime.notation.Node;


/**
 * Interface that wraps the Node view in order to retrieve the size when the 
 * Node's extent (either width or height) have been autosized.
 * 
 * This interface can be used in the layout provider implementation class.
 * 
 * @author sshaw
 * @canBeSeenBy %level1
 */
public interface ILayoutNodeBase {

	/**
	 * getNode
	 * Accessor method to return the notation meta-model Node object.
	 * 
	 * @return Node
	 */
	public Node getNode();
}
