/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.editpolicies;

import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

/**
 * Extends GEF's <code>GraphicalEditPolicy</code> to add the ability to be 
 * refreshed from its editpart.
 * 
 * @author chmahone
 */
public abstract class GraphicalEditPolicyEx extends GraphicalEditPolicy {

	/**
	 * This method is called when the editpart is refreshed.
	 */
	public void refresh() {
	  // Default behaviour is to do nothing
	}
}
