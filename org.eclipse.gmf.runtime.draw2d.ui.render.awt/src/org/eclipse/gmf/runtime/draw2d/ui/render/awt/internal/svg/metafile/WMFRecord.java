/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * The WMFRecord class reads the record header from a WMF record, then reads the
 * record's data into a buffer.  The record header contains the type of the
 * record and the size of the record.
 * 
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
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
