/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents one record of a metafile.  It contains the type of the
 * record, the size of the record (in bytes) and all the data contained in the 
 * record excluding the type/size information (each record is prefixed with the 
 * size and type information).  
 * @author dhabib
 */
public class Record 
{
	private int		m_size	= 0;
	private int	 	m_type	= 0;
	private byte[]	m_data	= null;
	
	/**
	 * Default constructor.
	 */
	protected Record()
	{
	    // Nothing to initialize
	}

	/**
	 * Initializes a record with a specific type and size.  Reads 'size' bytes from the input
	 * stream, but does not interpret the results.
	 * @throws IOException Thrown if there is an error in stream (end of stream encountered before all the data could be read)
	 */
	public Record( int type, int size, InputStream stream ) throws IOException
	{
		m_type = type;
		m_size = size;
		readData( stream );
	}
	
	/**
	 * @return The type of the record.  See the EMR_ constants in EMFTranscoder.
	 */
	public int getType()
	{
		return m_type;
	}

	/**
	 * Sets the type of the record.  Only derived classes are allowed to do this.
	 * @param type
	 */
	protected void setType( int type )
	{
		m_type = type;
	}
	
	/**
	 * @return The size of the record, excluding the size of the header.
	 */
	public int getSize()
	{
		return m_size;
	}

	/**
	 * Sets the size of the record.  Only derived classes are allowed to do this.
	 * @param type
	 */
	protected void setSize( int size )
	{
		m_size = size;
	}
	
	protected void readData( InputStream stream ) throws IOException
	{
		m_data = new byte[ m_size ];
		stream.read( m_data, 0, m_size );
	}
	
	/**
	 * Retrieves the DWORD at the specified offset.  Returns a 'long' instead of an 'int' since
	 * DWORD's are not signed, but it only reads 4 bytes.
	 * @param offset Offset into the data where the DWORD resides.
	 * @return The DWORD at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the DWORD is greater than the size of the data buffer.
	 */
	public long getDWORDAt( int offset ) throws IOException
	{
		long retval = 0;
		if( offset <= m_size - 4 )
		{
			int byte1 = m_data[offset] & 0xff;
			int byte2 = ( m_data[ offset + 1 ] & 0xff ) << 8;
			int byte3 = ( m_data[ offset + 2 ] & 0xff ) << 16;
			int byte4 = ( m_data[ offset + 3 ] & 0xff ) << 24;

			retval = ( byte1 + byte2 + byte3 + byte4 ) & 0xffffffff;
		}
		else
		{
			throw new IOException( "Read beyond end of data" ); 	//$NON-NLS-1$
		}
		
		return retval;
	}
	
	/**
	 * Retrieves the integer at the specified offset. 
	 * @param offset Offset into the data where the integer resides.
	 * @return The value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the integer is greater than the size of the data buffer.
	 */
	public int getIntAt( int offset ) throws IOException
	{
		int retval = 0;
		if( offset <= m_size - 4 )
		{
			int byte1 = m_data[offset] & 0xff;
			int byte2 = ( m_data[ offset + 1 ] & 0xff ) << 8;
			int byte3 = ( m_data[ offset + 2 ] & 0xff ) << 16;
			int byte4 = ( m_data[ offset + 3 ] & 0xff ) << 24;

			retval = ( byte1 | byte2 | byte3 | byte4 );
		}
		else
		{
			throw new IOException( "Read beyond end of data" );		//$NON-NLS-1$ 
		}
		
		return retval;
	}

	/**
	 * Retrieves the short at the specified offset. 
	 * @param offset Offset into the data where the short resides.
	 * @return The value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the short is greater than the size of the data buffer.
	 */
	public short getShortAt( int offset ) throws IOException
	{
		short retval = 0;
		if( offset <= m_size - 2 )
		{
			int byte1 = m_data[offset] & 0xff;
			int byte2 = ( m_data[ offset + 1 ] & 0xff ) << 8;
			
			retval 	= (short)( byte2 & 0xffff );
			retval |= (short)( byte1 & 0xff );
		}
		else
		{
			throw new IOException( "Read beyond end of data" );		//$NON-NLS-1$ 
		}
		
		return retval;
	}
	
