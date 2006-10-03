/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderableObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * This interface must be implemented by each class that handles a WMF metafile record.  
 * This is the basis for all metafile conversions.  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public interface IWmf2SvgConverter extends IRenderableObject
{
	/**
	 * Initializes the converter.  This method reads all required data out of the 
	 * specified metafile record.
	 * @param rec Record object containing all the data for this metafile record.
	 * @throws IOException if the data is not properly formed or unsupported.
	 */
	public void readWMFRecord( Record rec ) throws IOException;

}
