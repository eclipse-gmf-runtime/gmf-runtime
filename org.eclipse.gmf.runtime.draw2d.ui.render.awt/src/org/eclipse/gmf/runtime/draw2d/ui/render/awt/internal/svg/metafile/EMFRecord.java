/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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

import java.io.BufferedInputStream;
import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * The EMFRecord class reads the record header from an EMF record, then reads the
 * record's data into a buffer.  The record header contains the type of the
 * record and the size of the record.
 * 
 * @author dhabib
 */
public class EMFRecord extends Record 
{
	/** Size of the metafile record header, which consists of 4 bytes for the type and 4 bytes for the record size */
	public static final int	EMR_HEADER_SIZE		= 8;

	/**
	 * Initializes the record by reading the record header from the specified stream, then reading the
	 * record data.
	 * @param stream Stream to read the data from.
	 * @throws IOException Thrown if there is an error in stream (end of stream encountered before all the data could be read)
	 */
	public EMFRecord( BufferedInputStream stream ) throws IOException
	{
		setType( (int)readDWORD( stream ) );
		setSize( (int) readDWORD( stream ) - 8 );	// Subtract 8 bytes for the header
		readData( stream );
	}

}