	/**
	 * Retrieves the Color value at the specified offset.  The color is a windows COLORREF, which is
	 * converted into an appropriate Java Color object. 
	 * @param offset Offset into the data where the color resides.
	 * @return The value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the color is greater than the size of the data buffer.
	 */
	public Color getColorAt( int offset ) throws IOException
	{
		int colorref =  (int) getDWORDAt( offset );
		int red = colorref & 0xff;
		int green = ( colorref & 0xff00 ) >> 8;
		int blue = ( colorref & 0xff0000 ) >> 16;
		//int flags = ( colorref & 0x3000000 ) >> 24;
		
		return new Color( red, green, blue );
	}

	/**
	 * Retrieves the byte at the specified offset. 
	 * @param offset Offset into the data where the byte resides.
	 * @return The value at the specified location.
	 * @throws IOException Thrown if the offset is greater than or equal to the size of the data buffer.
	 */
	public int getByteAt( int offset ) throws IOException
	{
		int retval = 0;
		if( offset < m_size )
		{
			retval = m_data[ offset ] & 0xff; 
		}
		else
		{
			throw new IOException( "Read beyond end of data" );		//$NON-NLS-1$ 
		}
		
		return retval;
	}
	
	/**
	 * Retrieves the set of bytes at the specified offset.
	 * @param offset Offset into the data where the block of bytes begins
	 * @param count Number of bytes to retrieve
	 * @return An array containing the block of bytes.
	 * @throws IOException Thrown if the offset plus the count is greater thn the size of the data buffer.
	 */
	public byte[] getBytesAt( int offset, int count ) throws IOException
	{
		byte[] retval = null;
		if( offset + count <= m_size )
		{
			retval = new byte[ count ];
			for( int i = 0; i < count; i++ )
			{
				retval[ i ] = m_data[ offset + i ];
			}
		}
		else
		{
			throw new IOException( "Read beyond end of data" );		//$NON-NLS-1$ 
		}
		
		return retval;
	}

	/**
	 * Retrieves a 'long Dimension' at the specified offset.  That is, retrieves a Dimension that is stored
	 * in the data as two integers.
	 * @param offset Offset to the dimension in the data.
	 * @return A Dimension object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the dimension is greater than the size of the data buffer.
	 */
	public Dimension getDimensionLAt( int offset ) throws IOException
	{
		int w = getIntAt( offset );
		int h = getIntAt( offset + 4 );
		
		return new Dimension( w, h );
	}

	/**
	 * Retrieves a 'short Dimension' at the specified offset.  That is, retrieves a Dimension that is stored
	 * in the data as two shorts.
	 * @param offset Offset to the dimension in the data.
	 * @return A Dimension object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the dimension is greater than the size of the data buffer.
	 */
	public Dimension getDimensionAt( int offset ) throws IOException
	{
		int w = getShortAt( offset );
		int h = getShortAt( offset + 2 );
		
		return new Dimension( w, h );
	}

	/**
	 * Retrieves a 'short Point' at the specified offset.  That is, retrieves a Point that is stored
	 * in the data as two shorts.
	 * @param offset Offset to the point in the data.
	 * @return A Point object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the point is greater than the size of the data buffer.
	 */
	public Point getPointAt( int offset ) throws IOException
	{
		int x = getShortAt( offset );
		int y = getShortAt( offset + 2 );
		
		return new Point( x, y );
	}
	
	/**
	 * Retrieves a 'long Point' at the specified offset.  That is, retrieves a Point that is stored
	 * in the data as two integers.
	 * @param offset Offset to the point in the data.
	 * @return A Point object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the point is greater than the size of the data buffer.
	 */
	public Point getPointLAt( int offset ) throws IOException
	{
		int x = getIntAt( offset );
		int y = getIntAt( offset + 4 );
		
		return new Point( x, y );
	}
	
