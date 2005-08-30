/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp.       2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * Interface for those edit parts that support surface operations.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface ISurfaceEditPart {
	
	/**
	 * Returns true if the surface edit part is to support
	 * the view actions.  False otherwise.
	 * @return boolean isSupportingViewActions
	 */
	public boolean isSupportingViewActions();
	
	/**
	 * Setter for isSupportingViewActions
	 * @param boolean supportsViewActions
	 */
	public void setIsSupportingViewActions(boolean supportsViewActions);
	
}
