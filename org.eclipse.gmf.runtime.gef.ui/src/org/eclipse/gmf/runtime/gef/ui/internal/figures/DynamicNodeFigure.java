/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.figures;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;

import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */
public class DynamicNodeFigure extends NodeFigure {

	protected Hashtable connectionAnchors = new Hashtable(7);

	/** 
	 * Returns a string identifier associated with a given ConnectionAnchor.
	 * @param c ConnectionAnchor to determine the name of
	 * @return String name associated with the given ConnectionAnchor.
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if (connectionAnchors.containsValue(c)) {
			Iterator iter = connectionAnchors.keySet().iterator();
			String key;
			while (iter.hasNext()) {
				key = (String) iter.next();
				if (connectionAnchors.get(key).equals(c))
					return key;
			}
		}
		return null;
	}

}