	/**
	 * Retrieves a 'short Rectangle' at the specified offset.  That is, retrieves a Rectangle that is stored
	 * in the data as four shorts.
	 * @param offset Offset to the rectangle in the data.
	 * @return A Rectangle object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the Rectangle is greater than the size of the data buffer.
	 */
	public Rectangle getRectangeAt( int offset ) throws IOException
	{
		int x = getShortAt( offset );
		int y = getShortAt( offset + 2 );
		int w = getShortAt( offset + 4 );
		int h = getShortAt( offset + 6 );
		
		return new Rectangle( x, y, w, h );
	}
	
	/**
	 * Retrieves a 'long Rectangle' at the specified offset.  That is, retrieves a Rectangle that is stored
	 * in the data as four integers.
	 * @param offset Offset to the rectangle in the data.
	 * @return A Rectangle object with the value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the Rectangle is greater than the size of the data buffer.
	 */
	public Rectangle getRectangleLAt( int offset ) throws IOException
	{
		int x 	= getIntAt( offset );
		int y 	= getIntAt( offset + 4 );
		int x1	= getIntAt( offset + 8 );
		int y1	= getIntAt( offset + 12 );
		
		int w = x1 - x;
		int h = y1 - y;
		
		return new Rectangle( x, y, w, h );
	}

	/**
	 * Retrieves a 2D scale/translate/rotate transform as a float[6] array.
	 * @param offset Offset to the transform in the data.
	 * @return A float[] with the value at the specified location.
	 * @throws IOException
	 */
	public float[] getTransformAt( int offset ) throws IOException
	{
		float[] xform = new float[6];
		for (int i = 0; i < 6; ++i) {
			xform[i] = getFloatAt( offset + i * 4);
		}
		return xform;
	}
	
	/**
	 * Retrieves the floating point value at the specified offset. 
	 * @param offset Offset into the data where the float resides.
	 * @return The value at the specified location.
	 * @throws IOException Thrown if the offset plus the size of the float is greater than the size of the data buffer.
	 */
	public float getFloatAt( int offset )  throws IOException
	{
		int bits = (int) ( getDWORDAt( offset ) & 0xffffffff );
		return Float.intBitsToFloat( bits );
	}
	
	/**
	 * Retrieves the set of characters at the specified offset.
	 * @param offset Offset into the data where the characters reside.
	 * @param numChars Number of characters to retrieve
	 * @return Array of characters at the specified location
	 * @throws IOException Thrown if the offset plus the (number of characters * 2) is greater than the size of the data buffer. 
	 */
	public char[] getCharsAt( int offset, int numChars ) throws IOException
	{
		char [] retval = null;
		
		if( offset + ( numChars * 2 ) < m_size )
		{
			retval = new char[ numChars ];
			
			for( int index = 0; index < numChars; index++ )
			{
				char lowByte 	= (char) (m_data[ offset + ( index * 2 ) ] & 0xff);
				char highByte 	= (char) (m_data[ offset + 1 + ( index * 2 ) ] & 0xff);
				retval[ index ] =  (char) (lowByte + (highByte << 8));
			}
		}
		else
		{
			throw new IOException( "Read beyond end of data" );		//$NON-NLS-1$ 
		}
		
		return retval;
	}

	protected long readDWORD( InputStream stream ) throws IOException
	{
		int byte1 = stream.read();
		int byte2 = stream.read();
		int byte3 = stream.read();
		int byte4 = stream.read();
		
		int retval = byte1 + 
					(byte2 << 8) +
					(byte3 << 16) +
					(byte4 << 24);
		return 	retval;
	}

	protected int readShort( InputStream stream ) throws IOException
	{
		int byte1 = stream.read();
		int byte2 = stream.read();
		
		int retval = byte1 + 
					(byte2 << 8);
		return 	retval;
	}
}
