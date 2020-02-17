/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.figures;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;

import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

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
