/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * @author tisrar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * This interface implemented by editparts to evaluate if a 
 * note can be added as a peer connected by a note attachment to it. 
 */
public interface INoteableEditPart {

	/**
	 * This will return true if a note can be added as a peer connected by a note 
	 * attachment to the editpart which will implement this interface. 
	 *  
	 * @return boolean True: if note can be attached to it, false otherwise.
	 */
	public boolean canAttachNote();
	
}
