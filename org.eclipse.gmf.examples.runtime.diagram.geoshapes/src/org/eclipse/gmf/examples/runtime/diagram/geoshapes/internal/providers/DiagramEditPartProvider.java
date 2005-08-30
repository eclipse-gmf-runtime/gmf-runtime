/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.providers;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Editpart provider for the geoshape diagram.
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 */

public class DiagramEditPartProvider extends AbstractEditPartProvider {	
	
	/**
	 * Gets a diagram's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getDiagramEditPartClass(View view ) {
		if (view.getType().equals("Geoshape")) { //$NON-NLS-1$
            return(DiagramEditPart.class);
        }
		return null;
	}
}
