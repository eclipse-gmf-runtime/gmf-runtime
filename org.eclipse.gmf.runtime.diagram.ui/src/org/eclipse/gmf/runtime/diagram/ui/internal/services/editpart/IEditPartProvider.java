/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import org.eclipse.gef.RootEditPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import com.ibm.xtools.notation.View;

/**
 * Defines the factory methods for creating the various editpart elements.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface IEditPartProvider extends IProvider 
{

	/** Create an editpart mapped to the supplied view element. */
	public IGraphicalEditPart createGraphicEditPart(View view);
	
	/**
	 * Creates a <code>RootEditPart</code>. 
	 * @return the <code>RootEditPart</code>
	 */
	public RootEditPart createRootEditPart();
}

