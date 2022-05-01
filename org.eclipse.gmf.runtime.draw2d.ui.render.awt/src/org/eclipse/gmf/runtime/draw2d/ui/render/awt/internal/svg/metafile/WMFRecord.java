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

/**
 * The WMFRecord class reads the record header from a WMF record, then reads the
 * record's data into a buffer.  The record header contains the type of the
 * record and the size of the record.
 * 
 * @author dhabib
 */
public class WMFRecord extends Record
{
	/** Size of the metafile record header, which consists of 2 bytes for the type and 4 bytes for the record size */
	public static final int	WMR_HEADER_SIZE		= 6;

	/**
	 * Initializes the record by reading the record header from the specified stream, then reading the
	 * record data.
	 * @param stream Stream to read the data from.
	 * @throws IOException Thrown if there is an error in stream (end of stream encountered before all the data could be read)
	 */
	public WMFRecord( BufferedInputStream stream ) throws IOException
	{
		// Note, last record has a size of 3 words, and a function id of 0.
		int size = (int) readDWORD( stream ) * 2;  // Size is in words, not bytes, so double it.
		int type = readShort( stream );
		
		setSize( size - 6 );	// Subtract 6 bytes for the header
		setType( type );
		readData( stream );
	}
}
