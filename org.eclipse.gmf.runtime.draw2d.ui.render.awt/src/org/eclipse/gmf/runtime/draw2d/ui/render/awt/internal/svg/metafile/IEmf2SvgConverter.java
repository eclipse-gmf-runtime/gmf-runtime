/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderableObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * This interface must be implemented by each class that handles an EMF metafile record.  
 * This is the basis for all metafile conversions.  
 * @author dhabib
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
