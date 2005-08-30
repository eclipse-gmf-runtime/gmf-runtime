/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.io.IOException;

/**
 * This interface must be implemented by each class that handles an EMF metafile record.  
 * This is the basis for all metafile conversions.  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public interface IEmf2SvgConverter extends IRenderableObject
{
	/**
	 * Initializes the converter.  This method reads all required data out of the specified 
	 * metafile record.
	 * @param rec Record object containing all the data for this metafile record.
	 * @throws IOException if the data is not properly formed or unsupported.
	 */
	public void readEMFRecord( Record rec ) throws IOException;
}
