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

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.DiagramViewFactory;

/**
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 * 
 * View provider for the geoshape diagram.
 */
public class DiagramViewProvider extends AbstractViewProvider { 

	HashMap diagramMap = new HashMap(); 
	{
		diagramMap.put("Geoshape", DiagramViewFactory.class);//$NON-NLS-1$
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.view.AbstractViewProvider#getDiagramViewClass(IAdaptable, java.lang.String)
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter, String diagramKind) {
		return (Class) diagramMap.get(diagramKind);
	}
}

