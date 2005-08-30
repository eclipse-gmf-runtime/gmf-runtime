/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import org.eclipse.gef.ui.actions.ActionBarContributor;


/**
 * Minimal action bar contributor that does not use the Contribution
 * Item Service.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class MinimalActionBarContributor extends ActionBarContributor{

	/**
	 * Do nothing.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		//do nothing
	}

	/**
	 * Do nothing.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		//do nothing
	}

}